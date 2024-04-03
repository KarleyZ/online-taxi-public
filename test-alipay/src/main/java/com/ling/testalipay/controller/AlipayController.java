package com.ling.testalipay.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alipay")
@ResponseBody
public class AlipayController {

    @GetMapping("/pay")
    public String pay(@RequestParam String subject,@RequestParam String outTradeNo, @RequestParam String totalAmount){
        AlipayTradePagePayResponse response;
        try {
            response = Factory.Payment.Page().pay(subject,outTradeNo,totalAmount,"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response.getBody();

    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) throws Exception {

        String tradeStatus = request.getParameter("trade_status");
        if(tradeStatus.trim().equals("TRADE_SUCCESS")){
            Map<String,String> param = new HashMap<>();

            Map<String,String[]> parameterMap = request.getParameterMap();
            for (String name:parameterMap.keySet()) {
                param.put(name,request.getParameter(name));
            }
            if(Factory.Payment.Common().verifyNotify(param)){
                System.out.println("通过支付宝的验证！");
                for (String name: param.keySet()) {
                    System.out.println("收到并且接受好的参数，" + name + ","+ param.get(name));
                }
            }else {
                System.out.println("支付宝验证不通过！");
            }
        }
        return "success";
    }
}
