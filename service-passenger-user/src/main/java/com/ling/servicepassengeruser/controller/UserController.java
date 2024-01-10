package com.ling.servicepassengeruser.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.VerificationCodeDTO;
import com.ling.servicepassengeruser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseResult LoginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("service-passenger-user中的乘客手机号：" + passengerPhone);
        return userService.LoginOrRegister(passengerPhone);
    }


    @GetMapping("/user-info/{phone}")
    public ResponseResult getUser (@PathVariable("phone") String passengerPhone){

     return userService.getUserByPhone(passengerPhone);
    }
}
