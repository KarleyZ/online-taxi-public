package com.ling.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.DriverCarConstants;
import com.ling.internalcommon.dto.DriverCarBindingRelationship;
import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.DriverUserWorkStatus;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.OrderDriverResponse;
import com.ling.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.ling.servicedriveruser.mapper.DriverUserMapper;
import com.ling.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserService {
    @Autowired
    private DriverUserMapper driverUserMapper;

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult insertUser(DriverUser driverUser){

        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);

        driverUserMapper.insert(driverUser);

        //在成功添加driver后，在driver_user_work_status表中初始化该driver的工作状态的记录
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_START);
        driverUserWorkStatus.setGmtCreate(now);
        driverUserWorkStatus.setGmtModified(now);
        driverUserWorkStatusMapper.insert(driverUserWorkStatus);

        return ResponseResult.success("");
    }

    public ResponseResult updateUser(DriverUser driverUser){
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModified(now);

        driverUserMapper.updateById(driverUser);

        return ResponseResult.success("");

    }

    public ResponseResult getUserByPhone(String driverPhone){
        Map<String,Object> map = new HashMap<>();
        map.put("driver_phone",driverPhone);
        map.put("state", DriverCarConstants.DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if(driverUsers.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_NOT_EXISTS.getValue());
        }
        DriverUser driverUser = driverUsers.get(0);

        return ResponseResult.success(driverUser);

    }

    public ResponseResult<OrderDriverResponse> getAvailableDriver(Long carId){
        //合格的司机要绑定车辆，要出车，要处于有效状态
        //根据车辆绑定的信息表查询绑定的司机id
        //根据司机ID查询司机的手机信息
         QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_id",carId);
        queryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        DriverCarBindingRelationship relationship = driverCarBindingRelationshipMapper.selectOne(queryWrapper);
        Long driverId = relationship.getDriverId();
        QueryWrapper<DriverUserWorkStatus> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("driver_id",driverId);
        queryWrapper1.eq("work_status",DriverCarConstants.DRIVER_WORK_STATUS_START);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(queryWrapper1);
        if(null == driverUserWorkStatus){
            return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
        }else {
            QueryWrapper<DriverUser> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("id",driverId);
            queryWrapper2.eq("state",DriverCarConstants.DRIVER_STATE_VALID);

            DriverUser driverUser = driverUserMapper.selectOne(queryWrapper2);
            if(null == driverUser){
                return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
            }else{
                OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
                orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());
                orderDriverResponse.setDriverId(driverId);
                return ResponseResult.success(orderDriverResponse);
            }
        }
    }
}
