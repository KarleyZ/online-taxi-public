package com.ling.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author ling
 * @since 2024-03-12
 */
@Data
public class PriceRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 起步价
     */
    private Double startFare;

    /**
     * 起步距离
     */
    private Integer startMile;

    /**
     * 一公里多少钱
     */
    private Double unitPricePerMile;

    /**
     * 一分钟多少钱
     */
    private Double unitPricePerMinute;

    /**
     * 运价类型编码
     */
    private String fareType;

    /**
     * 运价类型版本，默认1，逐次递增
     */
    private Integer fareVersion;
}
