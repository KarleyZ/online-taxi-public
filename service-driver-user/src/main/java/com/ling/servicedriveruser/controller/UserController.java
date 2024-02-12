package com.ling.servicedriveruser.controller;

import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.mapper.DriverUserMapper;
import com.ling.servicedriveruser.service.DriverUserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
public class UserController {

    @Autowired
    DriverUserMapper driverUserMapper;

    @Autowired
    DriverUserService driverUserService;

    @PostMapping("/user")
    public ResponseResult insertUser(@RequestBody DriverUser driverUser){

        return driverUserService.insertUser(driverUser);

    }

    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        //log.info(JSONObject.fromObject(driverUser).toString());

        return  driverUserService.updateUser(driverUser);
    }
}
