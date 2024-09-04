package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.req.DealWithApplyReq;
import com.sen.chat.chatserver.dto.req.FriendApplyReq;
import com.sen.chat.chatserver.dto.req.GroupApplyReq;
import com.sen.chat.chatserver.dto.resp.ApplyResp;
import com.sen.chat.common.api.BasePageReq;
import com.sen.chat.common.api.SenCommonPage;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 14:55
 */
public interface ApplyService {

    /**
     * 提交好友申请
     *
     * @param friendApplyReq 好友申请请求
     */
    void submitFriendApply(FriendApplyReq friendApplyReq);

    /**
     * 添加机器人好友
     *
     * @param uid 用户UID
     */
    void addDefaultFriend(Long uid);

    /**
     * 处理好友申请
     *
     * @param dealWithApplyReq 请求
     */
    void dealWithFriendApply(DealWithApplyReq dealWithApplyReq);

    /**
     * 查询申请列表
     *
     * @param request 请求
     * @return
     */
    SenCommonPage<ApplyResp> queryApplyWithPage(BasePageReq request);

    /**
     * 提交入群申请
     *
     * @param groupApplyReq 入群申请
     */
    void submitGroupApply(GroupApplyReq groupApplyReq);

    /**
     * 处理入群申请
     *
     * @param dealWithApplyReq
     */
    void dealWithGroupApply(DealWithApplyReq dealWithApplyReq);
}
