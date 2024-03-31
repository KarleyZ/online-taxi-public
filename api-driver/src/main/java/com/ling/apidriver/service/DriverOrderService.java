package com.ling.apidriver.service;

import com.ling.apidriver.remote.ServiceOrderClient;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverOrderService {

    @Autowired
    ServiceOrderClient orderClient;

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest){
        return orderClient.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    public ResponseResult arrivedDeparture(OrderRequest orderRequest){
        return  orderClient.arrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger( OrderRequest orderRequest){
        return orderClient.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客到达目的地，行程终止
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetOff(OrderRequest orderRequest){
        return orderClient.passengerGetOff(orderRequest);
    }



}
