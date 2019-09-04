package com.tanglover.zuul.cache;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author TangXu
 * @create 2019-07-22 15:53
 * @description:
 */
@Component
public class RedisCache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    /**
     * 任务执行线程数200，队列缓冲数2000
     */
    private static ExecutorService exec = new ThreadPoolExecutor(20, 100, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue(10000));

    /**
     * redis过期时间,默认30分钟,设置为7天
     */
    private final static long EXPIRE_TIME_IN_SECONDS = 60 * 60 * 24 * 7;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 将对象转换成json格式进行缓存
     *
     * @param cacheable
     */
    public void cacheObject(ICacheable cacheable) {
        //调整缓存时间
        this.cacheObject(cacheable, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 将对象转换成json格式进行缓存
     *
     * @param cacheable
     * @param EXPIRE_TIME_IN_SECONDS : 过期时间 单位 秒
     */
    public void cacheObject(ICacheable cacheable, long EXPIRE_TIME_IN_SECONDS) {
        //调整缓存时间
        this.cacheObject(cacheable, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 将对象转换成json格式进行缓存
     *
     * @param cacheable
     */
    public void cacheObject(ICacheable cacheable, Long expireSeconds, TimeUnit timeunit) {
        this.putObject(cacheable.cacheKey(), cacheable, expireSeconds, timeunit);
    }

    /**
     * Get cached query result from redis
     *
     * @param key
     * @return
     */
    public <T> T getObject(Object key, Class<T> cl) {
        Object value = this.getObject(key);
        if (value instanceof String) {
            try {
                return JSONObject.parseObject(value + "", cl);
            } catch (Exception e) {
                return null;
            }
        }
        return (T) value;
    }

    /**
     * 删除缓存对象
     */
    public void delete(ICacheable cacheable) {
        this.delete(cacheable.cacheKey());
    }

    /**
     * 删除缓存对象
     *
     * @param key
     */
    public void delete(String key) {
        this.removeObject(key);
    }

    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     */
    public void putObject(Object key, Object value) {
        putObject(key, value, EXPIRE_TIME_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     * @expireSeconds 过期时间(秒)
     */
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value, Long expireSeconds, TimeUnit timeunit) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (value == null) {// 设置控制直接remove掉
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
                    if (expireSeconds == null || timeunit == null) {
                        opsForValue.set(key, value);
                    } else {
                        opsForValue.set(key, value, expireSeconds, timeunit);
                    }
                    logger.debug("Put query result to redis");
                } catch (Throwable t) {
                    logger.error("Redis put failed", t);
                }
            }
        });
    }

    /**
     * Get cached query result from redis
     *
     * @param key
     * @return
     */
    public Object getObject(Object key) {
        try {
            ValueOperations opsForValue = redisTemplate.opsForValue();
            logger.debug("Get cached query result from redis");
            Object obj = opsForValue.get(key);
            if (obj instanceof String) {
                return obj.toString();
            }
            return obj;
        } catch (Throwable t) {
            logger.error("Redis get failed, fail over to db", t);
        }
        return null;
    }

    /**
     * Remove cached query result from redis
     *
     * @param key
     * @return
     */
    private void removeObject(Object key) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    redisTemplate.delete(key);
                    logger.debug("Remove cached query result from redis");
                } catch (Throwable t) {
                    logger.error("Redis remove failed", t);
                }
            }
        });

    }

    /**
     * Clears this cache instance
     */
    public void clear() {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                redisTemplate.execute((RedisCallback) connection -> {
                    connection.flushDb();
                    return null;
                });
                logger.debug("Clear all the cached query result from redis");
            }
        });

    }

}