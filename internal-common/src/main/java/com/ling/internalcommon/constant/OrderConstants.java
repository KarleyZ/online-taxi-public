package com.ling.internalcommon.constant;

public class OrderConstants {
    /**
     * 订单状态
     * 0: 订单无效（没叫到司机）
     * 1：订单开始
     * 2：司机接单
     * 3：司机出发去接乘客
     * 4：司机到达出发点
     * 5：司机接到乘客，行程开始
     * 6：到达目的地，乘客下车未支付
     * 7：发起收款
     * 8：乘客支付完成
     * 9：订单取消
     */
    public static final int ORDER_INVALID = 0;
    public static final int ORDER_START = 1;
    public static final int RECEIVE_ORDER = 2;
    public static final int DRIVER_TO_PICK_UP_PASSENGER = 3;
    public static final int DRIVER_ARRIVED_DEPARTURE = 4;
    public static final int PICK_UP_PASSENGER = 5;
    public static final int PASSENGER_GET_OFF = 6;
    public static final int TO_START_PAY = 7;
    public static final int SUCCESS_PAY = 8;
    public static final int ORDER_CANCEL = 9;

    /**
     * 订单取消
     * 1:乘客提前取消
     * 2:司机提前取消
     * 3:平台取消
     * 4:乘客违约取消
     * 4:司机违约取消
     */
    public static final int CANACEL_PASSENGER_BEFORE = 1;
    public static final int CANACEL_DRIVER_BEFORE = 2;
    public static final int CANACEL_PLATFORM_BEFORE = 3;
    public static final int CANACEL_PASSENGER_ILLEGAL = 4;
    public static final int CANACEL_DRIVER_ILLEGAL = 5;
}
