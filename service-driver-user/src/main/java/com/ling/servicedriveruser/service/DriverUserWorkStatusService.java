package com.ling.servicedriveruser.service;

import com.ling.internalcommon.dto.DriverUserWorkStatus;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ling
 * @since 2024-02-14
 */
@Service
public class DriverUserWorkStatusService{

    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult changeWorkStatus(Long driverId, Integer workStatus){

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("driver_id",driverId);
        List<DriverUserWorkStatus> driverUserWorkStatuses = driverUserWorkStatusMapper.selectByMap(queryMap);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatuses.get(0);

        driverUserWorkStatus.setWorkStatus(workStatus);
        driverUserWorkStatus.setGmtModified(LocalDateTime.now());

        driverUserWorkStatusMapper.updateById(driverUserWorkStatus);

        return ResponseResult.success();

    }

}
