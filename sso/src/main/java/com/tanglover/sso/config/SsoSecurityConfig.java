package com.tanglover.sso.config;

import com.tanglover.sso.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author TangXu
 * @create 2019-08-15 17:33
 * @description:
 */
@Configuration
public class SsoSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(SsoSecurityConfig.class);

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 使用自定义userService验证用户
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("自定义userService验证用户");
        auth.userDetailsService(userDetailsService()).passwordEncoder(null);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/")
                .successHandler(null)
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        PrintWriter out = response.getWriter();
                        String result = null;
                        if (e instanceof UsernameNotFoundException) {
                            result = Result.returnSuccessString("用户名或密码错误，登录失败！", 10000, "");
                        } else if (e instanceof DisabledException) {
                            result = Result.returnSuccessString("账户被禁用，登录失败！", 10000, "");
                        } else {
                            result = Result.returnSuccessString("登录失败！", 10000, "");
                        }
                        out.write(result);
                        out.flush();
                        out.close();
                    }
                })
                .and().httpBasic()
                .and().authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilter(null);
    }
}