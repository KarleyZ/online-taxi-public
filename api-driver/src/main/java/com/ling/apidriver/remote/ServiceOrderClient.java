package com.ling.apidriver.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-order")
public interface ServiceOrderClient {

    @RequestMapping(method = RequestMethod.POST, value = "/order/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/passenger-get-off")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/cancel")
    public ResponseResult cancel(@RequestParam Long orderId, @RequestParam String identity);

}
