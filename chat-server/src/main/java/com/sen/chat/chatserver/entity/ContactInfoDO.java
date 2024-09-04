package com.sen.chat.chatserver.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 13:59
 */
@Data
public class ContactInfoDO {

    @ApiModelProperty("用户UID")
    private Integer uid;

    @ApiModelProperty("类别,1-群组 2-账号")
    private Integer contactType;

    @ApiModelProperty("联系人ID")
    private Integer contactId;

    @ApiModelProperty("会话ID")
    private Integer roomId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("群组名")
    private String groupName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改事件
     */
    private Timestamp updateTime;

}
