package com.ling.serviceorder.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-map")
public interface ServiceMapClient {

    @RequestMapping(method = RequestMethod.POST,value = "/terminal/aroundsearch")
    public ResponseResult<List<MapTerminalResponse>> aroundSearch(@RequestParam String center, @RequestParam Integer radius);
}
