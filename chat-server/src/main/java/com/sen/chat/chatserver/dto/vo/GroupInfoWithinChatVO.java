package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/29 16:03
 */
@Data
public class GroupInfoWithinChatVO {

    @ApiModelProperty("群组成员信息")
    private GroupInfoVO groupInfoVO;

    @ApiModelProperty("群组成员信息")
    private List<GroupMemberVO> groupMemberList;
}
