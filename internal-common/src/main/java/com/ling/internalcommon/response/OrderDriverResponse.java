package com.ling.internalcommon.response;

import lombok.Data;

@Data
public class OrderDriverResponse {

    private Long driverId;
    private String driverPhone;
    private String licenseId;
    private String vehicleNo;
    private Long carId;
    private String vehicleType;
}
