package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.query.UserInfoQuery;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 14:14
 */
@Repository
public interface UserInfoDAO extends BaseMapper<UserInfoDO> {

    List<UserInfoDO> selectList(@Param("query") UserInfoQuery query);

    UserInfoDO selectByUid(@Param("uid") Long uid);

    UserInfoDTO selectByUidWithLoginInfo(@Param("uid") Long uid);
}
