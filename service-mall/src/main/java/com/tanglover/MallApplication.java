package com.tanglover;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author TangXu
 * @create 2019-07-09 10:58
 * @description:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableDiscoveryClient
@EnableFeignClients
//@EnableCircuitBreaker
//@MapperScan("com.tanglover.mall.service.mapper")
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

}
