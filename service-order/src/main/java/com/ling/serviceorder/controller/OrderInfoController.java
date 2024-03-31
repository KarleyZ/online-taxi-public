package com.ling.serviceorder.controller;


import com.ling.internalcommon.constant.HeaderParamConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import com.ling.serviceorder.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ling
 * @since 2024-03-09
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderInfoController {

    @Autowired
    OrderInfoService orderInfoService;
    /**
     * 创建订单
     * @param orderRequest
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest){
        //扩展从header中获取设备码
        /*String deviceCode = httpServletRequest.getHeader(HeaderParamConstants.DEVICE_CODE);
        log.info("deviceCode:"+deviceCode);
        orderRequest.setDeviceCode(deviceCode);*/
        return orderInfoService.add(orderRequest);
    }

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult changeStatus(@RequestBody OrderRequest orderRequest){

        return orderInfoService.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达上车点
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){
        return orderInfoService.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderInfoService.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客到达目的地，行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-get-off")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest){
        return orderInfoService.passengerGetOff(orderRequest);
    }


}
