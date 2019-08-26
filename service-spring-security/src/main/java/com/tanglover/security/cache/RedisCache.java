package com.tanglover.security.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author TangXu
 * @create 2019-05-15 14:38
 * @description:
 */
@Component
public class RedisCache {

    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private static ExecutorService executor = new ThreadPoolExecutor(20, 100, 60L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(10000));

    //    @Value("expire_time_in_seconds")
    private final static long EXPIRE_TIME_IN_SECONDS = 60 * 60 * 24 * 7; //过期时间，7天

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 将对象转换成json格式进行缓存
     *
     * @param cacheObject
     */
    public void cacheObject(ICacheObject cacheObject) {
        this.cacheObject(cacheObject, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void cacheObject(ICacheObject cacheObject, long EXPIRE_TIME_IN_SECONDS) {
        this.cacheObject(cacheObject, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void cacheObject(ICacheObject cacheObject, long EXPIRE_TIME_IN_SECONDS, TimeUnit timeUnit) {
        this.putObject(cacheObject.cacheKey(), cacheObject, EXPIRE_TIME_IN_SECONDS, timeUnit);
    }

    /**
     * Put business data to redis
     *
     * @param key
     * @param value
     */
    public void pubObject(Object key, Object value) {
        this.putObject(key, value, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Put business data to redis
     *
     * @param key
     * @param value
     * @param expireSeconds
     * @param timeUnit
     */
    public void putObject(Object key, Object value, Long expireSeconds, TimeUnit timeUnit) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null == value) {
                        removeObject(key);
                        return;
                    }
                    ValueOperations opsForValue = redisTemplate.opsForValue();
                    if (value instanceof List) {
                        List list = (List) value;
                        if (list.isEmpty()) {
                            redisTemplate.delete(key);
                            return;
                        }
                    }
                    if (null == expireSeconds || null == timeUnit) {
                        opsForValue.set(key, value);
                    } else {
                        opsForValue.set(key, value, expireSeconds, timeUnit);
                    }
                    logger.debug("Put business data to redis success");
                } catch (Throwable throwable) {
                    logger.error("Put business data to redis failed", throwable);
                }
            }
        });
    }

    /**
     * Get cached business data from redis
     *
     * @param key
     */
    public Object getObject(Object key) {
        try {
            ValueOperations opsForValue = redisTemplate.opsForValue();
            logger.debug("Get cached business data from redis");
            Object o = opsForValue.get(key);
            if (o instanceof String) {
                return o.toString();
            }
            return o;
        } catch (Throwable throwable) {
            logger.error("Get cached business data failed, redis has not data");
        }
        return null;
    }

    /**
     * Remove cached business data from redis
     *
     * @param key
     */
    public void removeObject(Object key) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    redisTemplate.delete(key);
                    logger.debug("Remove cached business data from redis success");
                } catch (Throwable throwable) {
                    logger.error("Redis remove failed", throwable);
                }
            }
        });
    }
}