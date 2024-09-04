package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 13:26
 */
public enum ApplyDealOptTypeEnum implements IEnum<Integer> {
    AGREED(1, "同意"),
    REJECTED(2, "拒绝"),
    BLACKED(3, "拉黑"),
    ;

    private final Integer code;

    private final String description;

    ApplyDealOptTypeEnum(Integer code, String description) {
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
