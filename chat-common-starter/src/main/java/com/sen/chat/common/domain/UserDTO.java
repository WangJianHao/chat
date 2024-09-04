package com.sen.chat.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 登录用户信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/25 19:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码 加密后的
     */
    private String password;

    /**
     * 状态 0-正常 1-封禁
     */
    private Integer status;

    private String clientId;
    private List<String> roles;

}
