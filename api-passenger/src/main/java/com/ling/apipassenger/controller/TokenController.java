package com.ling.apipassenger.controller;

import com.ling.apipassenger.service.TokenService;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/token-refresh")
    public ResponseResult refreshToken(@RequestBody TokenResponse tokenResponse){
        String refreshTokenSrc = tokenResponse.getRefreshToken();
        System.out.println("请求中的 refreshToken:" + refreshTokenSrc);
        return tokenService.refreshToken(refreshTokenSrc);
    }
}
