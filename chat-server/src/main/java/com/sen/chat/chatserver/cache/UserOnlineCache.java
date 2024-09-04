package com.sen.chat.chatserver.cache;

import com.sen.chat.chatserver.constant.RedisConstant;
import com.sen.chat.common.service.RedisService;
import com.sen.chat.common.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 15:37
 */
@Component
@AllArgsConstructor
public class UserOnlineCache {

    private RedisService redisService;

    public Long getOnlineNum() {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        return RedisUtils.zCard(onlineKey);
    }

    public Long getOfflineNum() {
        String offlineKey = RedisConstant.ONLINE_UID_ZET;
        return RedisUtils.zCard(offlineKey);
    }

    //移除用户
    public void remove(Long uid) {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        String offlineKey = RedisConstant.OFFLINE_UID_ZET;
        //移除离线表
        redisService.zRemove(offlineKey, uid);
        //移除上线表
        redisService.zRemove(onlineKey, uid);
    }

    //用户上线
    public void online(Long uid, Date optTime) {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        String offlineKey = RedisConstant.OFFLINE_UID_ZET;
        //移除离线表
        redisService.zRemove(offlineKey, uid);
        //更新上线表
        redisService.zAdd(onlineKey, uid, optTime.getTime());
    }

    //获取用户上线列表
    public List<Long> getOnlineUidList() {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        Set<String> strings = RedisUtils.zAll(onlineKey);
        return strings.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public boolean isOnline(Long uid) {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        return Objects.nonNull(redisService.score(onlineKey, uid));
    }

    //用户下线
    public void offline(Long uid, Date optTime) {
        String onlineKey = RedisConstant.ONLINE_UID_ZET;
        String offlineKey = RedisConstant.OFFLINE_UID_ZET;
        //移除上线线表
        redisService.zRemove(onlineKey, uid);
        //更新上线表
        redisService.zAdd(offlineKey, uid, optTime.getTime());
    }


}
