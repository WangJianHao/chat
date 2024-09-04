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
@TableName(value = "tusr_logininfo")
public class UserLoginInfoDO implements Serializable {
    private static final long serialVersionUID = 4693886516078285020L;

    /**
     * 用户UID
     */
    @TableId(value = "uid")
    private Long uid;

    /**
     * 在线状态 1在线 2离线
     */
    @TableField(value = "active_status")
    private Integer activeStatus;

    /**
     * 登录IP
     */
    @TableField(value = "ip_info")
    private String ipInfo;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_opt_time")
    private Timestamp lastOptTime;

    /**
     * 上次离线时间
     */
    @TableField(value = "last_off_time")
    private Timestamp lastOffTime;

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
