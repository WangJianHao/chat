package com.sen.chat.chatserver.constant;

import com.sen.chat.common.constant.IEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 23:48
 */
public enum WSReqTypeEnum implements IEnum<Integer> {
    HEARTBEAT(1, "心跳包"),
    ;

    private final Integer code;
    private final String desc;

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getCode, Function.identity()));
    }

    WSReqTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
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
        return desc;
    }
}
