package com.ling.internalcommon.dto;

import lombok.Data;

@Data
public class PriceRule {

    //城市编码
    private String cityCode;
    //车辆类型
    private String vehicleType;
    //起步价
    private Double startFare;
    //起步公里
    private Double startMile;
    //超出基本公里后每公里单价
    private Double unitPricePerMile;
    //超出基本时间后每分钟单价
    private Double unitPricePerMinute;
}
