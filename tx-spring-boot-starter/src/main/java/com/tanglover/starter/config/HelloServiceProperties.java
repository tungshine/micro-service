package com.tanglover.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author TangXu
 * @create 2019-06-24 17:11
 * @description:
 */
@ConfigurationProperties("hello.service")
public class HelloServiceProperties {

    private String prefix;

    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}