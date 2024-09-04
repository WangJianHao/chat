package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 15:41
 */

public enum ApplyTypeEnum implements IEnum<Integer> {
    FRIEND_APPLY(0, "好友申请"),
    GROUP_APPLY(1, "群组申请"),
    ;

    private final Integer code;

    private final String description;

    ApplyTypeEnum(Integer code, String description) {
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
