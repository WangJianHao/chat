package com.sen.chat.chatserver.constant;

import com.sen.chat.chatserver.dto.ws.WSOnlineOfflineNotify;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 18:00
 */
@AllArgsConstructor
@Getter
public enum WSRespTypeEnum {
    MESSAGE(4, "新消息", null),
    ONLINE_OFFLINE_NOTIFY(5, "上下线通知", WSOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
    BLACK(7, "拉黑用户", null),
    MARK(8, "消息标记", null),
    RECALL(9, "消息撤回", null),
    APPLY(10, "好友/入群申请", null),
    MEMBER_CHANGE(11, "成员变动", null),
    ;

    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WSRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WSRespTypeEnum.values()).collect(Collectors.toMap(WSRespTypeEnum::getType, Function.identity()));
    }

    public static WSRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
