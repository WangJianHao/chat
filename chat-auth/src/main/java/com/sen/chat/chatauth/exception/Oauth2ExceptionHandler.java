package com.sen.chat.chatauth.exception;

import com.sen.chat.common.api.SenCommonResponse;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 12:56
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {
    @ExceptionHandler(value = OAuth2Exception.class)
    public SenCommonResponse<?> handleOauth2(OAuth2Exception e) {
        return SenCommonResponse.failed(e.getMessage());
    }
}
