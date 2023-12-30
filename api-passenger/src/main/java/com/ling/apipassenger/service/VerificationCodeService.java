package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServiceVerificationcodeClient;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;
    //调用验证码生成服务，获取验证码
    //这里api-passenger调用验证码生成服务不是直接调用，而是采用了nacos注册中心
    public String generatorCode(String passengerPhone){
        //调用验证码生成服务，获取验证码
        System.out.println("调用验证码生成服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote service生成的数字验证码是"+ numberCode);
        //存验证码到redis
        System.out.println("存验证码到redis");

        //返回值 code + message
        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");
        return result.toString();
    }
}
