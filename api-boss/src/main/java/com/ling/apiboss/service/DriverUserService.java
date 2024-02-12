package com.ling.apiboss.service;

import com.ling.apiboss.remote.ServiceUserDriverClient;
import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverUserService {

    @Autowired
    private ServiceUserDriverClient serviceUserDriverClient;

    public ResponseResult addDriverUser(DriverUser driverUser){
        return serviceUserDriverClient.addDriverUser(driverUser);
    }

    public ResponseResult updateDriverUser(DriverUser driverUser){
        return serviceUserDriverClient.updateDriverUser(driverUser);
    }
}
