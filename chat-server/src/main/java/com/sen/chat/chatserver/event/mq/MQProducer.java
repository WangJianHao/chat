package com.sen.chat.chatserver.event.mq;

import com.sen.chat.common.annotation.SecureInvoke;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 消息队列生产者
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:36
 */
@Slf4j
@Component
@AllArgsConstructor
public class MQProducer {

    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步发送消息
     *
     * @param topic 消息topic
     * @param body  消息体
     */
    public void sendMsg(String topic, Object body) {
        Message<Object> message = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.send(topic, message);
    }

    public void sendMsg(String topic, Object body, Object key) {
        Message<Object> message = MessageBuilder
                .withPayload(body)
                .setHeader("KEYS", key)
                .build();
        rocketMQTemplate.send(topic, message);
    }

    /**
     * 异步发送消息
     *
     * @param topic 消息topic
     * @param body  消息体
     */
    public void asyncSend(String topic, Object body) {
        Message<Object> message = MessageBuilder.withPayload(body).build();
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消息发送成功:{}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("消息发送失败:", throwable);
            }
        });
    }

    /**
     * 发送可靠消息，在事务提交后保证发送成功
     */
    @SecureInvoke
    public void sendSecureMsg(String topic, Object body, Object key) {
        Message<Object> build = MessageBuilder
                .withPayload(body)
                .setHeader("KEYS", key)
                .build();
        rocketMQTemplate.send(topic, build);
    }
}
