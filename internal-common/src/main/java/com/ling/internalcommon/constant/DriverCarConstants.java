package com.ling.internalcommon.constant;

public class DriverCarConstants {
    /**
     * 司机车辆关系状态：绑定
     */
    public static Integer DRIVER_CAR_BIND = 1;
    /**
     * 司机车辆关系状态：解绑
     */
    public static Integer DRIVER_CAR_UNBIND = 2;

    /**
     * 司机状态有效
     */
    public static Integer DRIVER_STATE_VALID = 1;

    /**
     * 司机状态无效
     */
    public static Integer DRIVER_STATE_INVALID = 0;

    public static int DRIVER_EXISTS = 1;

    public static int DRIVER_NOT_EXISTS = 0;

    /**
     * 司机工作状态
     * 0收车，1出车，2暂停
     */
    public static int DRIVER_WORK_STATUS_START = 1;
    public static int DRIVER_WORK_STATUS_STOP = 0;
    public static int DRIVER_WORK_STATUS_SUSPEND = 2;
}
