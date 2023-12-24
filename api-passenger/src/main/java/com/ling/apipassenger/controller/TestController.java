package com.ling.apipassenger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testApiPassengerApplicaiton")
    public String testApiPassengerApplicaiton(){

        return "ApiPassengerApplicaiton";
    }
}
