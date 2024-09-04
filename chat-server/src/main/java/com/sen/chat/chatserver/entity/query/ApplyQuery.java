package com.sen.chat.chatserver.entity.query;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 16:21
 */
@Builder
public class ApplyQuery {

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
     * 0-好友申请 1-群组申请
     */
    @TableField(value = "apply_type")
    private Integer applyType;

    /**
     * 0-待处理 1-已同意 2-已拒绝 3-已拉黑
     */
    @TableField(value = "status")
    private Integer status;

}
