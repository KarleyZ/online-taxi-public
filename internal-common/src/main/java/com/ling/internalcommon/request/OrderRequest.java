package com.ling.internalcommon.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {
    //乘客id
    private Long passengerId;
    //乘客电话
     private String passengerPhone;
    //区域标识
    private String address;
    //出发时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departTime;
    //下订单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    //出发地名（描述）
    private String departure;
    //出发地经度
    private String depLongitude;
    //出发地纬度
    private String depLatitude;
    //目的地名（描述）
    private String destination;
    //目的地经度
    private String destLongitude;
    //目的地纬度
    private String destLatitude;
    /**
     * 坐标加密标识
     * 1：GCJ-02测绘局标准
     * 2：WGS84 GPS标准
     * 3：JBD-09 百度标准
     * 4：CGCS2000 北斗标准
     * 0：其他
     */
    private Integer encrypt;
    //计价类型
    private String fareType;
    //计价类型版本
    private Integer fareVersion;

    //请求设备唯一码
    private String deviceCode;

}
