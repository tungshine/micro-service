package com.tanglover.security.config.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author TangXu
 * @create 2019-08-21 15:22
 * @description:
 */
public class SecurityUser implements UserDetails, Serializable {

    private long sys_user_id;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public long getSys_user_id() {
        return sys_user_id;
    }

    public void setSys_user_id(long sys_user_id) {
        this.sys_user_id = sys_user_id;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}