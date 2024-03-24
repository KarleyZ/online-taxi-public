package com.ling.serviceorder.controller;

import com.ling.internalcommon.dto.OrderInfo;
import com.ling.serviceorder.mapper.OrderInfoMapper;
import com.ling.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @GetMapping("/test")
    public String test(){
        return "service-order is ok!";
    }

    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatRealTimeOrder(@PathVariable("orderId") Long orderId){
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchRealTimeOrder(orderInfo);
        return "test success!";
    }
}
