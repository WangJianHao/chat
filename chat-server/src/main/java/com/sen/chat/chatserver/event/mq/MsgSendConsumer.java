package com.sen.chat.chatserver.event.mq;

import com.sen.chat.chatserver.constant.MQConstant;
import com.sen.chat.chatserver.dao.*;
import com.sen.chat.chatserver.dto.MsgSendMessageDTO;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.entity.*;
import com.sen.chat.chatserver.entity.query.GroupMemberQuery;
import com.sen.chat.chatserver.service.ChatService;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.common.constant.dict.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消费消息发送消息，推给WS发送消息队列
 *
 * @description:
 * @author: sensen
 * @date: 2024/9/1 2:20
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = MQConstant.SEND_MSG_GROUP, topic = MQConstant.SEND_MSG_TOPIC)
@Component
@AllArgsConstructor
public class MsgSendConsumer implements RocketMQListener<MsgSendMessageDTO> {

    private MessageDAO messageDAO;

    private RoomDAO roomDAO;

    private ChatService chatService;

    private GroupDAO groupDAO;

    private GroupMemberDAO groupMemberDAO;

    private PushService pushService;

    private ContactDAO contactDAO;

    private WSAdapter wsAdapter;

    private FriendDAO friendDAO;


    /**
     * 消费消息发送事件
     *
     * @param dto 用户发送信息的id
     */
    @Override
    public void onMessage(MsgSendMessageDTO dto) {
        MessageDO message = messageDAO.selectById(dto.getMsgId());
        if (Objects.isNull(message)) {
            log.error("消息不存在，消息id：{}", dto.getMsgId());
            return;
        }
        RoomDO room = roomDAO.selectByRoomId(message.getRoomId());
        ChatMessageResp msgResp = chatService.getMsgResp(message, null);

        //所有房间更新房间最新消息
        roomDAO.updateByRoomId(room.getRoomId(), message.getId(), message.getCreateTime());

        List<Long> memberUidList = new ArrayList<>();
        if (Objects.equals(room.getRoomType(), RoomTypeEnum.GROUP.getCode())) {//普通群聊推送所有群成员
            RoomGroupDO roomGroupDO = groupDAO.selectByGroupId(room.getRoomId());
            GroupMemberQuery query = GroupMemberQuery.builder().groupId(roomGroupDO.getGroupId()).build();
            memberUidList = groupMemberDAO.selectList(query).stream().map(GroupUserRelationDO::getUid).collect(Collectors.toList());
        } else if (Objects.equals(room.getRoomType(), RoomTypeEnum.FRIEND.getCode())) {//单聊对象
            //对单人推送
            FriendDO friendDO = friendDAO.selectByRoomId(message.getFromUid(), room.getRoomId());
            memberUidList = Arrays.asList(friendDO.getUid(), friendDO.getFriendUid());
        } else {
            return;
        }

        //更新所有群成员的会话时间
        contactDAO.updateBatch(room.getRoomId(), memberUidList, message.getId(), message.getCreateTime());

        //推送房间成员
        pushService.sendPushMsg(wsAdapter.buildMsgSend(msgResp), memberUidList);

    }
}
