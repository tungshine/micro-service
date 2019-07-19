package com.tanglover.mall.service;

import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.ProductMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author TangXu
 * @create 2019-07-16 15:10
 * @description:
 */
@Service
public class UserService {

    static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ProductMapper productMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    public Map<String, Object> grabForRedission(Long userId, Long productId, Long count) {
        String key = userId + "_" + productId + "_";
        RLock lock = redissonClient.getLock(key);
        try {
            boolean isLock = lock.tryLock(3, 3, TimeUnit.SECONDS);
            if (isLock) {

            }
        } catch (Exception e) {
            LOGGER.error("redission异常：{}", e.getMessage());
        }
        Product product = productMapper.selectByKey(productId);
        productMapper.updateProduct(product);
        return null;
    }

    public Map<String, Object> grabForRedis(Long userId, Long productId, Long count) {
        Map<String, Object> retMap = new HashMap<>();
        String key = "user_id_" + userId + "_product_id_" + productId;
        Boolean ifAbsent = true;

        while (ifAbsent) {
            String value = System.nanoTime() + UUID.randomUUID().toString();
            ifAbsent = redisTemplate.opsForValue().setIfAbsent(key, value);
            if (ifAbsent) {
                try {
                    ifAbsent = false;
                    Product product = productMapper.selectByKey(productId);
                    if (null != product && count <= product.getStock()) {
                        product.setStock(count);
                        product.setModify_time(System.currentTimeMillis());
                        long updateProduct = productMapper.updateProduct(product);
                        if (0 < updateProduct) {
                            LOGGER.info("update success:{}", updateProduct);
                            LOGGER.info("userId为: {} 的用户抢购成功，抢购数量为: {} 个", userId, count);
                            retMap.put("resp", "抢购成功");
                        } /*else {
                            LOGGER.error("update failed:{}", updateProduct);
                            retMap.put("resp", "抢购失败");
                        }*/
                    } /*else {
                        // TODO 记录没有抢购到记录，以便后期给该用户推送相同类产品抢购的活动
                        LOGGER.info("userId为: {} 的用户抢购失败，库存不足", userId);
                        retMap.put("resp", "无货");
                    }*/
                } catch (Exception e) {
                    LOGGER.error("update error:{}", e.getMessage());
                    retMap.put("resp", "抢购失败");
                } finally {
                    redisTemplate.delete(key);
                }

            } else {
                // TODO 记录没有抢购到记录，以便后期给该用户推送相同类产品抢购的活动
//                retMap.put("resp", "无货");
                try {
                    ifAbsent = true;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return retMap;
    }
}