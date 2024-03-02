package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.PointRequest;
import com.ling.servicemap.remote.MapPointClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    MapPointClient mapPointClient;

    public ResponseResult upload(PointRequest pointRequest){

        return mapPointClient.upload(pointRequest);
    }
}
