package com.tanglover.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
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

    public List<Product> products(JSONObject conditions) {
        Map<String, Object> conditionMap = new HashMap<>();
        String product_no = conditions.getString("product_no");
        long stock = conditions.getLongValue("stock");
        int pageNo = conditions.getIntValue("pageNo");
        int pageSize = conditions.getIntValue("pageSize");
        pageNo = (0 == pageNo) ? 1 : pageNo;
        pageSize = (0 == pageSize) ? 10 : pageSize;
        if (!StringUtils.isEmpty(product_no)) {
            conditionMap.put("product_no", product_no);
        }
        if (0 != stock) {
            conditionMap.put("stock", stock);
        }
        PageHelper.startPage(pageNo, pageSize);
        List<Product> productList = productMapper.getProductList(conditionMap);
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        long total = pageInfo.getTotal();
        System.out.println(total);
        return productList;
    }
}