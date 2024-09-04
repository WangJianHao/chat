package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 19:42
 */
public enum UserActiveStatusEnum implements IEnum<Integer> {
    ONLINE(1,"在线状态"),

    OFFLINE(0,"离线状态");

    private final Integer code;

    private final String description;

    UserActiveStatusEnum(Integer code, String description) {
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
