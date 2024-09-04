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
 * 聊天室表，无论是群组还是好友的会话都是在聊天室中进行
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
@TableName(value = "tbas_room")
public class RoomDO implements Serializable {
    private static final long serialVersionUID = 2552710199095978322L;

    /**
     * 房间ID
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 房间类型 1群聊 2单聊
     */
    @TableField(value = "room_type")
    private Integer roomType;

    /**
     * 群最后消息的更新时间
     */
    @TableField(value = "active_time")
    private Timestamp activeTime;

    /**
     * 会话中的最后一条消息id
     */
    @TableField(value = "last_msg_id")
    private Long lastMsgId;

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
