package com.ling.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ling
 * @since 2024-02-04
 */
@Data
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 车辆所在区域行政代码
     */
    private String address;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 车牌颜色（蓝色1，黄色2，黑色3，白色4，绿色5，其他9）
     */
    private String plateColor;

    /**
     * 核定载客位
     */
    private Integer seats;

    /**
     * 车辆厂牌
     */
    private String brand;

    /**
     * 车辆型号
     */
    private String model;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 车辆所有人
     */
    private String ownerName;

    /**
     * 车身颜色
     */
    private String vehicleColor;

    /**
     * 发动机型号
     */
    private String enginedId;

    /**
     * 车辆VIN码
     */
    private String vin;

    /**
     * 车辆注册日期
     */
    private LocalDate certifyDateA;

    /**
     * 燃料类型（汽油1，柴油2，天然气3，液化气4，电动5，其他9）
     */
    private String fueType;

    /**
     * 发动机排量，单位毫升
     */
    private String engineDisplace;

    /**
     * 车辆运输证发证机构
     */
    private String transAgency;

    /**
     * 车辆经验区域
     */
    private String transArea;

    /**
     * 车辆运输证有效期起
     */
    private LocalDate transDateStart;

    /**
     * 车辆运输证有效期止
     */
    private LocalDate transDateEnd;

    /**
     * 车辆初次登记日期
     */
    private LocalDate certifyDateB;

    /**
     * 车辆检修状态（未检修0，已检修1，未知2）
     */
    private String fixState;

    /**
     * 车辆下次年检时间
     */
    private LocalDate nextFixDate;

    /**
     * 车辆年度审验状态（未年审0，年审合格1，年审不合格2）
     */
    private String checkState;

    /**
     * 发票打印设备序列号
     */
    private String feePrintId;

    /**
     * 卫星定位装置品牌
     */
    private String gpsBrand;

    /**
     * 卫星定位装置型号
     */
    private String gpsModel;

    /**
     * 卫星定位设备安装日期
     */
    private LocalDate gpsInstallDate;

    /**
     * 报备日期
     */
    private LocalDate registerDate;

    /**
     * 服务类型（网络预约出租汽车1，巡游出租汽车2，私人小客车合乘3）
     */
    private Integer commercialType;

    /**
     * 运价类型编码
     */
    private String fareType;

    /**
     * 状态（有效0，失效1）
     */
    private Integer state;

    /**
     * 车辆终端的tid号
     */
    private String tid;

    /**
     * 车辆轨迹的trid号
     */
    private String trid;

    /**
     * 车辆轨迹的名称
     */
    private String trname;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;
}
