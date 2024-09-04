package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.ContactDO;
import com.sen.chat.chatserver.entity.ContactInfoDO;
import com.sen.chat.chatserver.entity.query.ContactQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 23:08
 */
@Repository
public interface ContactDAO extends BaseMapper<ContactDO> {

    ContactDO selectByPrimaryKey(@Param("uid") Long uid, @Param("roomId") Long roomId);

    List<ContactInfoDO> queryContactInfo(@Param("uid") Long uid, @Param("searchText") String searchText);

    List<ContactDO> selectList(@Param("query") ContactQuery query);

    int updateBatch(@Param("roomId") Long roomId, @Param("memberUidList") List<Long> memberUidList, @Param("msgId") Long msgId, @Param("msgTime") Timestamp msgTime);

    ContactDO selectByContactId(@Param("uid") Long uid, @Param("contactId") Long contactId, @Param("contactType") Integer contactType);

    int deleteByRoomIdAndUid(@Param("roomId") Long roomId, @Param("uid") Long uid);
}
