package com.sen.chat.chatserver.service.impl;

import com.sen.chat.chatserver.config.ChatServerProperties;
import com.sen.chat.chatserver.convert.GroupInfoConverter;
import com.sen.chat.chatserver.convert.UserConverter;
import com.sen.chat.chatserver.dao.ContactDAO;
import com.sen.chat.chatserver.dao.GroupDAO;
import com.sen.chat.chatserver.dao.GroupMemberDAO;
import com.sen.chat.chatserver.dao.RoomDAO;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.dto.vo.GroupInfoVO;
import com.sen.chat.chatserver.dto.vo.GroupInfoWithinChatVO;
import com.sen.chat.chatserver.dto.vo.GroupMemberVO;
import com.sen.chat.chatserver.entity.*;
import com.sen.chat.chatserver.entity.query.GroupInfoQuery;
import com.sen.chat.chatserver.entity.query.GroupMemberQuery;
import com.sen.chat.chatserver.event.mq.PushService;
import com.sen.chat.chatserver.service.FileService;
import com.sen.chat.chatserver.service.GroupService;
import com.sen.chat.chatserver.service.UserInfoService;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.common.constant.Constant;
import com.sen.chat.common.constant.IDHashKeyEnum;
import com.sen.chat.common.constant.dict.ContactTypeEnum;
import com.sen.chat.common.constant.dict.GroupRoleEnum;
import com.sen.chat.common.constant.dict.GroupStatusEnum;
import com.sen.chat.common.constant.dict.RoomTypeEnum;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.service.CommonIDGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 21:33
 */
@Slf4j
@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserInfoService userInfoService;

    private final FileService fileService;

    private final ChatServerProperties chatServerProperties;

    private final CommonIDGenerator commonIDGenerator;

    private final RoomDAO roomDAO;

    private final GroupDAO groupDAO;

    private final GroupMemberDAO groupMemberDAO;

    private final ContactDAO contactDAO;

    private final PushService pushService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(String groupName, String groupNotice, Integer joinType, MultipartFile avatarFile) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();

        //校验joinType是否是字典值

        //查询当前用户创建了多少个群组
        GroupInfoQuery query = GroupInfoQuery.builder()
                .groupOwnerUid(currentUser.getUid())
                .status(GroupStatusEnum.NORMAL.getCode())
                .build();
        int count = groupDAO.selectCount(query);
        //限制每个人创建的群组数量
        //todo 限制数量应该维护在系统设置中
        if (count >= 5) {
            throw new BusinessException(BaseErrorCodeEnum.OUT_OF_MAX_GROUP_COUNT.getCode(), String.format("最多能创建%d个群聊", 5));
        }

        Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());

        //插入room表
        Long roomId = commonIDGenerator.nextId(IDHashKeyEnum.ROOM_ID.getHashKey());
        RoomDO roomDO = new RoomDO();
        roomDO.setRoomId(roomId);
        roomDO.setRoomType(RoomTypeEnum.GROUP.getCode());
        roomDAO.insert(roomDO);

        //插入group表
        Long groupId = commonIDGenerator.nextId(IDHashKeyEnum.GROUP_ID.getHashKey());
        RoomGroupDO roomGroupDO = new RoomGroupDO();
        roomGroupDO.setGroupId(groupId);
        roomGroupDO.setGroupName(groupName);
        roomGroupDO.setGroupNotice(groupNotice);
        roomGroupDO.setRoomId(roomId);
        roomGroupDO.setJoinType(joinType);
        roomGroupDO.setGroupOwnerUid(currentUser.getUid());
        roomGroupDO.setStatus(GroupStatusEnum.NORMAL.getCode());
        groupDAO.insert(roomGroupDO);

        //插入group member表
        GroupUserRelationDO groupMember = new GroupUserRelationDO();
        groupMember.setGroupId(groupId);
        groupMember.setUid(currentUser.getUid());
        groupMember.setRole(GroupRoleEnum.OWNER.getCode());
        groupMemberDAO.insert(groupMember);

        //插入contact表
        ContactDO contactDO = new ContactDO();
        contactDO.setUid(currentUser.getUid());
        contactDO.setRoomId(roomId);
        contactDAO.insert(contactDO);


        //todo 创建会话 发送消息

        //异步保存群组头像，生成头像缩略图
        CompletableFuture.runAsync(() -> createCover(roomGroupDO, avatarFile));
    }

    @Override
    public void modifyGroup(Long groupId, String groupName, String groupNotice, Integer joinType, MultipartFile avatarFile) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(groupId);
        if (Objects.isNull(roomGroupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_NOT_EXISTS);
        }
        if (!Objects.equals(currentUser.getUid(), roomGroupDO.getGroupOwnerUid())) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }

        //更新表信息
        roomGroupDO.setGroupName(groupName);
        if (StringUtils.isNotEmpty(groupNotice)) {
            roomGroupDO.setGroupNotice(groupNotice);
        }
        roomGroupDO.setJoinType(joinType);
        groupDAO.updateById(roomGroupDO);


        //异步保存群组头像，生成头像缩略图
        if (Objects.nonNull(avatarFile)) {
            CompletableFuture.runAsync(() -> createCover(roomGroupDO, avatarFile));
        }

        //todo 修改群名称发送ws消息 消息推送
    }

    @Override
    public GroupInfoVO getGroupInfo(Long groupId) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(groupId);
        //todo 群聊解散处理
        if (Objects.isNull(roomGroupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_NOT_EXISTS);
        }
        if (GroupStatusEnum.DELETE.getCode().equals(roomGroupDO.getStatus())) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_IS_DELETE);
        }
        ContactDO contactDO = contactDAO.selectByPrimaryKey(currentUser.getUid(), roomGroupDO.getRoomId());
        //todo 被踢出群处理
        if (Objects.isNull(contactDO)) {
            throw new BusinessException(BaseErrorCodeEnum.NO_AUTH_GROUP);
        }

        //todo 查成员个数
        GroupMemberQuery query = GroupMemberQuery.builder().groupId(groupId).build();
        int memberCount = groupMemberDAO.selectCount(query);

        GroupInfoVO groupInfoVO = GroupInfoConverter.INSTANCE.convert(roomGroupDO);
        groupInfoVO.setMemberCount(memberCount);
        return groupInfoVO;
    }

    @Override
    public GroupInfoWithinChatVO getGroupInfoWithinChat(Long groupId) {
        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(groupId);
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        //todo 群聊解散处理
        if (Objects.isNull(roomGroupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_NOT_EXISTS);
        }
        if (GroupStatusEnum.DELETE.getCode().equals(roomGroupDO.getStatus())) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_IS_DELETE);
        }
        ContactDO contactDO = contactDAO.selectByPrimaryKey(currentUser.getUid(), roomGroupDO.getRoomId());
        //todo 被踢出群处理
        if (Objects.isNull(contactDO)) {
            throw new BusinessException(BaseErrorCodeEnum.NO_AUTH_GROUP);
        }
        List<GroupMemberDO> groupMemberDOList = groupMemberDAO.queryMemberList(groupId);

        GroupInfoWithinChatVO vo = new GroupInfoWithinChatVO();
        vo.setGroupInfoVO(GroupInfoConverter.INSTANCE.convert(roomGroupDO));
        List<GroupMemberVO> groupMemberVOList = groupMemberDOList.stream().map(UserConverter.INSTANCE::convert).collect(Collectors.toList());
        vo.setGroupMemberList(groupMemberVOList);
        return vo;
    }

    @Override
    public void enterGroup(Long uid, Long groupId) {
        ContactDO groupContact = contactDAO.selectByContactId(uid, groupId, ContactTypeEnum.GROUP.getCode());
        if (Objects.nonNull(groupContact)) {
            throw new BusinessException(BaseErrorCodeEnum.ALREADY_IN_GROUP);
        }

        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(groupId);
        if (Objects.isNull(roomGroupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_NOT_EXISTS);
        }
        Long roomId = roomGroupDO.getRoomId();

        //入群操作
        GroupUserRelationDO groupUserRelationDO = new GroupUserRelationDO();
        groupUserRelationDO.setGroupId(groupId);
        groupUserRelationDO.setUid(uid);
        groupUserRelationDO.setRole(GroupRoleEnum.MATE.getCode());
        groupMemberDAO.insert(groupUserRelationDO);

        //保存到联系表
        ContactDO contactDO = new ContactDO();
        contactDO.setUid(uid);
        contactDO.setRoomId(roomId);
        contactDO.setContactType(ContactTypeEnum.GROUP.getCode());
        contactDO.setContactId(groupId);
        contactDAO.insert(contactDO);

        //发布群员入群事件
        GroupMemberQuery query = GroupMemberQuery.builder().groupId(groupId).build();
        List<Long> receiveUidList = groupMemberDAO.selectList(query).stream().map(GroupUserRelationDO::getUid).collect(Collectors.toList());
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoAll(uid);
        pushService.sendPushMsg(WSAdapter.buildMemberAddWS(roomId, userInfoDTO), receiveUidList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exitGroup(Long groupId) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(groupId);
        if (Objects.isNull(roomGroupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }

        GroupUserRelationDO groupUserRelationDO = groupMemberDAO.selectByPrimaryKey(groupId, currentUser.getUid());
        if (Objects.isNull(groupUserRelationDO)) {
            throw new BusinessException(BaseErrorCodeEnum.BE_GROUP_REMOVED);
        }

        if (Objects.equals(roomGroupDO.getGroupOwnerUid(), currentUser.getUid())) {
            //群主  解散群操作
            //todo 解散群
        } else {
            //群员
            int count = contactDAO.deleteByRoomIdAndUid(roomGroupDO.getRoomId(), currentUser.getUid());
            if (count < 1) {
                throw new BusinessException("退出失败");
            }
            count = groupMemberDAO.deleteByPrimaryKey(groupId, currentUser.getUid());
            if (count < 1) {
                throw new BusinessException("退出失败");
            }
            GroupMemberQuery query = GroupMemberQuery.builder().groupId(groupId).build();
            List<GroupUserRelationDO> groupMemberDOS = groupMemberDAO.selectList(query);
            List<Long> receiveIdList = groupMemberDOS.stream().map(GroupUserRelationDO::getUid).collect(Collectors.toList());
            pushService.sendPushMsg(WSAdapter.buildMemberRemoveWS(roomGroupDO.getRoomId(), currentUser.getUid()), receiveIdList);
        }

    }

    private void createCover(RoomGroupDO groupDO, MultipartFile avatarFile) {
        if (Objects.isNull(avatarFile)) {
            return;
        }
        String baseFolderPath = chatServerProperties.getProjectFolder() + Constant.GROUP_AVATAR_PATH;
        File targetFolder = new File(baseFolderPath);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }
        String BaseAvatarPath = targetFolder.getPath() + File.separator + groupDO.getGroupId();
        String filePath = BaseAvatarPath + Constant.PNG_SUFFIX;
        try {
            File avatar = new File(filePath);
            avatarFile.transferTo(avatar);
            File cover = new File(BaseAvatarPath + Constant.COVER_SUFFIX + Constant.PNG_SUFFIX);
            fileService.createCover4Img(avatar, 150, cover);
        } catch (IOException e) {
            log.error("群头像保存失败", e);
        }
    }
}
