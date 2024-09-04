package com.sen.chat.chatserver.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 17:23
 */

@Data
@ApiModel
public class RegisterUserReq {


    @ApiModelProperty(value = "邮箱", required = true)
    @NotEmpty(message = "邮箱不允许为空")
    @Length(min = 8, max = 50, message = "邮箱长度超出限制范围")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "用户名/昵称", required = true)
    @NotEmpty(message = "昵称不允许为空")
    @Length(min = 1, max = 20, message = "昵称长度超出限制范围")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不允许为空")
//    @Length(min = 8, max = 15, message = "密码长度不在范围内{min},{max}")
    private String password;

    @ApiModelProperty(value = "邮箱验证码", required = true)
    @NotEmpty(message = "邮箱验证码不允许为空")
    private String emailCode;

    @ApiModelProperty(value = "图片验证码", required = true)
    @NotEmpty(message = "图片验证码不允许为空")
    private String captureCode;

    @ApiModelProperty(value = "图片验证码Key", required = true)
    @NotEmpty(message = "图片验证码Key不允许为空")
    private String captureCodeKey;

}
