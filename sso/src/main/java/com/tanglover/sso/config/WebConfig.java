package com.tanglover.sso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * @author TangXu
 * @create 2019-08-15 17:32
 * @description:
 */
@Configuration
public class WebConfig {

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}