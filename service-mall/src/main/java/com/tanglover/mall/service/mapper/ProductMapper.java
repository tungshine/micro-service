package com.tanglover.mall.service.mapper;

import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.provider.ProductProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-07-16 15:17
 * @description:
 */
@Mapper
public interface ProductMapper {

    /**
     * 通过主键查询产品信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product selectByKey(@Param("id") Long id);

    /**
     * 获取产品列表
     *
     * @param conditions
     * @return
     */
    @SelectProvider(type = ProductProvider.class, method = "getProductList")
    List<Product> getProductList(Map<String, Object> conditions);

    /**
     * 修改产品
     *
     * @param product
     * @return
     */
    @UpdateProvider(type = ProductProvider.class, method = "update")
    long updateProduct(Product product);

    /**
     * 增加库存
     *
     * @param product
     * @return
     */
    @UpdateProvider(type = ProductProvider.class, method = "increaseProductStock")
    long increaseProductStock(Product product);

}