package com.sen.chat.chatserver.service.impl;

import com.sen.chat.chatserver.convert.GroupInfoConverter;
import com.sen.chat.chatserver.convert.UserConverter;
import com.sen.chat.chatserver.dao.*;
import com.sen.chat.chatserver.dto.vo.ContactSearchVO;
import com.sen.chat.chatserver.entity.*;
import com.sen.chat.chatserver.service.ContactService;
import com.sen.chat.chatserver.service.UserInfoService;
import com.sen.chat.common.constant.IDHashKeyEnum;
import com.sen.chat.common.constant.dict.ContactTypeEnum;
import com.sen.chat.common.constant.dict.FriendStatusEnum;
import com.sen.chat.common.constant.dict.RoomTypeEnum;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.service.CommonIDGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/29 21:59
 */
@Slf4j
@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final UserInfoService userInfoService;

    private final ContactDAO contactDAO;

    private final UserInfoDAO userInfoDAO;

    private final FriendDAO friendDAO;

    private final GroupDAO groupDAO;

    private final RoomDAO roomDAO;

    private final CommonIDGenerator commonIDGenerator;

    @Override
    public List<ContactSearchVO> searchContact(String searchText) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();

        //查询联系人信息
        List<ContactInfoDO> contactInfoDOS = contactDAO.queryContactInfo(currentUser.getUid(), searchText);
        List<ContactSearchVO> groupContactList = contactInfoDOS.stream().map(contactInfoDO -> {
            ContactSearchVO groupContact = new ContactSearchVO();
            groupContact.setRoomId(contactInfoDO.getRoomId());
            groupContact.setContactType(ContactTypeEnum.GROUP.getCode());
            groupContact.setContactType(contactInfoDO.getContactType());
            if (Objects.equals(ContactTypeEnum.GROUP.getCode(), contactInfoDO.getContactType())) {
                //群组
                groupContact.setGroupId(contactInfoDO.getContactId());
                groupContact.setGroupName(contactInfoDO.getGroupName());
            } else {
                //好友
                groupContact.setUid(contactInfoDO.getContactId());
                groupContact.setUserName(contactInfoDO.getUserName());
            }
            return groupContact;
        }).collect(Collectors.toList());

        return groupContactList;
    }

    @Override
    public List<ContactSearchVO> search(Long id) {
        List<ContactSearchVO> contactSearchVOList = new ArrayList<>();

        //1.搜索账号
        //检查是否是自己
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        if (Objects.equals(currentUser.getUid(), id)) {
            //自己
            ContactSearchVO contactSearchVO = UserConverter.INSTANCE.convert(currentUser);
            contactSearchVO.setContactType(ContactTypeEnum.ACCOUNT.getCode());
            contactSearchVO.setIsContact(true);
            contactSearchVOList.add(contactSearchVO);
        }
        //查询用户表
        UserInfoDO userInfoDO = userInfoDAO.selectByUidWithLoginInfo(id);
        if (Objects.nonNull(userInfoDO)) {
            ContactSearchVO contactSearchVO = UserConverter.INSTANCE.convert(currentUser);
            contactSearchVO.setContactType(ContactTypeEnum.ACCOUNT.getCode());

            //查询是否是好友
            ContactDO contactDO = contactDAO.selectByContactId(currentUser.getUid(), id, ContactTypeEnum.ACCOUNT.getCode());
            contactSearchVO.setIsContact(Objects.nonNull(contactDO));
            contactSearchVOList.add(contactSearchVO);
        }

        //2.搜索群组
        RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(id);
        if (Objects.nonNull(roomGroupDO)) {
            ContactSearchVO contactSearchVO = GroupInfoConverter.INSTANCE.toContact(roomGroupDO);
            contactSearchVO.setContactType(ContactTypeEnum.GROUP.getCode());

            //查询是否是群组
            ContactDO contactDO = contactDAO.selectByContactId(currentUser.getUid(), id, ContactTypeEnum.GROUP.getCode());
            contactSearchVO.setIsContact(Objects.nonNull(contactDO));
            contactSearchVOList.add(contactSearchVO);
        }

        return contactSearchVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addFriend(Long uid, Long receiveUid) {
        ContactDO userContact = contactDAO.selectByContactId(uid, receiveUid, ContactTypeEnum.ACCOUNT.getCode());
        if (Objects.nonNull(userContact)) {
            throw new BusinessException(BaseErrorCodeEnum.ALREADY_FRIEND);
        }

        //生成聊天室
        RoomDO roomDO = new RoomDO();
        roomDO.setRoomId(commonIDGenerator.nextId(IDHashKeyEnum.ROOM_ID.getHashKey()));
        roomDO.setRoomType(RoomTypeEnum.FRIEND.getCode());
        roomDAO.insert(roomDO);

        //保存双方好友关系
        FriendDO friendDO = new FriendDO();
        friendDO.setUid(uid);
        friendDO.setFriendUid(receiveUid);
        friendDO.setRoomId(roomDO.getRoomId());
        friendDO.setStatus(FriendStatusEnum.FRIEND.getCode());
        friendDAO.insert(friendDO);

        friendDO.setUid(receiveUid);
        friendDO.setFriendUid(uid);
        friendDO.setRoomId(roomDO.getRoomId());
        friendDO.setStatus(FriendStatusEnum.FRIEND.getCode());
        friendDAO.insert(friendDO);

        //保存到联系表
        ContactDO contactDO = new ContactDO();
        contactDO.setUid(uid);
        contactDO.setRoomId(roomDO.getRoomId());
        contactDO.setContactType(ContactTypeEnum.ACCOUNT.getCode());
        contactDO.setContactId(receiveUid);
        contactDAO.insert(contactDO);

        contactDO.setUid(receiveUid);
        contactDO.setRoomId(roomDO.getRoomId());
        contactDO.setContactType(ContactTypeEnum.ACCOUNT.getCode());
        contactDO.setContactId(uid);
        contactDAO.insert(contactDO);

        return roomDO.getRoomId();
    }
}
