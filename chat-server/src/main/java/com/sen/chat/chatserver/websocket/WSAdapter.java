package com.sen.chat.chatserver.websocket;

import cn.hutool.core.bean.BeanUtil;
import com.sen.chat.chatserver.constant.WSRespTypeEnum;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.dto.WSMemberChangeDTO;
import com.sen.chat.chatserver.dto.resp.ChatMemberResp;
import com.sen.chat.chatserver.dto.resp.ChatMemberStatisticResp;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.dto.ws.WSBaseResp;
import com.sen.chat.chatserver.dto.ws.WSApply;
import com.sen.chat.chatserver.dto.ws.WSOnlineOfflineNotify;
import com.sen.chat.chatserver.service.ChatService;
import com.sen.chat.common.constant.dict.UserActiveStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.sen.chat.chatserver.dto.WSMemberChangeDTO.CHANGE_TYPE_ADD;
import static com.sen.chat.chatserver.dto.WSMemberChangeDTO.CHANGE_TYPE_REMOVE;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/31 17:50
 */
@Component
@AllArgsConstructor
public class WSAdapter {

    private ChatService chatService;

    public WSBaseResp<WSOnlineOfflineNotify> buildOnlineNotifyResp(UserInfoDTO user) {
        WSBaseResp<WSOnlineOfflineNotify> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        WSOnlineOfflineNotify onlineOfflineNotify = new WSOnlineOfflineNotify();
        onlineOfflineNotify.setChangeList(Collections.singletonList(buildOnlineInfo(user)));
        assembleNum(onlineOfflineNotify);
        wsBaseResp.setData(onlineOfflineNotify);
        return wsBaseResp;
    }

    public WSBaseResp<WSOnlineOfflineNotify> buildOfflineNotifyResp(UserInfoDTO user) {
        WSBaseResp<WSOnlineOfflineNotify> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        WSOnlineOfflineNotify onlineOfflineNotify = new WSOnlineOfflineNotify();
        onlineOfflineNotify.setChangeList(Collections.singletonList(buildOfflineInfo(user)));
        assembleNum(onlineOfflineNotify);
        wsBaseResp.setData(onlineOfflineNotify);
        return wsBaseResp;
    }

    public WSBaseResp<?> buildInvalidateTokenResp() {
        WSBaseResp<?> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }

    public WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
        wsBaseResp.setData(msgResp);
        return wsBaseResp;
    }

    public WSBaseResp<?> buildApplySend(WSApply wsApply) {
        WSBaseResp<WSApply> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.APPLY.getType());
        wsBaseResp.setData(wsApply);
        return wsBaseResp;
    }

    public static WSBaseResp<WSMemberChangeDTO> buildMemberAddWS(Long roomId, UserInfoDTO user) {
        WSBaseResp<WSMemberChangeDTO> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChangeDTO wsMemberChange = new WSMemberChangeDTO();
        wsMemberChange.setActiveStatus(user.getActiveStatus());
        wsMemberChange.setLastOptTime(user.getLastOptTime());
        wsMemberChange.setUid(user.getUid());
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setChangeType(CHANGE_TYPE_ADD);
        wsBaseResp.setData(wsMemberChange);
        return wsBaseResp;
    }

    public static WSBaseResp<WSMemberChangeDTO> buildMemberRemoveWS(Long roomId, Long uid) {
        WSBaseResp<WSMemberChangeDTO> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChangeDTO wsMemberChange = new WSMemberChangeDTO();
        wsMemberChange.setUid(uid);
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setChangeType(CHANGE_TYPE_REMOVE);
        wsBaseResp.setData(wsMemberChange);
        return wsBaseResp;
    }


    private void assembleNum(WSOnlineOfflineNotify onlineOfflineNotify) {
        ChatMemberStatisticResp memberStatistic = chatService.getMemberStatistic();
        onlineOfflineNotify.setOnlineNum(memberStatistic.getOnlineNum());
    }

    private ChatMemberResp buildOfflineInfo(UserInfoDTO user) {
        ChatMemberResp info = new ChatMemberResp();
        BeanUtil.copyProperties(user, info);
        info.setUid(user.getUid());
        info.setActiveStatus(UserActiveStatusEnum.OFFLINE.getCode());
        info.setLastOptTime(user.getLastOptTime());
        return info;
    }

    private ChatMemberResp buildOnlineInfo(UserInfoDTO user) {
        ChatMemberResp info = new ChatMemberResp();
        BeanUtil.copyProperties(user, info);
        info.setUid(user.getUid());
        info.setActiveStatus(UserActiveStatusEnum.ONLINE.getCode());
        info.setLastOptTime(user.getLastOptTime());
        return info;
    }


}
