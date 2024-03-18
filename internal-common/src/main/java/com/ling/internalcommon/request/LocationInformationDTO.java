package com.ling.internalcommon.request;

import lombok.Data;

@Data
public class LocationInformationDTO {

    //出发地经度
    private String depLongitude;
    //出发地纬度
    private String depLatitude;
    //出发地经度
    private String destLongitude;
    //出发地纬度
    private String destLatitude;

    //城市编码
    private String cityCode;
    //车辆类型
    private String vehicleType;

}
