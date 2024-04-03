package com.ling.testalipay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @PostMapping("/test")
    public String TestPeatnutShell(){
        log.info("支付宝回调了");
        return "test 支付宝回调 success!";
    }
}
