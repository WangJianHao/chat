package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.dto.req.LoginReq;
import com.sen.chat.chatserver.dto.req.RegisterUserReq;
import com.sen.chat.chatserver.dto.vo.CaptureCodeVO;
import com.sen.chat.chatserver.service.AccountService;
import com.sen.chat.common.api.SenCommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 3:23
 */
@RestController
@Api(tags = "AccountController", description = "用户登录注册管理")
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "获取图片验证码")
    @GetMapping(value = "/getCaptureCode")
    @ResponseBody
    public SenCommonResponse<CaptureCodeVO> getCaptureCode() {
        CaptureCodeVO captureCodeVO = accountService.generateCaptureCode();
        return SenCommonResponse.success(captureCodeVO);
    }

    @ApiOperation("发送邮箱验证码")
    @PostMapping(value = "/sendMailAuthCode")
    @ResponseBody
    public SenCommonResponse<?> sendMailAuthCode(@RequestParam(name = "email") String email) {
        accountService.sendMailAuthCode(email);
        return SenCommonResponse.success();
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public SenCommonResponse<?> register(@Validated @RequestBody RegisterUserReq registerReq) {
        accountService.register(registerReq);
        return SenCommonResponse.success(null, "注册成功");
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public SenCommonResponse<?> login(@Validated @RequestBody LoginReq loginReq) {
        return accountService.login(loginReq);
    }

    @ApiOperation(value = "退出")
    @PostMapping(value = "/logout")
    @ResponseBody
    public SenCommonResponse<?> logout() {
        accountService.logout();
        return SenCommonResponse.success();
    }

    /**
     * 忘记密码执行的操作，用户未登录
     */
    @ApiOperation(value = "重置密码")
    @PostMapping(value = "/resetPassword")
    @ResponseBody
    public SenCommonResponse<?> resetPassword(HttpSession session) {

        return SenCommonResponse.success();
    }

    @ApiOperation(value = "token续期")
    @PostMapping(value = "/renewToken")
    @ResponseBody
    public SenCommonResponse<?> renewToken() {
        return accountService.renewToken();
    }

}
