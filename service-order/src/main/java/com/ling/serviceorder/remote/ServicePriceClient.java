package com.ling.serviceorder.remote;

import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-price")
public interface ServicePriceClient {

    @RequestMapping(method = RequestMethod.GET,value = "/price-rule/is-new")
    public ResponseResult isNew(@RequestParam String fareType,@RequestParam Integer fareVersion);

    @RequestMapping(method = RequestMethod.GET,value = "/price-rule/if-exist")
    public ResponseResult<Boolean> ifExists(@RequestParam String cityCode,@RequestParam String vehicleType);

}
