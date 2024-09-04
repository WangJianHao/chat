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
 * @date 2024-09-01
 */

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
@TableName(value = "tusr_friend")
public class FriendDO implements Serializable {
    private static final long serialVersionUID = 8193164709547249687L;

    /**
     * 用户ID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 好友ID
     */
    @TableField(value = "friend_uid")
    private Long friendUid;

    /**
     * 会话ID
     */
    @TableField(value = "room_id")
    private Long roomId;

    /**
     * 0-非好友 1-好友 2-已删除好友 3-被好友删除 4-已拉黑好友 5-被好友拉黑
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
