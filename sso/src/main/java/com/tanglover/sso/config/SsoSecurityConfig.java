package com.tanglover.sso.config;

import com.tanglover.sso.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(Result.returnFailureString("未登录!", 10000));
            }
        })

                .and()
                .authorizeRequests()

                .anyRequest()
                .authenticated()// 其他 url 需要身份认证

                .and()
                .formLogin()  //开启登录
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        Object principal = authentication.getPrincipal();
                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        PrintWriter out = response.getWriter();
                        Result.returnSuccessMap("登录成功!", principal);
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        PrintWriter out = response.getWriter();
                        String result = null;
                        if (e instanceof UsernameNotFoundException) {
                            result = Result.returnFailureString("用户名或密码错误，登录失败！", 10000);
                        } else if (e instanceof DisabledException) {
                            result = Result.returnFailureString("账户被禁用，登录失败！", 10000);
                        } else {
                            result = Result.returnFailureString("登录失败！", 10000);
                        }
                        out.write(result);
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()

                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        Object principal = authentication.getPrincipal();
                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                        response.getWriter().write(Result.returnSuccessString("注销成功", principal));
                    }
                })
                .permitAll();

        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(Result.returnFailureString("沒有权限！", 10000));
            }
        }); // 无权访问 JSON 格式的数据

//        http
//                .formLogin()
//                .loginPage("/loginPage")
//                .loginProcessingUrl("/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        Object principal = authentication.getPrincipal();
//                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                        PrintWriter out = response.getWriter();
//                        Result.returnSuccessMap("登录成功!", principal);
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
//                        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                        PrintWriter out = response.getWriter();
//                        String result = null;
//                        if (e instanceof UsernameNotFoundException) {
//                            result = Result.returnFailureString("用户名或密码错误，登录失败！", 10000, "");
//                        } else if (e instanceof DisabledException) {
//                            result = Result.returnFailureString("账户被禁用，登录失败！", 10000, "");
//                        } else {
//                            result = Result.returnFailureString("登录失败！", 10000, "");
//                        }
//                        out.write(result);
//                        out.flush();
//                        out.close();
//                    }
//                })
//                .and()
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .addFilter(null)
//                .rememberMe()
//        ;
    }
}