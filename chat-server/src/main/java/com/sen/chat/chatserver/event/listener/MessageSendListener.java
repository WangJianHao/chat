package com.sen.chat.chatserver.event.listener;

import com.sen.chat.chatserver.constant.MQConstant;
import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dao.RoomDAO;
import com.sen.chat.chatserver.dto.MsgSendMessageDTO;
import com.sen.chat.chatserver.event.MessageSendEvent;
import com.sen.chat.chatserver.event.mq.MQProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 消息发送监听器
 *
 * @description:
 * @author: sensen
 * @date: 2024/9/1 12:21
 */
@Slf4j
@Component
@AllArgsConstructor
public class MessageSendListener {

    private MQProducer mqProducer;

    private MessageDAO messageDAO;

    private RoomDAO roomDAO;


    /**
     * MQ推送消息发送事件
     *
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendMsg(MQConstant.SEND_MSG_TOPIC, new MsgSendMessageDTO(msgId), msgId);
    }

//    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
//    public void handlerMsg(@NotNull MessageSendEvent event) {
//        MessageDO message = messageDAO.selectById(event.getMsgId());
//        RoomDO room = roomDAO.selectByRoomId(message.getRoomId());
//    }

}
