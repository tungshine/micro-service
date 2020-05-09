package com.tanglover.security.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TangXu
 * @create 2020-05-09 15:34
 * @description: Feign文件上传配置类
 */
@Configuration
public class FeignSupportConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}