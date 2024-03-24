package com.ling.servicemap.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.servicemap.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    TerminalService terminalService;

    /**
     * name采用车牌号
     * desc用来保存carId
     * @param name
     * @param desc
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<MapTerminalResponse> add(String name,String desc){

        return terminalService.add(name,desc);
    }

    @PostMapping("/aroundsearch")
    public ResponseResult<List<MapTerminalResponse>> aroundSearch(String center, Integer radius){
        return terminalService.aroundSearch(center,radius);
    }
}
