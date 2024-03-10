package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServicePassengerUserClient;
import com.ling.internalcommon.dto.PassengerUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.dto.TokenResult;
import com.ling.internalcommon.request.VerificationCodeDTO;
import com.ling.internalcommon.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    ServicePassengerUserClient servicePassengerUserClient;

    public ResponseResult getUserByAccessToken(String accessToken){

        log.info("getUserByAccessToken时的accessToken" + accessToken);
        TokenResult tokenResult = JwtUtils.checkToken(accessToken);
        String phone = tokenResult.getPhone();

        ResponseResult<PassengerUser> result = servicePassengerUserClient.getUserByPhone(phone);

        return ResponseResult.success(result.getData());
    }
}
