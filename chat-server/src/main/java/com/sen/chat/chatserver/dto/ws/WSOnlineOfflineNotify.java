package com.sen.chat.chatserver.dto.ws;

import com.sen.chat.chatserver.dto.resp.ChatMemberResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户上下线变动的推送类
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 17:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSOnlineOfflineNotify {

    //新的上下线用户
    private List<ChatMemberResp> changeList = new ArrayList<>();

    //在线人数
    private Long onlineNum;
}
