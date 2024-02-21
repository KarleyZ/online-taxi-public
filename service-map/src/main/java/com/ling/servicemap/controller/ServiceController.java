package com.ling.servicemap.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicemap.service.ServiceFromMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    ServiceFromMapService serviceFromMapService;
    /**
     * 创建服务
     * @param name
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(String name){

        return serviceFromMapService.add(name);
    }
}
