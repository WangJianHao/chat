package com.sen.chat.chatserver.dto.ws;

import lombok.Data;

/**
 * ws的基本返回信息体
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:42
 */
@Data
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息类型
     *
     * @see com.sen.chat.chatserver.constant.WSRespTypeEnum
     */
    private Integer type;

    /**
     * 内容
     */
    private T data;
}
