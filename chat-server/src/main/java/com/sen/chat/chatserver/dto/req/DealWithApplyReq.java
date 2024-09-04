package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 13:20
 */
@Data
@ApiModel
public class DealWithApplyReq {

    @ApiModelProperty("申请ID")
    @NotNull
    private Long applyId;

    @ApiModelProperty("操作类型，1-同意，2-拒绝，3-拉黑")
    @NotNull
    private Integer optType;
}
