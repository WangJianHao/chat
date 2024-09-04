package com.sen.chat.chatserver.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 14:13
 */
@ApiModel
@Data
public class ApplyResp {

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "申请人UID")
    private Long applyUid;

    @ApiModelProperty(value = "最后申请时间")
    private Timestamp lastApplyTime;

    @ApiModelProperty(value = "0-待处理 1-已同意 2-已拒绝 3-已拉黑")
    private Integer status;

    @ApiModelProperty(value = "申请信息")
    private String applyInfo;

    @ApiModelProperty(value = "0-好友申请 1-群组申请")
    private Integer applyType;

    @ApiModelProperty(value = "群组ID")
    private Long groupId;

}
