package com.sen.chat.chatserver.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 3:57
 */
@Data
public class CaptureCodeVO {

    @ApiModelProperty("图片验证码,Base64格式")
    private String captureCode;

    @ApiModelProperty("图片验证码Key，验证时需要传给后端")
    private String checkCodeKey;
}
