package com.sen.chat.chatserver.convert;

import com.sen.chat.chatserver.entity.GroupMemberDO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.chatserver.dto.vo.ContactSearchVO;
import com.sen.chat.chatserver.dto.vo.GroupMemberVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/29 16:11
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    GroupMemberVO convert(GroupMemberDO groupMemberDO);

    ContactSearchVO convert(UserInfoDO userInfoDO);
}
