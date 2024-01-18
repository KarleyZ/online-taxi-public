package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.DirectionResponse;
import com.ling.servicemap.remote.MapDirectionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DirectionService {

    @Autowired
    MapDirectionClient mapDirectionClient;

    /**
     * 根据起点与终点的经纬度，计算距离和时长，单位：米和分钟
     * @param depLongitude
     * @param depLatitude
     * @param destLongitude
     * @param destLatitude
     * @return
     */
    public ResponseResult driving(String depLongitude, String depLatitude, String destLongitude, String destLatitude){

        DirectionResponse direction = mapDirectionClient.direction(depLongitude, depLatitude, destLongitude, destLatitude);

        return ResponseResult.success(direction);
    }
}
