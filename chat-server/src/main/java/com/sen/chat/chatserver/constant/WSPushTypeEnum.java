package com.sen.chat.chatserver.constant;

import com.sen.chat.common.constant.IEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:44
 */
public enum WSPushTypeEnum implements IEnum<Integer> {

    USER(1, "个人"),
    ALL(2, "全部连接用户"),
    ;

    private final Integer code;
    private final String description;

    private static final Map<Integer, WSPushTypeEnum> cache;

    static {
        cache = Arrays.stream(WSPushTypeEnum.values()).collect(Collectors.toMap(WSPushTypeEnum::getCode, Function.identity()));
    }

    WSPushTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WSPushTypeEnum of(Integer type) {
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
        return description;
    }
}
