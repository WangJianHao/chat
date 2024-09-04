package com.sen.chat.chatserver.convert;

import com.sen.chat.chatserver.entity.RoomGroupDO;
import com.sen.chat.chatserver.dto.vo.ContactSearchVO;
import com.sen.chat.chatserver.dto.vo.GroupInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/28 1:14
 */
@Mapper
public interface GroupInfoConverter {
    GroupInfoConverter INSTANCE = Mappers.getMapper(GroupInfoConverter.class);

    GroupInfoVO convert(RoomGroupDO roomGroupDO);

    ContactSearchVO toContact(RoomGroupDO roomGroupDO);
}
