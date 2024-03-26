package com.ling.internalcommon.request;

import lombok.Data;

@Data
public class PriceRuleIsNewRequest {

    String fareType;

    Integer fareVersion;
}
