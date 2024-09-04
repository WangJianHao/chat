package com.sen.chat.chatserver.websocket.netty;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 保存需要的attributeKey到channel中
 *
 * @description:
 * @author: sensen
 * @date: 2023/7/30 14:23
 */
public class NettyContextHolder {

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        Attribute<T> attr = channel.attr(attributeKey);
        attr.set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> ip) {
        return channel.attr(ip).get();
    }
}
