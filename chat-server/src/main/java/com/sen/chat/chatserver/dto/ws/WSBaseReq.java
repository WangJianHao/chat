package com.sen.chat.chatserver.dto.ws;

import lombok.Data;

/**
 * @description: websocket前端请求体
 * @author: sensen
 * @date: 2023/6/31 23:50
 */
@Data
public class WSBaseReq {

    /**
     * 请求类型 1心跳检测
     *
     * @see com.sen.chat.chatserver.constant.WSReqTypeEnum
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}
