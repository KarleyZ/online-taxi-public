package com.ling.servicedriveruser.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityDriverUserService {

    @Autowired
    DriverUserMapper driverUserMapper;

    public ResponseResult<Boolean> isAvailableDriver(String cityCode){
        int i = driverUserMapper.selectDriverUserByCityCode(cityCode);
        if(i>0){
            return ResponseResult.success(true);
        }else{
            return ResponseResult.success(false);
        }
    }
}
