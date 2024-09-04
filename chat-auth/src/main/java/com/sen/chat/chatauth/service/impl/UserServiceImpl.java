package com.sen.chat.chatauth.service.impl;

import com.sen.chat.chatauth.constant.MessageConstant;
import com.sen.chat.chatauth.domain.SecurityUser;
import com.sen.chat.chatauth.feign.UserFeign;
import com.sen.chat.common.constant.AuthConstant;
import com.sen.chat.common.domain.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理业务类
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 12:57
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");
        UserDTO userDto = null;
        if (AuthConstant.ADMIN_CLIENT_ID.equals(clientId)) {
            //管理端暂时没实现
            userDto = userFeign.loadAdminUserByUsername(username);
        } else {
            userDto = userFeign.loadUserByUsername(Long.valueOf(username));
        }
        if (userDto == null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        userDto.setClientId(clientId);
        SecurityUser securityUser = new SecurityUser(userDto);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }
}
