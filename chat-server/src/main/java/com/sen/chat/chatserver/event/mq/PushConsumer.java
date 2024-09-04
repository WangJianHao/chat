package com.sen.chat.chatserver.event.mq;

import com.sen.chat.chatserver.constant.MQConstant;
import com.sen.chat.chatserver.constant.WSPushTypeEnum;
import com.sen.chat.chatserver.dto.PushMessageDTO;
import com.sen.chat.chatserver.websocket.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息队列 PUSH_TOPIC 消费者
 * 交给WS 推送给用户
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 20:40
 */
@RocketMQMessageListener(topic = MQConstant.PUSH_TOPIC, consumerGroup = MQConstant.PUSH_GROUP, messageModel = MessageModel.BROADCASTING)
@Slf4j
@Component
@AllArgsConstructor
public class PushConsumer implements RocketMQListener<PushMessageDTO> {

    private WebSocketService webSocketService;

    /**
     * 接收到消息后交给WS 推送给在线的用户
     *
     * @param message 消息体
     */
    @Override
    public void onMessage(PushMessageDTO message) {
        WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER:
                message.getUidList().forEach(uid -> {
                    webSocketService.sendToUid(message.getWsBaseMsg(), uid);
                });
                break;
            case ALL:
                webSocketService.sendToAllOnline(message.getWsBaseMsg(), null);
                break;
        }
    }
}
