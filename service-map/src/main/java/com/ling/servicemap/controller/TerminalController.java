package com.ling.servicemap.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicemap.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    TerminalService terminalService;

    @PostMapping("/add")
    public ResponseResult add(String name){

        return terminalService.add(name);
    }
}
