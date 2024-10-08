package com.ling.servicessepush.controller;

import com.ling.internalcommon.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SseController {

    public static Map<String,SseEmitter> sseEmitterMap = new HashMap<>();

    /**
     * 创建连接
     * @param userId 用户id
     * @param identity 身份类型
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId,@RequestParam String identity){
        log.info("用户id:" + userId +",身份类型:" + identity);
        SseEmitter sseEmitter = new SseEmitter(0L);//链接永不超时
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        sseEmitterMap.put(sseMapKey,sseEmitter);
        return sseEmitter;
    }

    /**
     * 发送消息
     * @param userId
     * @param identity
     * @param content 发送内容
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam Long userId,@RequestParam String identity,@RequestParam String content){
        log.info("开始推送消息了");
        log.info("用户id:" + userId +",身份类型:" + identity);
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        try {
            if(sseEmitterMap.containsKey(sseMapKey)){
                sseEmitterMap.get(sseMapKey).send(content);
            }else {
                return "此用户不存在，推送失败！";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "给用户" + userId + "发送了消息:" + content;
    }

    /**
     * 关闭连接
     * @param userId
     * @param identity
     * @return
     */
    @GetMapping("/close")
    public  String close(@RequestParam Long userId,@RequestParam String identity){
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        log.info("关闭连接:" + sseMapKey);
        if(sseEmitterMap.containsKey(sseMapKey)){
            sseEmitterMap.remove(sseMapKey);
        }
        return "close success!";
    }
}
