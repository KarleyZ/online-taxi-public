package com.ling.servicemap.service;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.servicemap.remote.TerminalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    @Autowired
    TerminalClient terminalClient;

    public ResponseResult<MapTerminalResponse> add(String name){

        return terminalClient.add(name);
    }
}
