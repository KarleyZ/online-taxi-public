package com.ling.apiboss.service;

import com.ling.apiboss.remote.ServiceUserDriverClient;
import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    ServiceUserDriverClient serviceUserDriverClient;

    public ResponseResult addCar(Car car){
        return serviceUserDriverClient.addCar(car);
    }
}
