package com.ling.servicedriveruser.controller;

import com.ling.servicedriveruser.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    DriverUserMapper driverUserMapper;

    @GetMapping("/test-xml")
    public int testXml(String cityCode){
        String number = "1";
        int i = driverUserMapper.selectDriverUserByCityCode(cityCode);
        return i;
    }
}
