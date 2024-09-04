package com.sen.chat.chatserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tchat_message_mark")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageMarkDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息表id
     */
    @TableField("msg_id")
    private Long msgId;

    /**
     * 标记人uid
     */
    @TableField("uid")
    private Long uid;

    /**
     * 标记类型 1点赞 2举报
     *
     * @see com.sen.chat.common.constant.dict.MessageMarkTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 消息状态 0正常 1取消
     */
    @TableField("status")
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
