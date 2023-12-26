package com.ling.serviceverificationcode.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {

    @GetMapping("/numberCode/{size}")
    public ResponseResult getNumberCode(@PathVariable int size){
        System.out.println("数字验证码长度："+ size);
        //生成验证码
        double mathRandom = (Math.random()*9 + 1) * Math.pow(10,size-1);
        int resultInt = (int)mathRandom;
        System.out.println("生成的验证码：" + resultInt);

        NumberCodeResponse response = new NumberCodeResponse();
        response.setNumberCode(resultInt);

        return ResponseResult.success(response);
        //旧的JSON体生成返回结果，弃之不用
        /*
        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");
        JSONObject data = new JSONObject();
        data.put("numberCode",resultInt);
        result.put("data",data);
        */

    }
}
