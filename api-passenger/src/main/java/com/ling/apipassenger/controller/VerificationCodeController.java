package com.ling.apipassenger.controller;


import com.ling.apipassenger.service.VerificationCodeService;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.VerificationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();

        System.out.println("验证的乘客手机号" + passengerPhone);
        System.out.println("验证的验证码" + verificationCode);


        return verificationCodeService.checkCode(passengerPhone,verificationCode);
    }
}
