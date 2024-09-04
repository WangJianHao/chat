package com.sen.chat.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.sen.chat.common.constant.errorcode.ResultCodeEnum;
import com.sen.chat.common.constant.AuthConstant;
import com.sen.chat.common.constant.MDCKey;
import com.sen.chat.common.domain.UserDTO;
import com.sen.chat.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 16:18
 */
@Order(-2)
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户登录token
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(userStr)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        //从token中获取通用用户信息
        UserDTO userDto = JSONUtil.toBean(userStr, UserDTO.class);
        Long uid = userDto.getUid();
        MDC.put(MDCKey.UID, uid.toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(MDCKey.UID);
    }

}
