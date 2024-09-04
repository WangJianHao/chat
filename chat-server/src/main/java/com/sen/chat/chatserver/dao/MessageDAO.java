package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.MessageDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:33
 */
@Repository
public interface MessageDAO extends BaseMapper<MessageDO> {

    MessageDO selectById(@Param("msgId") Long msgId);

    Integer getGapCount(@Param("roomId") Long roomId, @Param("fromId") Long fromId, @Param("toId") Long toId);

}
