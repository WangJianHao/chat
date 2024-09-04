package com.sen.chat.chatserver.event;

import org.springframework.context.ApplicationEvent;

/**
 * 消息发送事件
 *
 * @description:
 * @author: sensen
 * @date: 2024/9/1 12:21
 */
public class MessageSendEvent extends ApplicationEvent {
    private Long msgId;

    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }

    public Long getMsgId() {
        return msgId;
    }
}
