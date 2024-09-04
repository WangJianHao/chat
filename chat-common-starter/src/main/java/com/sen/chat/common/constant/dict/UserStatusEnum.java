package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 12:52
 */
public enum UserStatusEnum implements IEnum<Integer> {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private final Integer code;

    private final String description;

    UserStatusEnum(Integer code, String description) {
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
