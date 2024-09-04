package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/29 22:34
 */
@Data
public class ContactSearchVO {

    @ApiModelProperty("类别,1-群组 2-账号")
    private Integer contactType;

    @ApiModelProperty("账号UID")
    private Integer uid;

    @ApiModelProperty("群组ID")
    private Integer groupId;

    @ApiModelProperty("会话ID")
    private Integer roomId;

    @ApiModelProperty("用户名")
    //todo 后面要添加备注功能
    private String userName;

    @ApiModelProperty("群组名")
    private String groupName;

    @ApiModelProperty("是否为联系人，true表示是好友或者在群中，false表示不是好友或者不是群")
    private Boolean isContact;
}
