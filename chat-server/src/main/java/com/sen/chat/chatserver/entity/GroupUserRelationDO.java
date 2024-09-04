package com.sen.chat.chatserver.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "tbas_group_user_relation")
public class GroupUserRelationDO implements Serializable {
    private static final long serialVersionUID = 3111636457373792133L;

    /**
     * 群组ID
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 成员UID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 1群主 2管理员 3普通成员
     */
    @TableField(value = "role")
    private Integer role;

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
