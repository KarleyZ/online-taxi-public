package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServiceOrderClient;
import com.ling.internalcommon.constant.IdentityConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    ServiceOrderClient serviceOrderClient;

    /**
     * 乘客下订单
     * @param orderRequest
     * @return
     */
    public ResponseResult add(OrderRequest orderRequest){
        return serviceOrderClient.add(orderRequest);
    }

    /**
     * 乘客取消订单
     * @param orderId
     * @return
     */
    public ResponseResult cancel(Long orderId){

        return serviceOrderClient.cancel(orderId, IdentityConstants.PASSENGER_IDENTITY);
    }
}
