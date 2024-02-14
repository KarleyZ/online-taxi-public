package com.ling.apidriver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 此类并非业务中的部分，只是当司机验证码成功后会生成accessToken和refreshToken
 * 我们需要根据token来判断user是否有权限访问某些服务
 * 因此这里测试token是否正常
 */
@RestController
public class TokenTestController {

    /**
     * 需要带上token才能访问
     * @return
     */
    @GetMapping("/auth")
    public String testAuth(){
        return "auth";
    }

    /**
     * 不需要带上token，有无token都可以
     * @return
     */
    @GetMapping("/noauth")
    public String testNoAuth(){
        return "no auth";
    }
}
