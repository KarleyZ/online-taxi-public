package com.ling.servicedriveruser.controller;

import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.constant.DriverCarConstants;
import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.DriverUserExistsResponse;
import com.ling.servicedriveruser.mapper.DriverUserMapper;
import com.ling.servicedriveruser.service.DriverUserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class UserController {

    @Autowired
    DriverUserMapper driverUserMapper;

    @Autowired
    DriverUserService driverUserService;


    /**
     * 添加司机
     * @param driverUser
     * @return
     */
    @PostMapping("/user")
    public ResponseResult insertUser(@RequestBody DriverUser driverUser){

        return driverUserService.insertUser(driverUser);

    }

    /**
     * 更新司机
     * @param driverUser
     * @return
     */
    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        //log.info(JSONObject.fromObject(driverUser).toString());

        return  driverUserService.updateUser(driverUser);
    }

    /**
     * 根据手机号查询司机
     * @param driverPhone
     * @return
     */
    @GetMapping("/check-driver/{driverPhone}")
    public ResponseResult<DriverUserExistsResponse> getUser(@PathVariable("driverPhone") String driverPhone){
        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getUserByPhone(driverPhone);
        DriverUser driverUserDB = driverUserByPhone.getData();
        DriverUserExistsResponse driverUserResponse = new DriverUserExistsResponse();
        int isExists = DriverCarConstants.DRIVER_EXISTS;
        if (driverUserDB == null){
            isExists = DriverCarConstants.DRIVER_NOT_EXISTS;
            driverUserResponse.setDriverPhone(driverPhone);
        }else{
            driverUserResponse.setDriverPhone(driverUserDB.getDriverPhone());
        }
        driverUserResponse.setIsExists(isExists);

        return ResponseResult.success(driverUserResponse);
    }


}
