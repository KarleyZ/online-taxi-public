package com.ling.internalcommon.response;

import lombok.Data;

@Data
public class MapTerminalResponse {

    private String tid;

    private Long carId;

    private Long longitude;

    private Long latitude;
}
