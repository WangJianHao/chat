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
 * @date 2024-09-01
 */

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
@TableName(value = "tusr_apply")
public class ApplyDO implements Serializable {

    private static final long serialVersionUID = 6253667344671118014L;

    /**
     * 自增ID
     */
    @TableId(value = "apply_id")
    private Long applyId;

    /**
     * 申请人UID
     */
    @TableField(value = "apply_uid")
    private Long applyUid;

    /**
     * 接收人UID
     */
    @TableField(value = "receive_uid")
    private Long receiveUid;

    /**
     * 最后申请时间
     */
    @TableField(value = "last_apply_time")
    private Timestamp lastApplyTime;

    /**
     * 0-待处理 1-已同意 2-已拒绝 3-已拉黑
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 申请信息
     */
    @TableField(value = "apply_info")
    private String applyInfo;

    /**
     * 0-好友申请 1-群组申请
     */
    @TableField(value = "apply_type")
    private Integer applyType;

    /**
     * 群组ID
     */
    @TableField(value = "group_id")
    private Long groupId;

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
