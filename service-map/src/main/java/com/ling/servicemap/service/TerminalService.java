package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.servicemap.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {

    @Autowired
    TerminalClient terminalClient;

    public ResponseResult<MapTerminalResponse> add(String name,String desc){

        return terminalClient.add(name,desc);
    }

    public ResponseResult<List<MapTerminalResponse>> aroundSearch(String center, String radius){

        return terminalClient.aroundSearch(center,radius);
    }
}
