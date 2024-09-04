package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMessagePageReq extends CursorPageBaseReq {

    @NotNull
    @ApiModelProperty("会话id")
    private Long roomId;
}
