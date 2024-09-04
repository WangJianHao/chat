package com.sen.chat.chatauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2获取Token返回信息封装
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 12:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
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
