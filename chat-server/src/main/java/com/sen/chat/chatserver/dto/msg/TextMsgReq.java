package com.sen.chat.chatserver.dto.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 0:34
 */
@Data
public class TextMsgReq {
    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长，服务器扛不住啊，兄dei")
    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("回复的消息id,如果没有别传就好")
    private Long replyMsgId;

    @ApiModelProperty("艾特的uid")
    @Size(max = 10, message = "一次别艾特这么多人")
    private List<Long> atUidList;
}
