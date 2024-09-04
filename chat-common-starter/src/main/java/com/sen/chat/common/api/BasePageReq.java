package com.sen.chat.common.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 14:12
 */
@Data
public class BasePageReq {

    @ApiModelProperty("当前页码")
    @NotNull
    private Integer current = 1;

    @ApiModelProperty("每页数量")
    @NotNull
    private Integer size = 10;

}
