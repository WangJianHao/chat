package com.sen.chat.chatserver.dao;

import com.sen.chat.chatserver.entity.query.MessageMarkQuery;
import com.sen.chat.chatserver.entity.MessageMarkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 19:40
 */
@Repository
public interface MessageMarkDAO {


    List<MessageMarkDO> selectList(@Param("query") MessageMarkQuery query);
}
