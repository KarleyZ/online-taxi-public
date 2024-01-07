package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServicePassengerUserClient;
import com.ling.apipassenger.remote.ServiceVerificationcodeClient;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.IdentityConstant;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.VerificationCodeDTO;
import com.ling.internalcommon.response.NumberCodeResponse;
import com.ling.internalcommon.response.TokenResponse;
import com.ling.internalcommon.util.JwtUtils;
import com.ling.internalcommon.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class VerificationCodeService {
    //调用生成验证码服务远程客户端
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    //调用添加乘客远程客户端
    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;

    //Key和value是String类型的，因此采用StringRedisTemplate,如果是其他类型可以采用RedisTemplate
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 生成验证码
     * @param passengerPhone
     * @return
     */
    //这里api-passenger调用验证码生成服务不是直接调用，而是采用了nacos注册中心
    public ResponseResult generatorCode(String passengerPhone){
        //System.out.println("调用验证码生成服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        //System.out.println("remote service生成的数字验证码是"+ numberCode);
        //存验证码到redis
        //System.out.println("存验证码到redis");
        //存入redis需要什么：key,value和ktl
        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone);
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2,TimeUnit.MINUTES);

        //通过短信服务商将短信发送给乘客，阿里短信服务，腾讯短信通，华信，容联
        //先空者，后期调用

        //返回值 code + message
        return ResponseResult.success("");
    }


    /**
     * 校验验证码
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode){

        //根据手机号去redis获取验证码，需要先根据手机号生成key,根据key
        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone);

        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中 " + key + "保存的验证码是：" + codeRedis);

        //校验验证码是否正确
        //验证码不正确：不正确+过期两种情况
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        if(!verificationCode.trim().equals(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_INCORRECT.getCode(),CommonStatusEnum.VERIFICATION_CODE_INCORRECT.getValue());
        }

        VerificationCodeDTO verificationCodeDTO= new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        verificationCodeDTO.setVerificationCode(verificationCode);
        //判断原来是否有该用户，有就登录，没有就添加用户
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);
        //颁发令牌 JWT
        String token = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY);
        //将token存入redis方便之后的使用。
        String tokenKey = RedisPrefixUtils.generatorToken(passengerPhone,IdentityConstant.PASSENGER_IDENTITY);
        stringRedisTemplate.opsForValue().set(tokenKey,token,30,TimeUnit.DAYS);

        //相应的结果，要将token返回
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        return ResponseResult.success(tokenResponse);
    }

}
