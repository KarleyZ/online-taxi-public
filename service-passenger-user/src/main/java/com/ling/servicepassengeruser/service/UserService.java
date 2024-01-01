package com.ling.servicepassengeruser.service;

import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public ResponseResult LoginOrRegister(String passengerPhone){

        //根据手机号查询用户信息是否存在，存在返回token,不存在添加用户信息
        return ResponseResult.success();
    }
}
