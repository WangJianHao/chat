package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.FriendDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 11:59
 */
@Repository
public interface FriendDAO extends BaseMapper<FriendDO> {
    FriendDO selectByRoomId(@Param("uid") Long uid, @Param("roomId") Long roomId);
}
