package com.sen.chat.common.constant.errorcode;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 0:41
 */
public enum CommonErrorEnum {
    LOCK_LIMIT(-4, "获取锁失败"),
    FREQUENCY_LIMIT(-3, "请求太频繁了，请稍后再试"),
    CAPACITY_REFILL_ERROR(-2, "Capacity and refill rate must be positive"),
    ;
    private final Integer code;
    private final String message;

    CommonErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
