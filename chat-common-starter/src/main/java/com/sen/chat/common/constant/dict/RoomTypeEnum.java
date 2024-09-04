package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:48
 */
public enum RoomTypeEnum implements IEnum<Integer> {
    GROUP(1, "群聊"),
    FRIEND(2, "单聊");

    private final Integer code;

    private final String description;

    RoomTypeEnum(Integer code, String description) {
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
