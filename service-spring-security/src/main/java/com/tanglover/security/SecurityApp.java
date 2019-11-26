package com.tanglover.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author TangXu
 * @create 2019-05-15 11:11
 * @description:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableDiscoveryClient
public class SecurityApp {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApp.class);
    }
}