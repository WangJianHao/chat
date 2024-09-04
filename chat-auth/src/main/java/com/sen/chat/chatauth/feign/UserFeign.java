package com.sen.chat.chatauth.feign;

import com.sen.chat.common.domain.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 13:00
 */
@FeignClient("chat-server")
public interface UserFeign {

    @GetMapping("/user/loadAdminByUsername")
    UserDTO loadAdminUserByUsername(@RequestParam String username);


    @GetMapping("/user/loadUserByUid")
    UserDTO loadUserByUsername(@RequestParam Long uid);
}
