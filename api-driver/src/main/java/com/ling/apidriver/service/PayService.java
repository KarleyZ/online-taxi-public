package com.ling.apidriver.service;

import com.ling.apidriver.remote.ServiceSsePushClient;
import com.ling.internalcommon.constant.IdentityConstants;
import com.ling.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    @Autowired
    ServiceSsePushClient serviceSsePushClient;

    public ResponseResult pushPayInfo(Long orderId, Double price,Long passengerId){
        //封装消息
        JSONObject message = new JSONObject();
        message.put("price",price);
        //推送消息
        serviceSsePushClient.push(passengerId, IdentityConstants.PASSENGER_IDENTITY,message.toString());

        return ResponseResult.success();
    }
}
