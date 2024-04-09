package com.ling.apipassenger.controller;

import com.ling.apipassenger.service.OrderService;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest){

        return orderService.add(orderRequest);
    }

    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId){
        return orderService.cancel(orderId);
    }
}
