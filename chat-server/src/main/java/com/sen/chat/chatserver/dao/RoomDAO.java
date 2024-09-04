package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.RoomDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:37
 */
@Repository
public interface RoomDAO extends BaseMapper<RoomDO> {

    RoomDO selectByRoomId(Long roomId);

    int updateByRoomId(@Param("roomId") Long roomId, @Param("msgId") Long msgId, @Param("msgTime") Timestamp msgTime);
}
