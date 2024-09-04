package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.chatserver.service.UserInfoService;
import com.sen.chat.common.api.SenCommonResponse;
import com.sen.chat.common.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/26 14:09
 */
@RestController
@Api(tags = "UserInfoController", description = "后台用户管理")
@RequestMapping("/user")
@Validated
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    /**
     * 根据普通用户登录用户名获取普通用户信息
     *
     * @param uid 登录用户名 本系统中为uid
     * @return 通用用户信息
     */
    @ApiOperation("根据普通用户登录用户名获取普通用户信息")
    @RequestMapping(value = "/loadUserByUid", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO loadByUid(@RequestParam Long uid) {
        return userInfoService.loadUserByUid(uid);
    }

    /**
     * 登录后查询用户信息
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public SenCommonResponse<?> getUserInfo() {
        UserInfoDO userInfoDO = userInfoService.getCurrentUser();
        return SenCommonResponse.success(userInfoDO);
    }
}
