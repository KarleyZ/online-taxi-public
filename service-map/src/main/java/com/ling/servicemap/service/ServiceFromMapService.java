package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicemap.remote.SeriveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFromMapService {

    @Autowired
    SeriveClient seriveClient;

    /**
     * 创建服务
     * @param name
     * @return
     */
    public ResponseResult add(String name){

        return seriveClient.add(name);
    }
}
