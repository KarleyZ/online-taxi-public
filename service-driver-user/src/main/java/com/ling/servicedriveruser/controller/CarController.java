package com.ling.servicedriveruser.controller;


import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ling
 * @since 2024-02-04
 */
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car){

        return carService.addCar(car);
    }

    @GetMapping("/car")
    public ResponseResult<Car> getCarById(Long carId){

        return carService.getCarById(carId);
    }
}
