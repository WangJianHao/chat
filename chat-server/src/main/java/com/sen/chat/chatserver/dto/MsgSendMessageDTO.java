package com.sen.chat.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 2:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgSendMessageDTO implements Serializable {

    /**
     * 消息ID
     */
    private Long msgId;
}
