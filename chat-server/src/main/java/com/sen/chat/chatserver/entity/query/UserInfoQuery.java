package com.sen.chat.chatserver.entity.query;

import lombok.Builder;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 12:28
 */
@Builder
public class UserInfoQuery {

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 0-直接加好友 1-同意后加好友
     */
    private Integer joinType;

    /**
     * 性别0为未知 1-男性 2-女性
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 地区编号
     */
    private String areaCode;

    /**
     * 地区
     */
    private String area;

    /**
     * 使用状态 0.正常 1封禁
     */
    private Integer status;

    private List<Long> uidList;
}
