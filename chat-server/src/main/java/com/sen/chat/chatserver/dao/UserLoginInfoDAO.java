package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.UserLoginInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 19:15
 */
@Repository
public interface UserLoginInfoDAO extends BaseMapper<UserLoginInfoDO> {


    UserLoginInfoDO selectByUid(@Param("uid") Long uid);
}
