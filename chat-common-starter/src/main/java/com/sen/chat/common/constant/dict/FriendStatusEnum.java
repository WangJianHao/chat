package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 12:39
 */
public enum FriendStatusEnum implements IEnum<Integer> {
    NOT_FRIEND(0, "非好友"),
    FRIEND(1, "好友"),
    DELETE_FRIEND(2, "已删除好友"),
    BE_DELETED(3, "被好友删除"),
    BLACK_FRIEND(4, "已拉黑好友"),
    BE_BLACKED(5, "被好友拉黑"),
    ;

    private final Integer code;

    private final String description;

    FriendStatusEnum(Integer code, String description) {
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
