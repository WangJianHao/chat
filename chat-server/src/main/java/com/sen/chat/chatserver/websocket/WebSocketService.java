package com.sen.chat.chatserver.websocket;

import com.sen.chat.chatserver.dto.ws.WSBaseResp;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/30 14:22
 */
public interface WebSocketService {

    /**
     * 验证token
     *
     * @param channel
     * @param token
     */
    void authorize(Channel channel, String token);

    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 处理ws断开连接的事件
     *
     * @param channel
     */
    void removed(Channel channel);

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     * @param skipUidList    需要跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, List<Long> skipUidList);

    /**
     * 推送消息给指定的人
     */
    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);

    void sendToUid(WSBaseResp<?> wsBaseResp, List<Long> uidList);

}
