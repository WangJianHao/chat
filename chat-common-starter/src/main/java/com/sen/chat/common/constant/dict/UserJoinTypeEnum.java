package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 19:18
 */
public enum UserJoinTypeEnum implements IEnum<Integer> {
    WITHOUT_ADMIT(0, "直接通过好友验证"),
    WITH_ADMIT(1, "需要好友验证消息");

    private final Integer code;

    private final String description;

    UserJoinTypeEnum(Integer code, String description) {
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
