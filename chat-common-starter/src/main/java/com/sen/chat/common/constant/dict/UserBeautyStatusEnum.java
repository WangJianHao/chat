package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 14:20
 */
public enum UserBeautyStatusEnum implements IEnum<Integer> {

    USELESS(0, "未使用"),
    USED(1, "已使用");

    private final Integer code;

    private final String description;

    UserBeautyStatusEnum(Integer code, String description) {
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
