package com.tanglover.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-07-22 14:41
 * @description:
 */
@ConfigurationProperties
@Component
@RefreshScope
public class Configuration {

    /**
     * 禁用的IP段功能开关 0:开启 1：关闭
     */
    @Value("${forbidden_ips_state}")
    private String FORBIDDEN_IPS_STATE;

    /**
     * 不需要被禁用的IP段或者IP--IP白名单,中间以逗号分隔
     */
    @Value("${allow_ips}")
    private String ALLOW_IPS;

    /**
     * 禁用的IP段
     */
    @Value("${forbidden_ips}")
    private String FORBIDDEN_IPS;

    /**
     * 禁用的次数颈线阈值
     */
    @Value("${forbidden_ips_count}")
    private String FORBIDDEN_IPS_COUNT;

    /**
     * 禁用的邮件IP的缓存KEY的失效时长 量纲为 秒
     */
    @Value("${forbidden_ips_time}")
    private String FORBIDDEN_IPS_TIME;


    /**
     * 对那些接口进行IP检查 以逗号分隔 举例：email_register,email_bind
     */
    @Value("${forbidden_ips_uri}")
    private String FORBIDDEN_IPS_URI;


    /**
     * 对那些接口传递了邮箱的进行邮件正则表达式检查,多个正则表达式以逗号分隔
     */
    @Value("${forbidden_email_pattern:}")
    private String FORBIDDEN_EMAIL_PATTERN;

    /**
     * 对那些接口传递了邮箱的进行邮件后缀检查,多个后缀以逗号分隔
     */
    @Value("${allow_email_suffixes:}")
    private String ALLOW_EMAIL_SUFFIXES;

    public String getFORBIDDEN_IPS_STATE() {
        return FORBIDDEN_IPS_STATE;
    }

    public String getALLOW_IPS() {
        return ALLOW_IPS;
    }

    public String getFORBIDDEN_IPS() {
        return FORBIDDEN_IPS;
    }

    public String getFORBIDDEN_IPS_COUNT() {
        return FORBIDDEN_IPS_COUNT;
    }

    public String getFORBIDDEN_IPS_TIME() {
        return FORBIDDEN_IPS_TIME;
    }

    public String getFORBIDDEN_IPS_URI() {
        return FORBIDDEN_IPS_URI;
    }

    public String getFORBIDDEN_EMAIL_PATTERN() {
        return FORBIDDEN_EMAIL_PATTERN;
    }

    public String getALLOW_EMAIL_SUFFIXES() {
        return ALLOW_EMAIL_SUFFIXES;
    }
}
