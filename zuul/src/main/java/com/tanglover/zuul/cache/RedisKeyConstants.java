package com.tanglover.zuul.cache;

import org.springframework.util.StringUtils;

import java.util.StringJoiner;

/**
 * @Author: TangXu
 * @Date: 2019/07/22 15:31
 * @Description: 记录redis KEY定义
 */
public class RedisKeyConstants {

    private static String IP_FORBIDDEN_EMAIL_PREFIX = "ip_forbidden_email";

    public static String getIpForbiddenEmailPrefix() {
        return IP_FORBIDDEN_EMAIL_PREFIX;
    }

    /**
     * 获取缓存KEY
     *
     * @param prefix_key：前缀key
     * @param suffix_key：后缀key
     * @return
     */
    public static String getCacheKey(String prefix_key, String suffix_key) {
        if (StringUtils.hasLength(prefix_key) && StringUtils.hasLength(suffix_key)) {
            StringJoiner sj = new StringJoiner(":");
            return sj.add(prefix_key).add(suffix_key).toString();
        }
        return null;
    }


}
