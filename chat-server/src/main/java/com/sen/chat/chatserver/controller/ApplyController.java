package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.dto.req.DealWithApplyReq;
import com.sen.chat.chatserver.dto.req.FriendApplyReq;
import com.sen.chat.chatserver.dto.req.GroupApplyReq;
import com.sen.chat.chatserver.dto.resp.ApplyResp;
import com.sen.chat.chatserver.service.ApplyService;
import com.sen.chat.common.api.BasePageReq;
import com.sen.chat.common.api.SenCommonPage;
import com.sen.chat.common.api.SenCommonResponse;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 提交申请控制器
 *
 * @description:
 * @author: sensen
 * @date: 2024/9/1 14:54
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/apply")
public class ApplyController {

    private ApplyService applyService;

    /**
     * 好友申请
     */
    @PostMapping("/friendApply")
    public SenCommonResponse<?> friendApply(@RequestBody FriendApplyReq friendApplyReq) {
        applyService.submitFriendApply(friendApplyReq);
        return SenCommonResponse.success();
    }

    /**
     * 处理好友申请
     */
    @ApiOperation("处理好友申请")
    @PostMapping("/dealWithFriendApply")
    public SenCommonResponse<?> dealWithFriendApply(@RequestBody DealWithApplyReq dealWithApplyReq) {
        applyService.dealWithFriendApply(dealWithApplyReq);
        return SenCommonResponse.success();
    }

    /**
     * 入群申请
     */
    @PostMapping("/groupApply")
    public SenCommonResponse<?> groupApply(@RequestBody GroupApplyReq groupApplyReq) {
        applyService.submitGroupApply(groupApplyReq);
        return SenCommonResponse.success();
    }

    /**
     * 处理入群申请
     */
    @ApiOperation("处理入群申请")
    @PostMapping("/dealWithGroupApply")
    public SenCommonResponse<?> dealWithGroupApply(@RequestBody DealWithApplyReq dealWithApplyReq) {
        applyService.dealWithGroupApply(dealWithApplyReq);
        return SenCommonResponse.success();
    }

    @ApiOperation("申请列表")
    @GetMapping("/queryApplyWithPage")
    public SenCommonResponse<SenCommonPage<ApplyResp>> queryApplyWithPage(@Valid @RequestBody BasePageReq request) {
        return SenCommonResponse.success(applyService.queryApplyWithPage(request));
    }


}
