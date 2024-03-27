package com.ling.internalcommon.response;

import lombok.Data;

@Data
public class MapTerminalResponse {

    private String tid;

    private Long carId;

    private String longitude;

    private String latitude;
}
