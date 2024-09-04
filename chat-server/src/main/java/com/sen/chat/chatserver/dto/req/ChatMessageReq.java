package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:14
 */
@Data
public class ChatMessageReq {

    @NotNull
    @ApiModelProperty("房间id")
    private Long roomId;

    @ApiModelProperty("消息类型")
    @NotNull
    private Integer msgType;

    @ApiModelProperty("消息内容，类型不同传值不同")
    @NotNull
    private Object body;

}
