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
 * 群组表
 *
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
@TableName(value = "tbas_room_group")
public class RoomGroupDO implements Serializable {
    private static final long serialVersionUID = 2794075575564466385L;

    /**
     * 群组ID
     */
    @TableId(value = "group_id")
    private Long groupId;

    /**
     * 房间ID
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 群名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 0-正常,1-解散
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0-直接加入，1-管理员同意后加入
     */
    @TableField(value = "join_type")
    private Integer joinType;

    /**
     * 群公告
     */
    @TableField(value = "group_notice")
    private String groupNotice;

    /**
     * 群主uid
     */
    @TableField(value = "group_owner_uid")
    private Long groupOwnerUid;

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
