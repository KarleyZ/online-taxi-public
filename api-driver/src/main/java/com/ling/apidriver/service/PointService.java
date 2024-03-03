package com.ling.apidriver.service;

import com.ling.apidriver.remote.ServiceDriverUserClient;
import com.ling.apidriver.remote.ServiceMapClient;
import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.DriverPointRequest;
import com.ling.internalcommon.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    ServiceMapClient serviceMapClient;

    public ResponseResult upload(DriverPointRequest driverPointRequest){

        //获取carId,并以此获取tid和trid -service-driver-user接口
        Long carId = driverPointRequest.getCarId();
        Car car = serviceDriverUserClient.getCarById(carId).getData();
        String tid = car.getTid();
        String trid = car.getTrid();

        //调用轨迹上传的服务 -service-map接口
        PointRequest pointRequest = new PointRequest();
        pointRequest.setTid(tid);
        pointRequest.setTrid(trid);
        pointRequest.setPoints(driverPointRequest.getPoints());

        return serviceMapClient.upload(pointRequest);
    }
}
