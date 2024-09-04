package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.service.ContactService;
import com.sen.chat.common.api.SenCommonResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/29 21:55
 */
@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    /**
     * 搜好友或所在群组
     *
     * @param searchText
     * @return
     */
    @GetMapping("/searchContact")
    public SenCommonResponse<?> searchContact(@NotEmpty String searchText) {
        return SenCommonResponse.success(contactService.searchContact(searchText));
    }

    /**
     * 根据UID或群组ID搜索
     *
     * @param id 目标ID
     */
    @GetMapping("/search")
    public SenCommonResponse<?> search(@NotNull Long id) {
        return SenCommonResponse.success(contactService.search(id));
    }



}
