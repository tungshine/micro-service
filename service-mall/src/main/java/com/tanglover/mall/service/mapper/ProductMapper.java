package com.tanglover.mall.service.mapper;

import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.provider.ProductProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

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
    @Select("SELECT * FROM product")
    Product selectByKey(@Param("id") Long id);

    /**
     * 修改产品
     *
     * @param product
     * @return
     */
    @UpdateProvider(type = ProductProvider.class, method = "update")
    long updateProduct(Product product);

}