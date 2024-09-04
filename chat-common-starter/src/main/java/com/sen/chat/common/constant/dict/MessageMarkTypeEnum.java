package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 19:35
 */
public enum MessageMarkTypeEnum implements IEnum<Integer> {
    LIKE(1, "点赞", 10),
    DISLIKE(2, "点踩", 5),

    ;
    private final Integer code;

    private final String description;

    private final Integer riseNum;//需要多少个标记升级

    MessageMarkTypeEnum(Integer code, String description, Integer riseNum) {
        this.code = code;
        this.description = description;
        this.riseNum = riseNum;
    }

    private static Map<Integer, MessageMarkTypeEnum> cache;

    static {
        cache = Arrays.stream(MessageMarkTypeEnum.values()).collect(Collectors.toMap(MessageMarkTypeEnum::getCode, Function.identity()));
    }

    public static MessageMarkTypeEnum of(Integer type) {
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

    public Integer getRiseNum() {
        return riseNum;
    }


}
