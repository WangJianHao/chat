package com.sen.chat.chatserver.dto.resp;

import com.sen.chat.chatserver.dto.vo.UploadFileResp;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 23:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatFileMessageResp {

    @ApiModelProperty("发送者信息")
    private ChatMessageResp.UserInfo fromUser;

    @ApiModelProperty("消息详情")
    private ChatMessageResp.Message message;

    @ApiModelProperty("文件上传详情")
    private UploadFileResp uploadFileResp;

}
