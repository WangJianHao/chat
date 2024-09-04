package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.vo.ContactSearchVO;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/29 21:59
 */
public interface ContactService {

    List<ContactSearchVO> searchContact(String searchText);

    /**
     * 搜索目标人或目标群
     *
     * @param id 目标ID
     * @return 目标信息
     */
    List<ContactSearchVO> search(Long id);


    /**
     * 添加好友
     *
     * @param uid 当前用户UID
     * @param receiveUid 对方用户UID
     */
    Long addFriend(Long uid, Long receiveUid);
}
