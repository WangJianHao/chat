package com.sen.chat.chatserver.event.listener;

import com.sen.chat.chatserver.cache.UserOnlineCache;
import com.sen.chat.chatserver.dao.ContactDAO;
import com.sen.chat.chatserver.dao.UserInfoDAO;
import com.sen.chat.chatserver.dao.UserLoginInfoDAO;
import com.sen.chat.chatserver.dto.UserInfoDTO;
import com.sen.chat.chatserver.entity.ContactDO;
import com.sen.chat.chatserver.entity.UserLoginInfoDO;
import com.sen.chat.chatserver.entity.query.ContactQuery;
import com.sen.chat.chatserver.event.UserOfflineEvent;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.chatserver.websocket.WebSocketService;
import com.sen.chat.common.constant.dict.ContactTypeEnum;
import com.sen.chat.common.constant.dict.UserActiveStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户下线监听器
 *
 * @description:
 * @author: sensen
 * @date: 2023/6/31 22:15
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserOfflineListener {

    private WebSocketService webSocketService;

    private UserInfoDAO userInfoDAO;

    private ContactDAO contactDAO;

    private UserLoginInfoDAO userLoginInfoDAO;

    private UserOnlineCache userOnlineCache;

    private WSAdapter wsAdapter;

    /**
     * 监听到ws断连后推送
     *
     * @param event
     */
    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent event) {
        UserInfoDTO user = event.getUserInfoDTO();
        userOnlineCache.offline(user.getUid(), new Date());

        //推送给好友和群友，该用户登录成功
        ContactQuery query = ContactQuery.builder().uid(user.getUid()).build();
        //查询好友和群组
        List<ContactDO> contactDOS = contactDAO.selectList(query);
        Map<Integer, List<ContactDO>> contactMap = contactDOS.stream().collect(Collectors.groupingBy(ContactDO::getContactType));
        //推送给好友
        List<Long> uidList = contactMap.getOrDefault(ContactTypeEnum.ACCOUNT.getCode(), Collections.emptyList()).stream().map(ContactDO::getContactId).collect(Collectors.toList());

        List<Long> groupIdList = contactMap.get(ContactTypeEnum.GROUP.getCode()).stream().map(ContactDO::getContactId).collect(Collectors.toList());


        //推送给好友和群组
        webSocketService.sendToUid(wsAdapter.buildOfflineNotifyResp(user), uidList);
    }

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent event) {
        UserInfoDTO user = event.getUserInfoDTO();
        UserLoginInfoDO userLoginInfoDO = new UserLoginInfoDO();
        userLoginInfoDO.setUid(user.getUid());
        userLoginInfoDO.setActiveStatus(UserActiveStatusEnum.OFFLINE.getCode());
        userLoginInfoDO.setLastOffTime(Timestamp.valueOf(LocalDateTime.now()));
        userLoginInfoDAO.updateById(userLoginInfoDO);
    }
}
