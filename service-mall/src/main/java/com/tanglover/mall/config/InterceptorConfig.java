package com.tanglover.mall.config;

import com.tanglover.mall.config.interceptor.TimestampInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TangXu
 * @create 2019-11-22 14:23
 * @description:
 */
//@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timestampInterceptor());
    }

    @Bean
    public TimestampInterceptor timestampInterceptor() {
        return new TimestampInterceptor();
    }

}