package com.ling.apipassenger.controller;

import com.ling.apipassenger.request.VerificationCodeDTO;
import com.ling.apipassenger.service.VerificationCodeService;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("乘客手机号："+passengerPhone);
        return verificationCodeService.generatorCode(passengerPhone);
    }
}
