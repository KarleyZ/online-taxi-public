package com.ling.servicedriveruser.service;

import com.ling.internalcommon.dto.Car;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.internalcommon.response.MapTrackResponse;
import com.ling.servicedriveruser.mapper.CarMapper;
import com.ling.servicedriveruser.remote.ServiceMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Autowired
    private CarMapper carMapper;

    @Autowired
    ServiceMapClient serviceMapClient;

    public ResponseResult addCar(Car car){

        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModified(now);
        //保存车辆
        carMapper.insert(car);
        //根据car的来创建有关这辆车的终端，并一起写入数据库。
        ResponseResult<MapTerminalResponse> responseResult = serviceMapClient.addTerminal(car.getVehicleNo(),car.getId().toString());
        String tid = responseResult.getData().getTid();

        car.setTid(tid);

        //获得此车的轨迹trid
        ResponseResult<MapTrackResponse> trackResponseResponseResult = serviceMapClient.addTrack(tid);
        String trid = trackResponseResponseResult.getData().getTrid();
        String trname = trackResponseResponseResult.getData().getTrname();
        car.setTrid(trid);
        car.setTrname(trname);
        //更新车辆信息
        carMapper.updateById(car);
        return ResponseResult.success();
    }

    public ResponseResult<Car> getCarById(Long carId){

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",carId);
        List<Car> cars = carMapper.selectByMap(queryMap);
        return ResponseResult.success(cars.get(0));
    }
}
