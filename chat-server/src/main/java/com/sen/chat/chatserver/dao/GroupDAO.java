package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.query.GroupInfoQuery;
import com.sen.chat.chatserver.entity.RoomGroupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:13
 */
@Repository
public interface GroupDAO extends BaseMapper<RoomGroupDO> {

    int selectCount(@Param("query") GroupInfoQuery query);

    RoomGroupDO selectByGroupId(@Param("groupId") Long groupId);

    RoomGroupDO selectByRoomId(@Param("roomId") Long roomId);
}
