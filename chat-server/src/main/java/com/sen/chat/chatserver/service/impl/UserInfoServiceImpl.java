package com.sen.chat.chatserver.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.sen.chat.chatserver.cache.UserInfoCache;
import com.sen.chat.chatserver.cache.UserOnlineCache;
import com.sen.chat.chatserver.dao.UserInfoDAO;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.chatserver.service.UserInfoService;
import com.sen.chat.common.constant.AuthConstant;
import com.sen.chat.common.constant.dict.UserActiveStatusEnum;
import com.sen.chat.common.constant.errorcode.ResultCodeEnum;
import com.sen.chat.common.domain.UserDTO;
import com.sen.chat.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户服务类
 *
 * @description:
 * @author: sensen
 * @date: 2023/7/26 14:12
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoDAO userInfoDAO;

    private final UserInfoCache userInfoCache;

    private final UserOnlineCache userOnlineCache;

    private HttpServletRequest request;

    @Override
    public UserDTO loadUserByUid(Long uid) {
        //获取用户信息
        UserInfoDO userInfoDO = getUserInfoByUid(uid);
        if (userInfoDO != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userInfoDO, userDTO);
            userDTO.setClientId(AuthConstant.PORTAL_CLIENT_ID);
            return userDTO;
        }
        return null;
    }

    @Override
    public UserInfoDO getCurrentUser() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        //从token中获取通用用户信息
        UserDTO userDto = JSONUtil.toBean(userStr, UserDTO.class);

        //todo 查询用户群组 好友信息
        //先从缓存中获取，缓存中不存在就从数据库中获取并更新缓存
        UserInfoDO userInfo = userInfoCache.getUserInfo(userDto.getUid());
        if (Objects.nonNull(userInfo)) {
            return userInfo;
        }
        UserInfoDO userInfoDO = userInfoDAO.selectByUid(userDto.getUid());
        userInfoCache.setUserInfo(userInfoDO);
        return userInfoDO;
    }

    @Override
    public UserInfoDTO getUserInfoAll(Long uid) {
        boolean online = userOnlineCache.isOnline(uid);
        UserInfoDTO userInfoDTO = userInfoDAO.selectByUidWithLoginInfo(uid);
        if (online) {
            userInfoDTO.setActiveStatus(UserActiveStatusEnum.ONLINE.getCode());
        } else {
            userInfoDTO.setActiveStatus(UserActiveStatusEnum.OFFLINE.getCode());
        }
        return userInfoDTO;
    }

    private UserInfoDO getUserInfoByUid(Long uid) {
        UserInfoDO userInfo = userInfoCache.getUserInfo(uid);
        if (Objects.nonNull(userInfo)) {
            return userInfo;
        }
        userInfo = userInfoDAO.selectByUid(uid);
        userInfoCache.setUserInfo(userInfo);
        return userInfo;
    }
}
