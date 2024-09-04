package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 15:14
 */
public enum ApplyStatusEnum implements IEnum<Integer> {
    WAIT_DEAL(0, "待处理"),
    AGREED(1, "已同意"),
    REJECTED(2, "已拒绝"),
    BLACKED(3, "已拉黑"),
    ;

    private final Integer code;

    private final String description;

    ApplyStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getCodeString() {
        return code.toString();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
