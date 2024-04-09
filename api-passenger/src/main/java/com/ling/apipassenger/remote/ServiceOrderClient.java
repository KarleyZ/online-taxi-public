package com.ling.apipassenger.remote;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-order")
public interface ServiceOrderClient {

    @RequestMapping(method = RequestMethod.POST,value = "/order/add")
    public ResponseResult add(OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/cancel")
    public ResponseResult cancel(@RequestParam Long orderId, @RequestParam String identity);

}
