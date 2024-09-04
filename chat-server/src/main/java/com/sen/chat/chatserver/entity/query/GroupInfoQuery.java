package com.sen.chat.chatserver.entity.query;

import lombok.Builder;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:16
 */
@Builder
public class GroupInfoQuery {

    /**
     * 群主UID
     */
    private Long groupOwnerUid;

    /**
     * 群组状态
     */
    private Integer status;
}
