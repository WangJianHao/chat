package com.sen.chat.chatserver.websocket.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.sen.chat.chatserver.dto.ws.WSBaseResp;
import com.sen.chat.chatserver.cache.UserOnlineCache;
import com.sen.chat.chatserver.config.ThreadPoolConfig;
import com.sen.chat.chatserver.constant.RedisConstant;
import com.sen.chat.chatserver.dao.UserInfoDAO;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.event.UserOfflineEvent;
import com.sen.chat.chatserver.event.UserOnlineEvent;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.chatserver.websocket.WebSocketService;
import com.sen.chat.chatserver.websocket.netty.NettyContextHolder;
import com.sen.chat.common.constant.errorcode.ResultCodeEnum;
import com.sen.chat.common.domain.UserDTO;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.service.RedisService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/31 12:51
 */
@Slf4j
@Service
@AllArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, Long> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    private RedisService redisService;

    private UserOnlineCache userOnlineCache;

    private UserInfoDAO userInfoDAO;

    private ApplicationEventPublisher applicationEventPublisher;

    private WSAdapter wsAdapter;

    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void authorize(Channel channel, String token) {
        UserDTO userDTO = (UserDTO) redisService.get(RedisConstant.TOKEN_KEY + token);
        if (Objects.isNull(userDTO)) {
            sendMsg(channel, wsAdapter.buildInvalidateTokenResp());
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        //更新上线列表
        online(channel, userDTO.getUid());
        //发送用户上线事件
        boolean online = userOnlineCache.isOnline(userDTO.getUid());
        if (!online) {
            UserInfoDTO userInfoDTO = userInfoDAO.selectByUidWithLoginInfo(userDTO.getUid());
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, userInfoDTO));
        }
    }

    @Override
    public void connect(Channel channel) {

    }

    @Override
    public void removed(Channel channel) {
        Optional<Long> uidOptional = Optional.ofNullable(ONLINE_WS_MAP.get(channel));
        boolean offlineAll = offline(channel, uidOptional);
        if (uidOptional.isPresent() && offlineAll) {//已登录用户断连,并且全下线成功
            UserInfoDTO user = new UserInfoDTO();
            user.setUid(uidOptional.get());
            applicationEventPublisher.publishEvent(new UserOfflineEvent(this, user));
        }
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp, List<Long> skipUidList) {
        Set<Long> skipUidSet = new HashSet<>(skipUidList);
        ONLINE_WS_MAP.forEach((channel, uid) -> {
            if (CollectionUtils.isNotEmpty(skipUidSet) && skipUidSet.contains(uid)) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    @Override
    public void sendToUid(WSBaseResp<?> wsBaseResp, Long uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
        if (CollectionUtil.isEmpty(channels)) {
            log.info("用户：{}不在线", uid);
            return;
        }
        channels.forEach(channel -> {
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, wsBaseResp));
        });
    }

    @Override
    public void sendToUid(WSBaseResp<?> wsBaseResp, List<Long> uidList) {
        uidList.forEach(uid -> {
            sendToUid(wsBaseResp, uid);
        });
    }

    /**
     * 用户上线
     */
    private void online(Channel channel, Long uid) {
        Long aLong = ONLINE_WS_MAP.get(channel);
        if (Objects.isNull(aLong)) {
            ONLINE_WS_MAP.put(channel, uid);
        }
        ONLINE_UID_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        ONLINE_UID_MAP.get(uid).add(channel);
        NettyContextHolder.setAttr(channel, NettyContextHolder.UID, uid);
    }

    /**
     * 用户下线
     * return 是否全下线成功
     */
    private boolean offline(Channel channel, Optional<Long> uidOptional) {
        ONLINE_WS_MAP.remove(channel);
        if (uidOptional.isPresent()) {
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uidOptional.get());
            if (CollectionUtil.isNotEmpty(channels)) {
                channels.removeIf(ch -> Objects.equals(ch, channel));
            }
            return CollectionUtil.isEmpty(ONLINE_UID_MAP.get(uidOptional.get()));
        }
        return true;
    }

    /**
     * 给本地channel发送消息
     *
     * @param channel
     * @param wsBaseResp
     */
    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }
}
