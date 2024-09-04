package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.GroupMemberDO;
import com.sen.chat.chatserver.entity.GroupUserRelationDO;
import com.sen.chat.chatserver.entity.query.GroupMemberQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 22:54
 */
@Repository
public interface GroupMemberDAO extends BaseMapper<GroupUserRelationDO> {

    int selectCount(@Param("query") GroupMemberQuery query);

    List<GroupUserRelationDO> selectList(@Param("query") GroupMemberQuery query);

    List<GroupMemberDO> queryMemberList(@Param("groupId") Long groupId);

    GroupUserRelationDO selectByPrimaryKey(@Param("groupId") Long groupId, @Param("uid") Long uid);

    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("uid") Long uid);
}
