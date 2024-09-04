package com.sen.chat.common.exception;

import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 12:11
 */
public class SenAsserts {
    public static void fail(String message) {
        throw new BusinessException(message);
    }

    public static void fail(BaseErrorCodeEnum errorCode) {
        throw new BusinessException(errorCode);
    }
}
