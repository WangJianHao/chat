package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/28 1:11
 */
@Data
public class GroupInfoVO {

    /**
     * 群组ID
     */
    @ApiModelProperty(value = "群组ID")
    private Long groupId;

    /**
     * 房间ID
     */
    @ApiModelProperty(value = "房间ID")
    private Long roomId;

    /**
     * 群名称
     */
    @ApiModelProperty(value = "群名称")
    private String groupName;

    /**
     * 0-正常,1-解散
     */
    @ApiModelProperty(value = "0-正常,1-解散")
    private Integer status;

    /**
     * 0-直接加入，1-管理员同意后加入
     */
    @ApiModelProperty(value = "0-直接加入，1-管理员同意后加入")
    private Integer joinType;

    /**
     * 群公告
     */
    @ApiModelProperty(value = "群公告")
    private String groupNotice;

    /**
     * 群主uid
     */
    @ApiModelProperty(value = "群主uid")
    private String groupOwnerUid;

    @ApiModelProperty(value = "群成员数量")
    private Integer memberCount;
}
