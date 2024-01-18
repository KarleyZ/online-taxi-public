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
    PRICE_RULE_EMPTY(1300,"计价规则不存在")
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
