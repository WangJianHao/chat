package com.sen.chat.chatserver.cache;

import cn.hutool.core.collection.CollUtil;
import com.sen.chat.chatserver.entity.query.UserInfoQuery;
import com.sen.chat.chatserver.constant.RedisConstant;
import com.sen.chat.chatserver.dao.UserInfoDAO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.common.service.RedisService;
import com.sen.chat.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 18:02
 */
@Component
@AllArgsConstructor
public class UserInfoCache {

    private final UserInfoDAO userInfoDAO;

    private final RedisService redisService;

    public void remove(Long uid) {
        redisService.hDel(RedisConstant.USER_INFO_CACHE_KEY, uid);
    }

    public void setUserInfo(UserInfoDO userInfoDO) {
        redisService.hSet(RedisConstant.USER_INFO_CACHE_KEY, String.valueOf(userInfoDO.getUid()), userInfoDO, RedisConstant.TEM_MINUTE);
    }

    /**
     * 获取用户信息，盘路缓存模式
     */
    public UserInfoDO getUserInfo(Long uid) {
        return getUserInfoBatch(Collections.singletonList(uid)).get(uid);
    }

    /**
     * 获取用户信息，盘路缓存模式
     */
    public Map<Long, UserInfoDO> getUserInfoBatch(List<Long> uids) {
        //批量get
        List<UserInfoDO> mget = redisService.hGet(RedisConstant.USER_INFO_CACHE_KEY, uids.stream().map(String::valueOf).collect(Collectors.toList()), UserInfoDO.class);
        Map<Long, UserInfoDO> map = mget.stream().filter(Objects::nonNull).collect(Collectors.toMap(UserInfoDO::getUid, Function.identity()));
        //发现差集——还需要load更新的uid
        List<Long> needLoadUidList = uids.stream().filter(a -> !map.containsKey(a)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needLoadUidList)) {
            //批量load
            UserInfoQuery query = UserInfoQuery.builder().uidList(needLoadUidList).build();
            List<UserInfoDO> needLoadUserList = userInfoDAO.selectList(query);
            Map<String, UserInfoDO> redisMap = needLoadUserList.stream().collect(Collectors.toMap(userInfoDO -> String.valueOf(userInfoDO.getUid()), Function.identity()));
            redisService.hSetAll(RedisConstant.USER_INFO_CACHE_KEY, redisMap, RedisConstant.TEM_MINUTE);
            //加载回redis
            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(UserInfoDO::getUid, Function.identity())));
        }
        return map;
    }

    public void userInfoChange(Long uid) {
        remove(uid);
        refreshUserModifyTime(uid);
    }

    public List<Long> getUserModifyTime(List<Long> uidList) {
        return redisService.hGet(RedisConstant.USER_INFO_MODIFY_TIME_KEY, uidList.stream().map(String::valueOf).collect(Collectors.toList()), Long.class);
    }

    public void refreshUserModifyTime(Long uid) {
        redisService.hSet(RedisConstant.USER_INFO_MODIFY_TIME_KEY, String.valueOf(uid), DateUtil.getCurrentTimestamp());
    }
}
