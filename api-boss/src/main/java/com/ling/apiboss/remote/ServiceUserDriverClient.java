package com.ling.apiboss.remote;

import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.DriverCarBindingRelationship;
import com.ling.internalcommon.dto.DriverUser;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.DriverUserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-driver-user")
public interface ServiceUserDriverClient {

    @RequestMapping(method = RequestMethod.POST,value = "/user")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.PUT, value = "/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.POST, value = "/car")
    public ResponseResult addCar(@RequestBody Car car);

    @RequestMapping(method = RequestMethod.POST, value = "/driver-car-binding-relationship/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    @RequestMapping(method = RequestMethod.POST, value = "/driver-car-binding-relationship/unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    @RequestMapping(method = RequestMethod.GET, value = "/check-driver/{driverPhone}")
    public ResponseResult<DriverUserExistsResponse> checkDriverUser(@PathVariable("driverPhone") String driverPhone);
}
