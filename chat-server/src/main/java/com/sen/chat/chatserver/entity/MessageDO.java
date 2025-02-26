package com.sen.chat.chatserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.sen.chat.chatserver.entity.message.MessageExtra;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tchat_message", autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    @TableField("room_id")
    private Long roomId;

    /**
     * 消息发送者uid
     */
    @TableField("from_uid")
    private Long fromUid;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 回复的消息内容
     */
    @TableField("reply_msg_id")
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     *
     * @see com.sen.chat.common.constant.dict.MessageStatusEnum
     */
    @TableField("status")
    private Integer status;

    /**
     * 与回复消息的间隔条数
     */
    @TableField("gap_count")
    private Integer gapCount;

    /**
     * 消息类型 1正常文本 2.撤回消息
     *
     * @see com.sen.chat.common.constant.dict.MessageTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 消息扩展字段
     */
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra;

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
