package com.tanglover.mall.service;

import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-07-19 14:45
 * @description:
 */
@Service
public class ProductService {

    static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductMapper productMapper;

    public Map<String, Object> increaseStock(long productId, long stock) {
        Map<String, Object> retMap = new HashMap<>();
        Product product = productMapper.selectByKey(productId);
        product.setStock(stock);
        product.setModify_time(System.currentTimeMillis());
        long l = productMapper.increaseProductStock(product);
        LOGGER.info("增加库存=====>, {}", l);
        return retMap;
    }

    public Map<String, Object> reduceStock(long productId, long reduceStock) {
        Map<String, Object> retMap = new HashMap<>();
        Product product = productMapper.selectByKey(productId);
        product.setStock(reduceStock);
        product.setModify_time(System.currentTimeMillis());
        long l = productMapper.updateProduct(product);
        LOGGER.info("减少库存=====>, {}", l);
        return retMap;
    }
}