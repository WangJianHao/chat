package com.sen.chat.chatserver.dto;

import com.sen.chat.chatserver.entity.UserInfoDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/26 17:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoDTO extends UserInfoDO {

    /**
     * 在线状态
     */
    private Integer activeStatus;

    /**
     * 登录IP信息
     */
    private String ipInfo;

    /**
     * 登录时间
     */
    private Timestamp lastOptTime;

    /**
     * 离线时间
     */
    private Timestamp lastOffTime;
}
