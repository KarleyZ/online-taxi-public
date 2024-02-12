package com.ling.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.DriverCarConstants;
import com.ling.internalcommon.dto.DriverCarBindingRelationship;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DriverCarBindingRelationshipService {

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship){
        //不可绑定一共有三种情况：本次绑定的车和司机已经绑定过了，司机绑定了其他车和车被其他司机绑定了
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        queryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);
        Integer result = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if(result.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_EXISTS.getCode(),CommonStatusEnum.DRIVER_CAR_BIND_EXISTS.getValue());
        }
        //司机被绑定
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);
        result = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if(result.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_BIND_EXISTS.getCode(),CommonStatusEnum.DRIVER_BIND_EXISTS.getValue());
        }
        //车辆被绑定
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        queryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);
        result = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if(result.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.CAR_BIND_EXISTS.getCode(),CommonStatusEnum.CAR_BIND_EXISTS.getValue());
        }

        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);
        driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);

        return ResponseResult.success("");

    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        //解绑需要查询到本次要解绑的信息，并判断装谈是否是有效，是的话，有效改失效。
        Map<String, Object> map = new HashMap<>();
        map.put("driver_id",driverCarBindingRelationship.getDriverId());
        map.put("car_id",driverCarBindingRelationship.getCarId());
        map.put("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        List<DriverCarBindingRelationship> driverCarBindingRelationships = driverCarBindingRelationshipMapper.selectByMap(map);
        if(driverCarBindingRelationships.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_BIND_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_BIND_NOT_EXISTS.getValue());
        }

        DriverCarBindingRelationship relationship = driverCarBindingRelationships.get(0);
        relationship.setBindState(DriverCarConstants.DRIVER_CAR_UNBIND);
        LocalDateTime now = LocalDateTime.now();
        relationship.setUnBindingTime(now);

        driverCarBindingRelationshipMapper.updateById(relationship);

        return ResponseResult.success("");
    }
}
