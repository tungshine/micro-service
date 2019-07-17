package com.tanglover.mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author TangXu
 * @create 2019-07-17 13:42
 * @description:
 */
@Component
@RefreshScope
public class RedisConfig {


    public static String REDIS_HOST = "redis.tanglover.cn";
    public static String REDIS_PORT = "6379";
    public static String REDIS_PASSWORD = "tangxu!@#123`";

    @Value("${spring.redis.host}")
    public void setRedisHost(String redisHost) {
        REDIS_HOST = redisHost;
    }

    @Value("${spring.redis.port}")
    public void setRedisPort(String redisPort) {
        REDIS_PORT = redisPort;
    }

    @Value("${spring.redis.password}")
    public void setRedisPassword(String redisPassword) {
        REDIS_PASSWORD = redisPassword;
    }
}