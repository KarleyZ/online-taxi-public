package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTrackResponse;
import com.ling.servicemap.remote.TrackClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    TrackClient trackClient;

    public ResponseResult<MapTrackResponse> add(String tid){

        return trackClient.add(tid);
    }
}
