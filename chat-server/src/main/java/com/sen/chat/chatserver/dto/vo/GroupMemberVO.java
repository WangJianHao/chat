package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 群组成员展示信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/29 16:00
 */
@Data
public class GroupMemberVO {

    /**
     * 用户UID
     */
    @ApiModelProperty(value = "用户UID")
    private String uid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 性别0-未知 1-男性 2-女性
     */
    @ApiModelProperty(value = "性别0-未知 1-男性 2-女性")
    private Integer sex;

    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名")
    private String signature;

    /**
     * 地区编号
     */
    @ApiModelProperty(value = "地区编号")
    private String areaCode;

    /**
     * 地区
     */
    @ApiModelProperty(value = "地区")
    private String area;

}
