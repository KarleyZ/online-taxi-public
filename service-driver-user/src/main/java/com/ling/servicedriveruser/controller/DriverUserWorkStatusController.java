package com.ling.servicedriveruser.controller;


import com.ling.internalcommon.dto.DriverUserWorkStatus;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicedriveruser.service.DriverUserWorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ling
 * @since 2024-02-14
 */
@RestController
public class DriverUserWorkStatusController {

    @Autowired
    DriverUserWorkStatusService driverUserWorkStatusService;

    @PostMapping("/driver-user-work-status")
    public ResponseResult  changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){

        return driverUserWorkStatusService.changeWorkStatus(driverUserWorkStatus.getDriverId(),driverUserWorkStatus.getWorkStatus());
    }

}
