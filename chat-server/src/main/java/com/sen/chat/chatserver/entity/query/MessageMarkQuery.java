package com.sen.chat.chatserver.entity.query;

import lombok.Builder;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 10:36
 */
@Builder
public class MessageMarkQuery {

    private List<Long> msgIds;

    private Integer status;
}
