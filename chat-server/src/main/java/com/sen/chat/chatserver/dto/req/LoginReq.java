package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 17:24
 */
@Data
@ApiModel
public class LoginReq {


    @ApiModelProperty(value = "用户UID")
    private Long uid;


    @ApiModelProperty(value = "邮箱")
    private String email;


    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不允许为空")
    private String password;


}
