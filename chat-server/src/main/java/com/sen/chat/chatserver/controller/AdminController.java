package com.sen.chat.chatserver.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 12:49
 */
@RestController
@Api(tags = "AdminController", description = "用户登录注册管理")
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

}
