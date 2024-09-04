package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 14:58
 */
@Data
public class FriendApplyReq {

    @ApiModelProperty("接收人UID")
    @NotNull
    private Long receiveUid;

    @ApiModelProperty("申请消息")
    private String applyInfo;
}
