package com.sen.chat.chatserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sen.chat.chatserver.entity.UserBeautyDO;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 14:08
 */
@Repository
public interface UserBeautyDAO extends BaseMapper<UserBeautyDO> {


    UserBeautyDO selectByEmail(String email);
}
