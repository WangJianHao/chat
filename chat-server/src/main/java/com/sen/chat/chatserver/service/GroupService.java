package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.vo.GroupInfoVO;
import com.sen.chat.chatserver.dto.vo.GroupInfoWithinChatVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 21:33
 */
public interface GroupService {

    /**
     * 创建群组信息
     *
     * @param groupName   群组名称
     * @param groupNotice 群组公告
     * @param joinType    通过类型
     * @param avatarFile  头像源文件
     */
    void createGroup(String groupName, String groupNotice, Integer joinType, MultipartFile avatarFile);

    /**
     * 修改群组信息
     *
     * @param groupId
     * @param groupName
     * @param groupNotice
     * @param joinType
     * @param avatarFile
     */
    void modifyGroup(Long groupId, String groupName, String groupNotice, Integer joinType, MultipartFile avatarFile);

    /**
     * 获取群组简单信息
     *
     * @param groupId
     * @return
     */
    GroupInfoVO getGroupInfo(Long groupId);

    /**
     * 获取群组详细信息
     *
     * @param groupId
     * @return
     */
    GroupInfoWithinChatVO getGroupInfoWithinChat(Long groupId);

    /**
     * 入群操作
     *
     * @param uid     要入群的用户UID
     * @param groupId 群组ID
     */
    void enterGroup(Long uid, Long groupId);

    /**
     * 退群操作
     *
     * @param groupId 群组ID
     */
    void exitGroup(Long groupId);
}
