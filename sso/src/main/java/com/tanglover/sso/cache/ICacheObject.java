package com.tanglover.sso.cache;

import java.io.Serializable;

/**
 * @author TangXu
 * @create 2019-05-15 14:38
 * @description: 代表可缓存对象, 用于手动进行缓存操作
 */
public interface ICacheObject<T> extends Serializable {

    /**
     * 获取加了前缀标识的键
     *
     * @return
     */
    public String cacheKey();
}
