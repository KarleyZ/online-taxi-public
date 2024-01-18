package com.ling.serviceprice.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.LocationInformationDTO;
import com.ling.internalcommon.response.DirectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-map")
public interface ServiceMapClient {

    @RequestMapping(method = RequestMethod.GET,value = "/direction/driving")
    public ResponseResult<DirectionResponse> direction(@RequestBody LocationInformationDTO locationInformationDTO);
}
