package com.ling.apidriver.service;

import com.ling.apidriver.remote.ServiceDriverUserClient;
import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult updateUser(DriverUser driverUser){

        return serviceDriverUserClient.updateUser(driverUser);
    }
}
