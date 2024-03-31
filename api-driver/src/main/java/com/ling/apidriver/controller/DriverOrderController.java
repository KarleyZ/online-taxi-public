package com.ling.apidriver.controller;

import com.ling.apidriver.service.DriverOrderService;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverOrderController {

    @Autowired
    DriverOrderService orderService;

    /**
     * 司机出发去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){

        return orderService.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){
        return orderService.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客，行程开始
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderService.pickUpPassenger(orderRequest);
    }

    @PostMapping("/passenger-get-off")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest){
        return orderService.passengerGetOff(orderRequest);
    }


}
