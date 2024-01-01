package com.ling.servicepassengeruser.service;

import com.ling.internalcommon.dto.PassengerUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult LoginOrRegister(String passengerPhone){

        //根据手机号查询用户信息是否存在，存在返回token,
        Map<String, Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        //System.out.println(passengerUsers.size()==0?"无记录":passengerUsers.get(0).getPassengerName());
        if(passengerUsers.isEmpty()){
            PassengerUser passengerUser = new PassengerUser();
            passengerUser.setPassengerPhone(passengerPhone);
            passengerUser.setPassengerName("用户"+passengerPhone.substring(0,4));
            passengerUser.setPassengerGender((byte) 0);
            passengerUser.setState((byte) 0);

            LocalDateTime now = LocalDateTime.now();
            passengerUser.setGmtCreate(now);
            passengerUser.setGmtModified(now);

            passengerUserMapper.insert(passengerUser);
        }


        return ResponseResult.success();
    }
}
