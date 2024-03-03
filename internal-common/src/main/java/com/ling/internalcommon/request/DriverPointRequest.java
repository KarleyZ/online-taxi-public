package com.ling.internalcommon.request;

import lombok.Data;

@Data
public class DriverPointRequest {

    private  Long carId;

    private PointDTO[] points;
}
