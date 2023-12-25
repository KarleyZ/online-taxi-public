package com.ling.apipassenger.service;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;



@Service
public class VerificationCodeService {
    //调用验证码生成服务，获取验证码
    //这里api-passenger调用验证码生成服务不是直接调用，而是采用了nacos注册中心
    public String generatorCode(String passengerPhone){
        //调用验证码生成服务，获取验证码
        System.out.println("调用验证码生成服务，获取验证码");
        String verificationCode = "123456";

        //存验证码到redis
        System.out.println("存验证码到redis");

        //返回值 code + message
        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");
        return result.toString();
    }
}
