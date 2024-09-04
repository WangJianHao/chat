package com.sen.chat.chatserver.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author sensen
 * @Description
 * @date 2024-08-27
 */

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
@TableName(value = "tusr_userinfo")
public class UserInfoDO implements Serializable {
    private static final long serialVersionUID = 2850505320215863390L;

    /**
     * 用户UID
     */
    @TableId(value = "uid")
    private Long uid;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户名称
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 0-直接加好友 1-同意后加好友
     */
    @TableField(value = "join_type")
    private Integer joinType;

    /**
     * 性别0为未知 1-男性 2-女性
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 个性签名
     */
    @TableField(value = "signature")
    private String signature;

    /**
     * 地区编号
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 地区
     */
    @TableField(value = "area")
    private String area;

    /**
     * 使用状态 0.正常 1封禁
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


}
