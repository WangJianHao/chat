package com.sen.chat.chatserver.constant;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 13:33
 */
public interface MQConstant {

    /**
     * 消息发送mq
     */
    String SEND_MSG_TOPIC = "chat_send_msg";
    String SEND_MSG_GROUP = "chat_send_msg_group";

    /**
     * push用户
     */
    String PUSH_TOPIC = "websocket_push";
    String PUSH_GROUP = "websocket_push_group";
}
