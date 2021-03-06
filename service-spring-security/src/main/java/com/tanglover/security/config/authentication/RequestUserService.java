package com.tanglover.security.config.authentication;

import com.tanglover.security.bean.SysRole;
import com.tanglover.security.mybatis.mapper.RoleMapper;
import com.tanglover.security.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-08-21 14:31
 * @description:
 */
@Service
public class RequestUserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser = userMapper.loadByUsername(username);
        if (null == securityUser) {
            throw new UsernameNotFoundException("用戶名错误");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("sys_user_id", securityUser.getSys_user_id());
        List<SysRole> sysRoles = roleMapper.userRoles(conditions);
        sysRoles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        securityUser.setAuthorities(authorities);
//        User user = new User(securityUser.getUsername(), new BCryptPasswordEncoder().encode(securityUser.getPassword()), authorities);
        return securityUser;
    }
}