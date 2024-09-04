package com.sen.chat.chatserver.event;

import com.sen.chat.chatserver.dto.UserInfoDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 15:46
 */
public class UserOnlineEvent extends ApplicationEvent {

    private final UserInfoDTO userInfoDTO;

    public UserOnlineEvent(Object source, UserInfoDTO userInfoDTO) {
        super(source);
        this.userInfoDTO = userInfoDTO;
    }

    public UserInfoDTO getUserInfoDTO() {
        return userInfoDTO;
    }
}
