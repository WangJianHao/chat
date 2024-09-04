package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 15:29
 */
@Data
public class GroupApplyReq {

    @ApiModelProperty("群组ID")
    @NotNull
    private Long groupId;

    @ApiModelProperty("申请消息")
    private String applyInfo;
}
