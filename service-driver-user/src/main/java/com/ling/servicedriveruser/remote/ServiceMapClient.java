package com.ling.servicedriveruser.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.internalcommon.response.MapTrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {

    @RequestMapping(method = RequestMethod.POST,value = "/terminal/add")
    ResponseResult<MapTerminalResponse> addTerminal(@RequestParam String name);

    @RequestMapping(method = RequestMethod.POST,value = "/track/add")
    ResponseResult<MapTrackResponse> addTrack(@RequestParam String tid);
}
