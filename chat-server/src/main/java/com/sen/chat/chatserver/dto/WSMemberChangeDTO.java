package com.sen.chat.chatserver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 21:57
 */
@Data
public class WSMemberChangeDTO {

    public static final Integer CHANGE_TYPE_ADD = 1;
    public static final Integer CHANGE_TYPE_REMOVE = 2;

    @ApiModelProperty("群组id")
    private Long roomId;

    @ApiModelProperty("变动uid集合")
    private Long uid;

    @ApiModelProperty("变动类型 1加入群组 2移除群组")
    private Integer changeType;

    /**
     * @see com.sen.chat.common.constant.dict.UserActiveStatusEnum
     */
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;

    @ApiModelProperty("最后一次上下线时间")
    private Date lastOptTime;
}
