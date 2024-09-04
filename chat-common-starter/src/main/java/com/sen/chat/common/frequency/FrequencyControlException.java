package com.sen.chat.common.frequency;

import com.sen.chat.common.constant.errorcode.CommonErrorEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 16:42
 */
public class FrequencyControlException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     *  错误码
     */
    protected Integer errorCode;

    /**
     *  错误信息
     */
    protected String errorMsg;

    public FrequencyControlException() {
        super();
    }

    public FrequencyControlException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public FrequencyControlException(CommonErrorEnum error) {
        super(error.getMessage());
        this.errorCode = error.getCode();
        this.errorMsg = error.getMessage();
    }
}
