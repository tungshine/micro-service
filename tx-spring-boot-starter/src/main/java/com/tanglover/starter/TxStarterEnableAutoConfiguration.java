package com.tanglover.starter;

import com.tanglover.starter.config.HelloServiceProperties;
import com.tanglover.starter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TangXu
 * @create 2019-06-24 11:44
 * @description:
 */
@Configuration
@ConditionalOnClass(HelloServiceProperties.class)
@EnableConfigurationProperties(HelloServiceProperties.class)
public class TxStarterEnableAutoConfiguration {

    private final HelloServiceProperties helloServiceProperties;

    @Autowired
    public TxStarterEnableAutoConfiguration(HelloServiceProperties helloServiceProperties) {
        this.helloServiceProperties = helloServiceProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "hello.service", name = "enable", havingValue = "true")
    HelloService helloService() {
        return new HelloService(helloServiceProperties.getPrefix(), helloServiceProperties.getSuffix());
    }
}