package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.req.LoginReq;
import com.sen.chat.chatserver.dto.req.RegisterUserReq;
import com.sen.chat.chatserver.dto.vo.CaptureCodeVO;
import com.sen.chat.common.api.SenCommonResponse;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 3:24
 */
public interface AccountService {

    /**
     * 注册信息
     *
     * @param registerReq
     * @return
     */
    void register(RegisterUserReq registerReq);

    /**
     * 登录返回token
     *
     * @param loginReq
     * @return
     */
    SenCommonResponse<?> login(LoginReq loginReq);

    /**
     * 生成图片验证码
     *
     * @return 图片验证码
     */
    CaptureCodeVO generateCaptureCode();


    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     */
    void sendMailAuthCode(String email);

    /**
     * 退出
     */
    void logout();

    /**
     * TOKEN续期
     */
    SenCommonResponse<?> renewToken();

}
