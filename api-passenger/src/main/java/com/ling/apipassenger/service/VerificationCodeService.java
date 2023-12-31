package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServiceVerificationcodeClient;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    //存入redis的key的前缀
    private String verificationCodePrefix ="passenger-verification-code";

    //Key和value是String类型的，因此采用StringRedisTemplate,如果是其他类型可以采用RedisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //调用验证码生成服务，获取验证码
    //这里api-passenger调用验证码生成服务不是直接调用，而是采用了nacos注册中心
    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码生成服务，获取验证码
        System.out.println("调用验证码生成服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote service生成的数字验证码是"+ numberCode);
        //存验证码到redis
        System.out.println("存验证码到redis");
        //存入redis需要什么：key,value和ktl
        String key = verificationCodePrefix + passengerPhone;
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2,TimeUnit.MINUTES);

        //通过短信服务商将短信发送给乘客，阿里短信服务，腾讯短信通，华信，容联
        //先空者，后期调用

        //返回值 code + message
        return ResponseResult.success("");
    }
}
