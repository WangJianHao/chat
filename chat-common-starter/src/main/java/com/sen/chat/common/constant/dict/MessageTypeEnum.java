package com.sen.chat.common.constant.dict;

import com.sen.chat.common.constant.IEnum;

import java.util.Arrays;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 19:29
 */
public enum MessageTypeEnum implements IEnum<Integer> {
    TEXT(1, "正常消息"),
    RECALL(2, "撤回消息"),
    IMG(3, "图片"),
    FILE(4, "文件"),
    SOUND(5, "语音"),
    VIDEO(6, "视频"),
    EMOJI(7, "表情"),
    SYSTEM(8, "系统消息"),
    ;

    private final Integer code;

    private final String description;

    MessageTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MessageTypeEnum of(Integer msgType) {
        return Arrays.stream(MessageTypeEnum.values()).filter(messageTypeEnum -> messageTypeEnum.getCode().equals(msgType)).findFirst().orElse(null);
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
