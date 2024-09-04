package com.sen.chat.chatserver.event.listener;

import com.sen.chat.chatserver.entity.query.ContactQuery;
import com.sen.chat.chatserver.cache.UserOnlineCache;
import com.sen.chat.chatserver.dao.ContactDAO;
import com.sen.chat.chatserver.dao.GroupMemberDAO;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.entity.ContactDO;
import com.sen.chat.chatserver.event.mq.PushService;
import com.sen.chat.chatserver.event.UserOnlineEvent;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.common.constant.dict.ContactTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户上线事件监听器
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 15:46
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserOnlineListener {

    private UserOnlineCache userOnlineCache;

    private PushService pushService;

    private WSAdapter wsAdapter;

    private ContactDAO contactDAO;

    private GroupMemberDAO groupMemberDAO;

    /**
     * 1.上线事件监听到修改为上线状态
     * 2.推送消息
     *
     * @param event 用户上线事件
     */
    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        UserInfoDTO user = event.getUserInfoDTO();
        userOnlineCache.online(user.getUid(), new Date());
        //推送给好友和群友，该用户登录成功
        ContactQuery query = ContactQuery.builder().uid(user.getUid()).build();
        //查询好友和群组
        List<ContactDO> contactDOS = contactDAO.selectList(query);
        Map<Integer, List<ContactDO>> contactMap = contactDOS.stream().collect(Collectors.groupingBy(ContactDO::getContactType));
        //推送给好友
        List<Long> uidList = contactMap.getOrDefault(ContactTypeEnum.ACCOUNT.getCode(), Collections.emptyList()).stream().map(ContactDO::getContactId).collect(Collectors.toList());

        List<Long> groupIdList = contactMap.get(ContactTypeEnum.GROUP.getCode()).stream().map(ContactDO::getContactId).collect(Collectors.toList());

//        groupMemberDAO.queryMemberList()
        pushService.sendPushMsg(wsAdapter.buildOnlineNotifyResp(event.getUserInfoDTO()), uidList);
    }

}
