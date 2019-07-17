package com.tanglover.lock.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author TangXu
 * @create 2019-07-15 16:23
 * @description:
 */
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean lock(String key, String value) {
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(currentValue)) {
            return true;
        }
        return false;
    }

    public int updateLock() {
        final String key = "";
        return 0;
    }
}