package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/31 1:56
 */
public enum FlagEnum implements IEnum<Integer> {
    NO(0, "否"),
    YES(1, "是"),
    ;

    private final Integer code;

    private final String description;

    FlagEnum(Integer code, String description) {
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

    public static Integer toStatus(Boolean bool) {
        return bool ? YES.getCode() : NO.getCode();
    }
}
