package com.sen.chat.chatserver.entity.query;

import lombok.Builder;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/28 1:07
 */
@Builder
public class GroupMemberQuery {

    /**
     * 群组ID
     */
    private Long groupId;

    /**
     * 用户UID
     */
    private Long uid;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 角色列表
     */
    private List<Integer> roles;
}
