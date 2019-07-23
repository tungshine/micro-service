package com.tanglover.zuul.cache;

import java.io.Serializable;

/**
 * @Author TangXu
 * @Description 代表可缓存对象, 用于手动进行缓存操作
 * @Date 2019/7/23 13:47
 */
public interface ICacheable<T> extends Serializable {

    /**
     * 获取加了前缀标识的键
     *
     * @return
     */
    public String cacheKey();
}
