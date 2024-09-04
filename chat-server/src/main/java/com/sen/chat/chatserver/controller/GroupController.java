package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.service.GroupService;
import com.sen.chat.chatserver.dto.vo.GroupInfoVO;
import com.sen.chat.chatserver.dto.vo.GroupInfoWithinChatVO;
import com.sen.chat.common.api.SenCommonResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 21:21
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {


    private final GroupService groupService;

    @PostMapping("/createGroup")
    public SenCommonResponse<?> createGroup(@RequestParam("groupName") @NotEmpty String groupName,
                                            @RequestParam(value = "groupNotice", required = false) String groupNotice,
                                            @RequestParam("joinType") @NotNull Integer joinType,
                                            @RequestParam("avatarFile") MultipartFile avatarFile) {
        groupService.createGroup(groupName, groupNotice, joinType, avatarFile);
        return SenCommonResponse.success();
    }

    @PostMapping("/modifyGroup")
    public SenCommonResponse<?> modifyGroup(@RequestParam("groupId") @NotNull Long groupId,
                                            @RequestParam("groupName") @NotEmpty String groupName,
                                            @RequestParam(value = "groupNotice", required = false) String groupNotice,
                                            @RequestParam("joinType") @NotNull Integer joinType,
                                            @RequestParam("avatarFile") MultipartFile avatarFile) {
        groupService.modifyGroup(groupId, groupName, groupNotice, joinType, avatarFile);
        return SenCommonResponse.success();
    }

    @GetMapping("/getGroupInfo")
    public SenCommonResponse<GroupInfoVO> getGroupInfo(@RequestParam("groupId") @NotNull Long groupId) {
        return SenCommonResponse.success(groupService.getGroupInfo(groupId));
    }


    @GetMapping("/getGroupInfoWithinChat")
    public SenCommonResponse<GroupInfoWithinChatVO> getGroupInfoWithinChat(@RequestParam("groupId") @NotNull Long groupId) {
        return SenCommonResponse.success(groupService.getGroupInfoWithinChat(groupId));
    }

    @PostMapping("/exitGroup")
    public SenCommonResponse<GroupInfoVO> exitGroup(@RequestParam("groupId") @NotNull Long groupId) {
        groupService.exitGroup(groupId);
        return SenCommonResponse.success();
    }
}
