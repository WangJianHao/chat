package com.sen.chat.chatserver.feign;

import com.sen.chat.chatserver.dto.Oauth2TokenDto;
import com.sen.chat.common.api.SenCommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 认证服务远程调用
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 19:05
 */
@FeignClient("chat-auth")
public interface AuthFeign {

    @PostMapping(value = "/oauth/token")
    SenCommonResponse<Oauth2TokenDto> getAccessToken(@RequestParam Map<String, String> parameters);
}
