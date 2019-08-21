package com.tanglover.sso.config.authentication;

import com.tanglover.sso.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author TangXu
 * @create 2019-08-21 14:09
 * @description: 登录失败处理
 */
@Component
public class RequestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    static final Logger logger = LoggerFactory.getLogger(RequestAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        String result = "";
        logger.error(e.getClass().getName() + "===>" + e.getMessage());
        if (e instanceof UsernameNotFoundException) {
            result = Result.returnFailureString("用戶未注册，登录失败！", 10000);
        } else if (e instanceof DisabledException) {
            result = Result.returnFailureString("账户被禁用，登录失败！", 10000);
        } else if (e instanceof BadCredentialsException) {
            result = Result.returnFailureString("密码错误，登录失败！", 10000);
        } else {
            result = Result.returnFailureString("登录失败！", 10000);
        }
        out.write(result);
        out.flush();
        out.close();
    }
}