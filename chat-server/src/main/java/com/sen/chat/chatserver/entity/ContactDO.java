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
 * 会话列表
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
@TableName(value = "tbas_contact")
public class ContactDO implements Serializable {
    private static final long serialVersionUID = 239351892650542708L;

    /**
     * 用户ID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 房间ID
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 联系人类型
     *
     * @see com.sen.chat.common.constant.dict.ContactTypeEnum
     */
    @TableField(value = "contact_type")
    private Integer contactType;

    /**
     * 联系人ID
     */
    @TableField(value = "contact_id")
    private Long contactId;


    /**
     * 会话最新消息id
     */
    @TableField(value = "last_msg_id")
    private Long lastMsgId;

    /**
     * 会话消息最后更新时间
     */
    @TableField(value = "active_time")
    private Timestamp activeTime;

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
