package com.ling.apipassenger.controller;

import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testApiPassengerApplicaiton")
    public String testApiPassengerApplicaiton(){

        return "ApiPassengerApplicaiton";
    }

    /*需要token的身份验证*/
    @GetMapping("/authtest")
    public ResponseResult authTest(){

        return ResponseResult.success("auth test");
    }

    /*不需要token的身份验证*/
    @GetMapping("/noauthtest")
    public ResponseResult noauthTest(){

        return ResponseResult.success("no auth test");
    }
}
