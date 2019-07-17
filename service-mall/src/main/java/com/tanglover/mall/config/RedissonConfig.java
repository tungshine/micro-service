package com.tanglover.mall.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author TangXu
 * @create 2019-07-17 13:40
 * @description:
 */
@Configuration
public class RedissonConfig {

    /**
     * https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
     * * // 加锁以后10秒钟自动解锁
     * * // 无需调用unlock方法手动解锁
     * * lock.lock(10, TimeUnit.SECONDS);
     * *
     * * // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
     * * boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
     * * if (res) {
     * *    try {
     * *      ...
     * *    } finally {
     * *        lock.unlock();
     * *    }
     * * }
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + RedisConfig.REDIS_HOST + ":" + RedisConfig.REDIS_PORT)
                .setPassword(RedisConfig.REDIS_PASSWORD);
        return Redisson.create(config);
    }
}