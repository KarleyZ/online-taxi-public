package com.ling.serviceorder.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.OrderConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import com.ling.internalcommon.dto.OrderInfo;
import com.ling.internalcommon.request.PriceRuleIsNewRequest;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.internalcommon.util.RedisPrefixUtils;
import com.ling.serviceorder.mapper.OrderInfoMapper;
import com.ling.serviceorder.remote.ServiceDriverUserClient;
import com.ling.serviceorder.remote.ServiceMapClient;
import com.ling.serviceorder.remote.ServicePriceClient;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        //判断有正在进行的订单不允许下单
        if(HasOngoingOrders(orderRequest.getPassengerId())){
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
        dispatchRealTimeOrder(orderInfo);

        return ResponseResult.success();
    }

    /**
     * 实时订单派单逻辑
     * @param orderInfo
     */
    public void dispatchRealTimeOrder(OrderInfo orderInfo){
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
        for (int i = 0; i < radiusList.size(); i++) {
            listResponseResult = serviceMapClient.aroundSearch(center, radiusList.get(i));
            //获得终端
            log.info("在半径为"+radiusList.get(i));
            log.info("终端搜索结果"+ JSONArray.fromObject(listResponseResult.getData()).toString());
            //解析终端结果
            //这里有问题，没车怎么办？
            JSONArray result = JSONArray.fromObject(listResponseResult.getData());
            for (int j = 0; j < result.size(); j++) {
                JSONObject jsonObject = result.getJSONObject(j);
                String carIdString = jsonObject.getString("carId");
                long carId = Long.parseLong(carIdString);

                //查询是否有可派单的司机。

            }

        }

    }

    public Boolean HasOngoingOrders(Long passengerId){
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

    public Boolean IsBlackDevice(String deviceCode){
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

    public Boolean IsVaildCityAndRule(String fareType){
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);
        ResponseResult<Boolean> result = servicePriceClient.ifExists(cityCode, vehicleType);
        return result.getData();
    }

    public Boolean HasAvailableDriver(String cityCode){
        ResponseResult<Boolean> result = serviceDriverUserClient.isAvailableDriver(cityCode);
        log.info("当前城市司机状态" + result.getData());
        return result.getData();
    }
}
