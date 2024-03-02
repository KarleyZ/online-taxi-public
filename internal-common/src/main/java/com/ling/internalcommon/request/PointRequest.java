package com.ling.internalcommon.request;

import lombok.Data;

@Data
public class PointRequest {

    String tid;

    String trid;

    PointDTO[] points;
}
