package com.ling.internalcommon.constant;

import lombok.Data;
import lombok.Getter;

public enum CommonStatusEnum {

    /**
     * 请求返回成功
     */
    SUCCESS(1,"success"),
    /**
     * 请求返回失败
     */
    FAIL(0,"fail"),
    /**
     * 验证码错误提示：1000-1099，1099是最后的情况，兜底提示验证码出问题
     */
    VERIFICATION_CODE_ERROR(1099,"VerificationCode is error"),
    /**
     * 验证码错误提示：验证码未过期但是不正确
     */
    VERIFICATION_CODE_INCORRECT(1098,"VerficationCode is incorrected"),

    /**
     * token检查时出错。
     */
    TOKEN_ERROR(1199,"token error"),

    /**
     * 用户不存在
     */
    UESR_NOT_EXISTS(1200,"user is not exists"),

    /**
     * 计价规则不存在
     */
    PRICE_RULE_EMPTY(1300,"计价规则不存在"),

    /**
     * 请求高德行政区域API错误
     */
    MAP_DISTRICT_ERROR(1400,"请求高德行政区域API错误"),
    /**
     * 司机和车辆关系不存在错误
     */
    DRIVER_CAR_BIND_NOT_EXISTS(1500,"司机和车辆关系不存在"),
    /**
     * 司机和车辆绑定关系已存在,重复绑定错误
     */
    DRIVER_CAR_BIND_EXISTS(1502,"司机和车辆绑定关系已存在，请勿重复绑定"),
    /**
     * 司机已绑定车辆，请勿重复绑定
     */
    DRIVER_BIND_EXISTS(1503,"司机已绑定车辆，请勿重复绑定"),
    /**
     * 司机已绑定车辆，请勿重复绑定
     */
    CAR_BIND_EXISTS(1504,"车辆已绑定司机，请勿重复绑定"),
    /**
     * 司机已绑定车辆，请勿重复绑定
     */
    DRIVER_BIND_NOT_EXISTS(1505,"该司机与车辆的绑定关系不存在"),
    /**
     * 司机不存在
     */
    DRIVER_NOT_EXISTS(1501,"司机不存在")
    ;

    @Getter
    private int code;
    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
