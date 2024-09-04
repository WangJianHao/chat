package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/31 10:38
 */
public enum MessageMarkStatusEnum implements IEnum<Integer> {
    NORMAL(0, "正常"),
    CANCEL(1, "取消"),

    ;
    private final Integer code;

    private final String description;

    MessageMarkStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    private static Map<Integer, MessageMarkStatusEnum> cache;

    static {
        cache = Arrays.stream(MessageMarkStatusEnum.values()).collect(Collectors.toMap(MessageMarkStatusEnum::getCode, Function.identity()));
    }

    public static MessageMarkStatusEnum of(Integer code) {
        return cache.get(code);
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
