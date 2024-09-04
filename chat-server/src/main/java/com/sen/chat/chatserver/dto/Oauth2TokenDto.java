package com.sen.chat.chatserver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 18:35
 */
@Data
public class Oauth2TokenDto {

    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("刷令牌")
    private String refreshToken;

    @ApiModelProperty("访问令牌头前缀")
    private String tokenHead;

    @ApiModelProperty("有效时间（秒）")
    private int expiresIn;
}
