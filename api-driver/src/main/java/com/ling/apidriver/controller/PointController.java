package com.ling.apidriver.controller;

import com.ling.apidriver.service.PointService;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.DriverPointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    PointService pointService;

    @RequestMapping("/upload")
    public ResponseResult upload(@RequestBody DriverPointRequest driverPointRequest){

        return pointService.upload(driverPointRequest);
    }
}
