package com.ling.serviceorder.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.PriceRuleIsNewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-price")
public interface ServicePriceClient {

    @RequestMapping(method = RequestMethod.POST,value = "/price-rule/is-new")
    public ResponseResult isNew(@RequestBody PriceRuleIsNewRequest priceRuleIsNewRequest);

    @RequestMapping(method = RequestMethod.GET,value = "/price-rule/if-exist")
    public ResponseResult<Boolean> ifExists(@RequestParam String cityCode,@RequestParam String vehicleType);

    @RequestMapping(method = RequestMethod.POST,value = "/calculate-price")
    public ResponseResult<Double> calculatePrice(@RequestParam Integer distance,@RequestParam Integer duration,@RequestParam String cityCode,@RequestParam String vehicleType);
}
