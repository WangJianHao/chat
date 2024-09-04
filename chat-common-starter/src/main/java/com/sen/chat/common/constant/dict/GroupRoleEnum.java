package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:59
 */
public enum GroupRoleEnum implements IEnum<Integer> {
    OWNER(1, "群主"),
    ADMINISTRATOR(2, "管理员"),
    MATE(3, "普通成员"),
    ;

    private final Integer code;

    private final String description;

    GroupRoleEnum(Integer code, String description) {
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
