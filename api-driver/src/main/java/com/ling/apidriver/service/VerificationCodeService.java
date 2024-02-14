package com.ling.apidriver.service;


import com.ling.apidriver.remote.ServiceDriverUserClient;
import com.ling.apidriver.remote.ServiceVerificationCodeClient;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.DriverCarConstants;
import com.ling.internalcommon.constant.IdentityConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.DriverUserExistsResponse;
import com.ling.internalcommon.response.NumberCodeResponse;
import com.ling.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
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
}
