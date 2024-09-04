package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.common.domain.UserDTO;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/26 14:11
 */
public interface UserInfoService {

    /**
     * 获取通用用户信息
     */
    UserDTO loadUserByUid(Long uid);

    /**
     * 获取当前用户信息
     */
    UserInfoDO getCurrentUser();


    /**
     * 获取完整用户信息
     */
    UserInfoDTO getUserInfoAll(Long uid);

}
