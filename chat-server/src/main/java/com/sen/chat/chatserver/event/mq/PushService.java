package com.sen.chat.chatserver.event.mq;

import com.sen.chat.chatserver.constant.MQConstant;
import com.sen.chat.chatserver.dto.PushMessageDTO;
import com.sen.chat.chatserver.dto.ws.WSBaseResp;
import com.sen.chat.chatserver.event.mq.MQProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:35
 */
@Service
@AllArgsConstructor
public class PushService {

    private MQProducer mqProducer;

    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uidList, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
