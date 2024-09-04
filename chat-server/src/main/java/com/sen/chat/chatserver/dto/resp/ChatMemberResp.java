package com.sen.chat.chatserver.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 群成员列表的成员信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp {

    @ApiModelProperty("uid")
    private Long uid;

    /**
     * @see com.sen.chat.common.constant.dict.UserActiveStatusEnum
     */
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;

    /**
     * 角色类型
     * @see com.sen.chat.common.constant.dict.GroupRoleEnum
     */
    private Integer role;

    @ApiModelProperty("最后一次上下线时间")
    private Date lastOptTime;
}
