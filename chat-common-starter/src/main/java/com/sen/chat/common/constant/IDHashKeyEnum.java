package com.sen.chat.common.constant;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 13:53
 */
public enum IDHashKeyEnum {

    USER_UID("user-uid", 100001L, "用户UID"),
    GROUP_ID("group-id", 0L, "群组ID"),
    ROOM_ID("room-id", 0L, "会话ID"),
    APPLY_ID("apply-id", 0L, "申请ID"),
    ;


    private final String hashKey;

    private final Long bound;

    private final String desc;


    IDHashKeyEnum(String hashKey, Long bound, String desc) {
        this.hashKey = hashKey;
        this.bound = bound;
        this.desc = desc;
    }

    public String getHashKey() {
        return hashKey;
    }

    public Long getBound() {
        return bound;
    }

    public String getDesc() {
        return desc;
    }
}
