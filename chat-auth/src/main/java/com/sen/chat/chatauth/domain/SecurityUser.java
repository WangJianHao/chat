package com.sen.chat.chatauth.domain;

import com.sen.chat.common.constant.dict.UserStatusEnum;
import com.sen.chat.common.domain.UserDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 12:48
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * 登录ID
     */
    private Long uid;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 登录客户端ID
     */
    private String clientId;
    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser() {

    }

    public SecurityUser(UserDTO userDto) {
        this.setUid(userDto.getUid());
        this.setUserName(userDto.getUserName());
        this.setPassword(userDto.getPassword());
        this.setEnabled(UserStatusEnum.ENABLE.getCode().compareTo(userDto.getStatus()) == 0);
        this.setClientId(userDto.getClientId());
        if (userDto.getRoles() != null) {
            authorities = new ArrayList<>();
            userDto.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.uid.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
