package com.ling.internalcommon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassengerUser {

    //主键
    private Long id;
    //乘客注册时间
    private LocalDateTime gmtCreate;
    //乘客修改时间
    private LocalDateTime gmtModified;
    //乘客手机号
    private String passengerPhone;
    //乘客称谓
    private String passengerName;
    //乘客性别
    private byte passengerGender;
    //乘客状态：0有效；1失效
    private byte state;
}
