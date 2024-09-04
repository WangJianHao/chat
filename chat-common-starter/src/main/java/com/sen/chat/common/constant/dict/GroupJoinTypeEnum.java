package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 21:59
 */
public enum GroupJoinTypeEnum implements IEnum<Integer> {
    WITHOUT_ADMIT(0, "直接加入"),
    WITH_ADMIT(1, "需要群主或者管理员同意后加入");

    private final Integer code;

    private final String description;

    GroupJoinTypeEnum(Integer code, String description) {
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
