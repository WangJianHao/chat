package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 17:22
 */
@Data
public class UserInfoVO {

    /**
     * 用户UID
     */
    @ApiModelProperty(value = "用户UID")
    private String uid;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 0-直接加好友 1-同意后加好友
     */
    @ApiModelProperty(value = "0-直接加好友 1-同意后加好友")
    private Integer joinType;

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

    /**
     * 使用状态 0.正常 1封禁
     */
    @ApiModelProperty(value = "使用状态 0.正常 1封禁")
    private Integer status;
}
