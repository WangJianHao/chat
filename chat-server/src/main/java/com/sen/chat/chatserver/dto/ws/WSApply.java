package com.sen.chat.chatserver.dto.ws;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 16:47
 */
@Data
public class WSApply {

    @ApiModelProperty("申请人")
    private Long applyUid;

    @ApiModelProperty("0-好友申请 1-群组申请")
    private Integer applyType;

    @ApiModelProperty("申请信息")
    private String applyInfo;

    @ApiModelProperty("群ID")
    private Long groupId;

    @ApiModelProperty("申请未读数")
    private Integer unreadCount;
}
