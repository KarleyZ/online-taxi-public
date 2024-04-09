package com.ling.serviceorder.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.IdentityConstants;
import com.ling.internalcommon.constant.OrderConstants;
import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import com.ling.internalcommon.dto.OrderInfo;
import com.ling.internalcommon.request.PriceRuleIsNewRequest;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.internalcommon.response.OrderDriverResponse;
import com.ling.internalcommon.response.TrsearchResponse;
import com.ling.internalcommon.util.RedisPrefixUtils;
import com.ling.serviceorder.mapper.OrderInfoMapper;
import com.ling.serviceorder.remote.ServiceDriverUserClient;
import com.ling.serviceorder.remote.ServiceMapClient;
import com.ling.serviceorder.remote.ServicePriceClient;
import com.ling.serviceorder.remote.ServiceSsePushClient;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    ServicePriceClient servicePriceClient;

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    ServiceMapClient serviceMapClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ServiceSsePushClient serviceSsePushClient;

    //@Autowired
    //RedissonClient redissonClient;

    /**
     * 新建订单
     * @param orderRequest
     * @return
     */
    public ResponseResult add(OrderRequest orderRequest){
        //判断当前城市是否有司机
        if(!(HasAvailableDriver(orderRequest.getAddress()))){
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(),CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }
        //判断下单的城市和计价规则是否正常
        if(!(IsVaildCityAndRule(orderRequest.getFareType()))){
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_EXISTS.getCode(),CommonStatusEnum.CITY_SERVICE_NOT_EXISTS.getValue());
        }

        //需要判断计价规则版本是否为最新
        PriceRuleIsNewRequest priceRuleIsNewRequest = new PriceRuleIsNewRequest();
        priceRuleIsNewRequest.setFareType(orderRequest.getFareType());
        priceRuleIsNewRequest.setFareVersion(orderRequest.getFareVersion());
        ResponseResult aNew = servicePriceClient.isNew(priceRuleIsNewRequest);
        if(aNew.getCode() != 1){
            return aNew;
        }

        //判断乘客有正在进行的订单不允许下单
        if(HasPassengerOngoingOrders(orderRequest.getPassengerId())){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),CommonStatusEnum.ORDER_GOING_ON.getValue());
        }
        //判断下单的设备是否是黑名单设备
        if(IsBlackDevice(orderRequest.getDeviceCode())){
            return ResponseResult.fail(CommonStatusEnum.DEVICE_IS_BLACK.getCode(),CommonStatusEnum.DEVICE_IS_BLACK.getValue());
        }

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderRequest,orderInfo);

        orderInfo.setOrderStatus(OrderConstants.ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapper.insert(orderInfo);

        //派单
        //dispatchRealTimeOrder(orderInfo);
        //定时任务处理
        for (int i = 0; i < 6; i++) {
            int result = dispatchRealTimeOrder(orderInfo);
            if(result == 1){
                break;
            }
            //等20s
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseResult.success();
    }

    /**
     * 实时订单派单逻辑
     * @param orderInfo
     */
    //处理单机并发问题：1.方法加synchronized，重量锁，对整个方法太慢了，只添加到锁库部分
    public int dispatchRealTimeOrder(OrderInfo orderInfo){
        int result = 0;
        //2km
        //出发纬度
        String depLongitude = orderInfo.getDepLongitude();
        String depLatitude = orderInfo.getDepLatitude();
        int radius = 2000;
        String center = depLatitude + "," + depLongitude;
        ResponseResult<List<MapTerminalResponse>> listResponseResult;
        List<Integer> radiusList = new ArrayList<>();
        radiusList.add(2000);
        radiusList.add(4000);
        radiusList.add(5000);
        radius:
        for (int i = 0; i < radiusList.size(); i++) {
            listResponseResult = serviceMapClient.aroundSearch(center, radiusList.get(i));
            //获得终端
            log.info("在半径为"+radiusList.get(i));
            log.info("终端搜索结果"+ JSONArray.fromObject(listResponseResult.getData()).toString());
            //解析终端结果
            //这里有问题，没车怎么办？
            List<MapTerminalResponse> data = listResponseResult.getData();
            for (int j = 0; j < data.size(); j++) {
                MapTerminalResponse jsonObject = data.get(j);
                long carId = jsonObject.getCarId();
                String longitude = jsonObject.getLongitude();
                String latitude = jsonObject.getLatitude();

                //查询是否有可派单的司机。
                ResponseResult<OrderDriverResponse> availableDriver = serviceDriverUserClient.getAvailableDriver(carId);

                if(availableDriver.getCode() == CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()){
                    log.info("没有车辆"+carId+"对应的司机");
                    continue;
                }else {
                    log.info("找到了正在出车的司机" + carId);



                    //判断司机是否有正在进行的订单
                    OrderDriverResponse orderDriverResponse = availableDriver.getData();
                    Long driverId = orderDriverResponse.getDriverId();
                    /**
                     * 多集群并法问题用redisson来解决
                     */
                    //String lockKey = (driverId+"").intern();
                    //RLock lock = redissonClient.getLock(lockKey);
                    //lock.lock();
                    /**
                     * 锁司机id的小技巧:
                     * 为什么driverId不行，因为该值是对象.get得到的，对于不同线程其实是不同线程的值，
                     * 每次锁不同的值所以没锁住，注意driverId对象没变，引用（赋）的值不同
                     * 为什么intern可以？因为每次都将driverId的值字符串存入常量池，
                     * 该常量池将字符串常量如果有就返回该常量池，没有就添加并返回常量池
                     * 所以每次锁的是同一个常量池中的值
                     * */
                    //synchronized ((driverId+"").intern()){
                        if(HasDriverOngoingOrders(driverId)){
                            //lock.unlok();
                            continue;
                        }
                        //判断当前车型是否和乘客下订单选的一致?
                    String vehicleTypeFromCar = orderDriverResponse.getVehicleType();
                    if(!(vehicleTypeFromCar.trim().equals(orderInfo.getVehicleType().trim()))){
                        log.info("车型不符合！");
                        continue;
                    }
                    //补全订单中有关司机的信息
                        //查询当前车辆信息
                        orderInfo.setDriverId(driverId);
                        orderInfo.setDriverPhone(orderDriverResponse.getDriverPhone());
                        orderInfo.setCarId(carId);
                        orderInfo.setReceiveOrderCarLongitude(longitude);
                        orderInfo.setReceiveOrderCarLatitude(latitude);
                        orderInfo.setReceiveOrderTime(LocalDateTime.now());
                        orderInfo.setLicenseId(orderDriverResponse.getLicenseId());
                        orderInfo.setVehicleNo(orderDriverResponse.getVehicleNo());
                        orderInfo.setOrderStatus(OrderConstants.RECEIVE_ORDER);

                        orderInfoMapper.updateById(orderInfo);

                        //通知司机
                        JSONObject driverContent = new JSONObject();
                        driverContent.put("passengerId",orderInfo.getPassengerId());
                        driverContent.put("passengerPhone",orderInfo.getPassengerPhone());
                        driverContent.put("departure",orderInfo.getDeparture());
                        driverContent.put("depLongitude",orderInfo.getDepLongitude());
                        driverContent.put("depLatitude",orderInfo.getDepLatitude());
                        driverContent.put("destination",orderInfo.getDestination());
                        driverContent.put("destLongitude",orderInfo.getDestLongitude());
                        driverContent.put("destLatitude",orderInfo.getDestLatitude());

                        serviceSsePushClient.push(driverId, IdentityConstants.DRIVER_IDENTITY,driverContent.toString());

                        //通知乘客
                        JSONObject passengerContent = new JSONObject();
                        passengerContent.put("driverId",orderInfo.getDriverId());
                        passengerContent.put("driverPhone",orderInfo.getDriverPhone());
                        passengerContent.put("vehicleNo",orderInfo.getVehicleNo());
                        //额外再获取车辆信息
                        ResponseResult<Car> carById = serviceDriverUserClient.getCarById(orderInfo.getCarId());
                        Car car = carById.getData();
                        passengerContent.put("vehicleType",car.getVehicleType());
                        passengerContent.put("Brand",car.getBrand());
                        passengerContent.put("model",car.getModel());
                        passengerContent.put("vehicleColor",car.getVehicleColor());


                        passengerContent.put("receiveOrderCarLongitude",orderInfo.getReceiveOrderCarLongitude());
                        passengerContent.put("receiveOrderCarLatitude",orderInfo.getReceiveOrderCarLatitude());

                        serviceSsePushClient.push(orderInfo.getPassengerId(), IdentityConstants.PASSENGER_IDENTITY,passengerContent.toString());
                        result = 1;
                        //lock.unlock(); //redisson解锁
                        //退出不再进行司机的查找
                        break radius;
                    //}

                }
            }

        }
        return result;
    }

    /**
     * 乘客是否有正在进行的订单 true表示有
     * @param passengerId
     * @return
     */
    private Boolean HasPassengerOngoingOrders(Long passengerId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id",passengerId);
        queryWrapper.and(wrapper->wrapper.eq("order_status",OrderConstants.ORDER_START)
                .or().eq("order_status",OrderConstants.RECEIVE_ORDER)
                .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.PASSENGER_GET_OFF)
                .or().eq("order_status",OrderConstants.TO_START_PAY)
        );
        Integer vaildOrderCount = orderInfoMapper.selectCount(queryWrapper);
        if(vaildOrderCount >0){
            return true;
        }
        return false;
    }

    /**
     * 是否是黑名单设备， true表示为黑名单设备
     * @param deviceCode
     * @return
     */
    private Boolean IsBlackDevice(String deviceCode){
        String deviceCodeKey = RedisPrefixUtils.generatorBlackDeviceCodeKey(deviceCode);
        //查看是否有key
        Boolean aBoolean = stringRedisTemplate.hasKey(deviceCodeKey);
        if (aBoolean){
            String s = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int i = Integer.parseInt(s);
            if(i>=2){
                //当前为黑名单设备
                return true;
            }else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }
        }else {
            //请注意这里有一个点，在设置值和过期时间的时候不能分开设置，由于非原则性操作会导致时间设置不准确
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey,"1",1L, TimeUnit.HOURS);
        }
        return false;
    }

    /**
     * 计价规则是否合法
     * @param fareType
     * @return
     */
    private Boolean IsVaildCityAndRule(String fareType){
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);
        ResponseResult<Boolean> result = servicePriceClient.ifExists(cityCode, vehicleType);
        return result.getData();
    }

    /**
     * 当前城市是否存在可用司机
     * @param cityCode
     * @return
     */
    private Boolean HasAvailableDriver(String cityCode){
        ResponseResult<Boolean> result = serviceDriverUserClient.isAvailableDriver(cityCode);
        log.info("当前城市司机状态" + result.getData());
        return result.getData();
    }

    /**
     * 司机是否存在正在进行的订单
     * @param driverId
     * @return
     */
    private Boolean HasDriverOngoingOrders(Long driverId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverId);
        queryWrapper.and(wrapper->wrapper.eq("order_status",OrderConstants.RECEIVE_ORDER)
                .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER)
        );
        Integer vaildOrderCount = orderInfoMapper.selectCount(queryWrapper);
        log.info("司机"+ driverId +"正在进行的订单数"+vaildOrderCount);
        if(vaildOrderCount >0){
            return true;
        }
        return false;
    }

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        String toPickUpPassengerAddress = orderRequest.getToPickUpPassengerAddress();
        String toPickUpPassengerLongitude = orderRequest.getToPickUpPassengerLongitude();
        String toPickUpPassengerLatitude = orderRequest.getToPickUpPassengerLatitude();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setToPickUpPassengerAddress(toPickUpPassengerAddress);
        orderInfo.setToPickUpPassengerLongitude(toPickUpPassengerLongitude);
        orderInfo.setToPickUpPassengerLatitude(toPickUpPassengerLatitude);
        orderInfo.setToPickUpPassengerTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstants.DRIVER_TO_PICK_UP_PASSENGER);

        orderInfoMapper.updateById(orderInfo);

        return ResponseResult.success();
    }

    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    public ResponseResult arrivedDeparture(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setDriverArrivedDepartureTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstants.DRIVER_ARRIVED_DEPARTURE);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger( OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setPickUpPassengerTime(LocalDateTime.now());
        orderInfo.setPickUpPassengerLongitude(orderRequest.getPickUpPassengerLongitude());
        orderInfo.setPickUpPassengerLatitude(orderRequest.getPickUpPassengerLatitude());
        orderInfo.setOrderStatus(OrderConstants.PICK_UP_PASSENGER);
        orderInfoMapper.updateById(orderInfo);

        return ResponseResult.success();
    }

    /**
     * 乘客到达目的地，行程终止
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetOff(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setPassengerGetoffTime(LocalDateTime.now());
        orderInfo.setPassengerGetoffLongitude(orderRequest.getPassengerGetoffLongitude());
        orderInfo.setPassengerGetoffLatitude(orderRequest.getPassengerGetoffLatitude());
        orderInfo.setOrderStatus(OrderConstants.PASSENGER_GET_OFF);
        //订单行驶的路程和时间
        ResponseResult<Car> car = serviceDriverUserClient.getCarById(orderInfo.getCarId());
        ResponseResult<TrsearchResponse> trSearch = serviceMapClient.trSearch(car.getData().getTid(),
                orderInfo.getToPickUpPassengerTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                , LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        TrsearchResponse data = trSearch.getData();
        Long driverMile = data.getDriverMile();
        Long driverTime = data.getDriverTime();
        orderInfo.setDriveMile(driverMile);
        orderInfo.setDriveTime(driverTime);
        //更新价格
        ResponseResult<Double> doubleResponseResult = servicePriceClient.calculatePrice(driverMile.intValue(), driverTime.intValue(), orderInfo.getAddress(), orderInfo.getVehicleType());
        Double price = doubleResponseResult.getData();

        orderInfo.setPrice(price);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    /**
     * 乘客支付成功
     * @param orderRequest
     * @return
     */
    public ResponseResult pay(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setOrderStatus(OrderConstants.SUCCESS_PAY);

        orderInfoMapper.updateById(orderInfo);
        return  null;
    }

    /**
     * 取消订单
     * @param orderId
     * @param identity
     * @return
     */
    public ResponseResult cancel(Long orderId, String identity){
        //查询当前状态
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        Integer orderStatus = orderInfo.getOrderStatus();
        LocalDateTime cancelTime = LocalDateTime.now();
        Integer cancelTypeCode = null;

        //取消结果，被赋为0时表示取消订单失败
        int cancelType = 1;

        //如果是乘客取消
        if(identity.trim().equals(IdentityConstants.PASSENGER_IDENTITY)){
            switch (orderStatus){
                case OrderConstants.ORDER_START:
                    cancelTypeCode = OrderConstants.CANACEL_PASSENGER_BEFORE;//订单刚开始，乘客提前取消
                    break;
                case OrderConstants.RECEIVE_ORDER:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if(between > 1){
                        cancelTypeCode = OrderConstants.CANACEL_PASSENGER_ILLEGAL;//超过一分钟，乘客违约取消
                    }else {
                        cancelTypeCode = OrderConstants.CANACEL_PASSENGER_BEFORE;//未超过一分钟，乘客正常取消
                    }
                    break;
                case OrderConstants.DRIVER_TO_PICK_UP_PASSENGER:
                    cancelTypeCode = OrderConstants.CANACEL_PASSENGER_ILLEGAL;
                    break;
                case OrderConstants.DRIVER_ARRIVED_DEPARTURE:
                    cancelTypeCode = OrderConstants.CANACEL_PASSENGER_ILLEGAL;
                    break;
                default:
                    log.info("乘客取消失败");
                    cancelType = 0;
                    break;
            }
        }

        //如果是司机取消
        if(identity.trim().equals(IdentityConstants.DRIVER_IDENTITY)){
            switch (orderStatus){
                case OrderConstants.RECEIVE_ORDER:
                case OrderConstants.DRIVER_TO_PICK_UP_PASSENGER:
                case OrderConstants.DRIVER_ARRIVED_DEPARTURE:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if(between > 1){
                        cancelTypeCode = OrderConstants.CANACEL_DRIVER_ILLEGAL;//超过一分钟，司机违约取消
                    }else {
                        cancelTypeCode = OrderConstants.CANACEL_DRIVER_BEFORE;//未超过一分钟，司机正常取消
                    }
                    break;
                default:
                    log.info("司机取消失败");
                    cancelType = 0;
                    break;
            }
        }
        //更新订单的取消状态
        if(cancelType == 0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(),CommonStatusEnum.ORDER_CANCEL_ERROR.getValue());
        }
        orderInfo.setCancelTime(cancelTime);
        orderInfo.setCancelOperator(Integer.parseInt(identity));
        orderInfo.setCancelTypeCode(cancelTypeCode);
        orderInfo.setOrderStatus(OrderConstants.ORDER_CANCEL);
        orderInfoMapper.updateById(orderInfo);

        return ResponseResult.success();
    }
}
