package com.ling.internalcommon.response;

import lombok.Data;

@Data
public class ForecastPriceResponse {

    //预估的打车价格
    private double price;

    private String cityCode;

    private String vehicleType;

    private String fareType;

    private Integer fareVersion;
}
