package com.ling.apidriver.remote;

import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.DriverUserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/user")
    public ResponseResult updateUser(DriverUser driverUser);

    @RequestMapping(method = RequestMethod.GET, value = "/check-driver/{driverPhone}")
    public ResponseResult<DriverUserExistsResponse> checkDriverUser(@PathVariable("driverPhone") String driverPhone);
}
