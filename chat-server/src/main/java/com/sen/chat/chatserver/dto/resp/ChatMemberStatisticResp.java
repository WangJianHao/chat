package com.sen.chat.chatserver.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群成员统计信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 18:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberStatisticResp {

    @ApiModelProperty("在线人数")
    private Long onlineNum;//在线人数

    @ApiModelProperty("总人数")
    private Long totalNum;//总人数
}
