package com.tanglover.sso.config.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author TangXu
 * @create 2019-08-21 14:21
 * @description:
 */
public class RequestAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取表单输入返回的用户名和密码
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}