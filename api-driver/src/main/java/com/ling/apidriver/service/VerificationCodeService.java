package com.ling.apidriver.service;


import com.ling.apidriver.remote.ServiceDriverUserClient;
import com.ling.apidriver.remote.ServiceVerificationCodeClient;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.DriverCarConstants;
import com.ling.internalcommon.constant.IdentityConstants;
import com.ling.internalcommon.constant.TokenConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.VerificationCodeDTO;
import com.ling.internalcommon.response.DriverUserExistsResponse;
import com.ling.internalcommon.response.NumberCodeResponse;
import com.ling.internalcommon.response.TokenResponse;
import com.ling.internalcommon.util.JwtUtils;
import com.ling.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeService {

    @Autowired
    ServiceDriverUserClient serviceUserDriverClient;

    @Autowired
    ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public ResponseResult checkAndSendVerificationCode(String driverPhone){
        //check driver
        ResponseResult<DriverUserExistsResponse> driverUserResponseResult = serviceUserDriverClient.checkDriverUser(driverPhone);
        DriverUserExistsResponse data = driverUserResponseResult.getData();
        int isExists = data.getIsExists();
        if(isExists == DriverCarConstants.DRIVER_NOT_EXISTS){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_NOT_EXISTS.getValue());
        }
        log.info("司机"+driverPhone +"存在！");
        //get verification code
        ResponseResult<NumberCodeResponse> numberCodeResult = serviceVerificationCodeClient.getNumberCode(6);
        NumberCodeResponse numberCodeResponse = numberCodeResult.getData();
        int numberCode = numberCodeResponse.getNumberCode();
        log.info("生成的验证码为："+ numberCode);
        //send verification code,第三方稍后写

        //存入redis
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone, IdentityConstants.DRIVER_IDENTITY);
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String driverPhone, String verificationCode){

        //根据手机号去redis获取验证码，需要先根据手机号生成key,根据key
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone,IdentityConstants.DRIVER_IDENTITY);

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

        //颁发令牌 JWT
        String accessToken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        String refreshToken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYPE);

        //将token存入redis方便之后的使用。
        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenConstants.ACCESS_TOKEN_TYPE);
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30,TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,31,TimeUnit.DAYS);

        //测试用，过期时间快
        //stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,25,TimeUnit.SECONDS);
        //stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,35,TimeUnit.SECONDS);


        //相应的结果，要将token返回
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);
    }
}
