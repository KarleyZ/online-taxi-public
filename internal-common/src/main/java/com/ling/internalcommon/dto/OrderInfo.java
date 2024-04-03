package com.ling.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ling
 * @since 2024-03-09
 */
@Data
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long id;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客手机号
     */
    private String passengerPhone;

    /**
     * 司机id
     */
    private Long driverId;

    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 接单的车辆id
     */
    private Long carId;

    /**
     * 发单地的区域代码
     */
    private String address;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 订单发起时间
     */
    private LocalDateTime orderTime;

    /**
     * 预计出发时间
     */
    private LocalDateTime departTime;

    /**
     * 出发地详细地址
     */
    private String departure;

    /**
     * 出发地经度
     */
    private String depLongitude;

    /**
     * 出发地纬度
     */
    private String depLatitude;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 目的地经度
     */
    private String destLongitude;

    /**
     * 目的地纬度
     */
    private String destLatitude;

    /**
     * 坐标加密标识 	1：GCJ-02测绘局标准	2：WGS84 GPS标准	3：JBD-09 百度标准	4：CGCS2000 北斗标准	0：其他
     */
    private Integer encrypt;

    /**
     * 运价类型版本
     */
    private Integer fareVersion;
    /**
     * 运价类型编码
     */
    private String fareType;

    /**
     * 接单时车辆经度
     */
    private String receiveOrderCarLongitude;

    /**
     * 接单时车辆纬度
     */
    private String receiveOrderCarLatitude;

    /**
     * 接单时间（派单时间）
     */
    private LocalDateTime receiveOrderTime;

    /**
     * 司机驾驶证号
     */
    private String licenseId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 司机去接乘客出发时间
     */
    private LocalDateTime toPickUpPassengerTime;

    /**
     * 司机去接乘客时的经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 司机去接乘客时的纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 去接乘客时，司机的地点
     */
    private String toPickUpPassengerAddress;

    /**
     * 司机到达上车点的时间
     */
    private LocalDateTime driverArrivedDepartureTime;

    /**
     * 乘客上车时间
     */
    private LocalDateTime pickUpPassengerTime;

    /**
     * 乘客上车时的经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 乘客上车的纬度
     */
    private String pickUpPassengerLatitude;

    /**
     * 乘客下车时间
     */
    private LocalDateTime passengerGetoffTime;

    /**
     * 乘客下车时的经度
     */
    private String passengerGetoffLongitude;

    /**
     * 乘客下车时的纬度
     */
    private String passengerGetoffLatitude;

    /**
     * 订单撤销时间
     */
    private LocalDateTime cancelTime;

    /**
     * 订单取消申请者：1乘客，2驾驶员，3平台
     */
    private Integer cancelOperator;

    /**
     * 撤销类型：1：乘客提前撤销 2：驾驶员提前撤销 3：平台公司撤销 4：乘客违约撤销 5：驾驶员违约撤销
     */
    private Integer cancelTypeCode;

    /**
     * 载客里程（米）
     */
    private Long driveMile;

    /**
     * 载客时长（分）
     */
    private Long driveTime;

    /**
     * 订单状态：1：订单开始 2：司机接单 3：去接乘客 4：司机到的乘客起点 5：乘客上车，司机开始行程 6：到达目的地，行程结束 7：发起收款 8：支付完成 9：订单取消
     */
    private Integer orderStatus;

    /**
     * 订单的实际价格
     */
    private Double price;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
