package com.ling.serviceorder.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.OrderConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import com.ling.internalcommon.dto.OrderInfo;
import com.ling.internalcommon.util.RedisPrefixUtils;
import com.ling.serviceorder.mapper.OrderInfoMapper;
import com.ling.serviceorder.remote.ServicePriceClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    ServicePriceClient servicePriceClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public ResponseResult add(OrderRequest orderRequest){
        //需要判断计价规则版本是否为最新
        ResponseResult<Boolean> aNew = servicePriceClient.isNew(orderRequest.getFareType(), orderRequest.getFareVersion());
        if(!(aNew.getData())){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(),CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
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
        return ResponseResult.success();
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

}
