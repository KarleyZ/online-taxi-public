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

}
