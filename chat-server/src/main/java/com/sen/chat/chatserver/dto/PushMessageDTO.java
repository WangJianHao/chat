package com.sen.chat.chatserver.dto;

import com.sen.chat.chatserver.constant.WSPushTypeEnum;
import com.sen.chat.chatserver.dto.ws.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 推送给用户的消息对象
 *
 * @description: 推送给用户的消息对象
 * @author: sensen
 * @date: 2023/6/31 17:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessageDTO implements Serializable {

    /**
     * 推送的ws消息
     */
    private WSBaseResp<?> wsBaseMsg;

    /**
     * 推送的uid
     */
    private List<Long> uidList;

    /**
     * 推送类型 1个人 2群组 3全部在线
     *
     * @see WSPushTypeEnum
     */
    private Integer pushType;

    public PushMessageDTO(Long uid, WSBaseResp<?> wsBaseMsg) {
        this.uidList = Collections.singletonList(uid);
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.USER.getCode();
    }

    public PushMessageDTO(List<Long> uidList, WSBaseResp<?> wsBaseMsg) {
        this.uidList = uidList;
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.USER.getCode();
    }

    public PushMessageDTO(WSBaseResp<?> wsBaseMsg) {
        this.wsBaseMsg = wsBaseMsg;
        this.pushType = WSPushTypeEnum.ALL.getCode();
    }
}
