package com.ling.servicedriveruser.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.service.CityDriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverController {

    @Autowired
    CityDriverUserService cityDriverUserService;

    /**
     * 根据cityCode查询当前城市可用司机
     * @param cityCode
     * @return
     */
    @GetMapping("/is-available_driver")
    public ResponseResult<Boolean> isAvailableDriver(String cityCode){
        return cityDriverUserService.isAvailableDriver(cityCode);
    }
}
