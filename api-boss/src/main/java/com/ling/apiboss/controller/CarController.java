package com.ling.apiboss.controller;

import com.ling.apiboss.service.CarService;
import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {


    @Autowired
    CarService carService;

    /**
     * 添加车辆信息
     * @param car
     * @return
     */
    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car){
        return carService.addCar(car);
    }
}
