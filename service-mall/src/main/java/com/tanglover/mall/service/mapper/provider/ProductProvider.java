package com.tanglover.mall.service.mapper.provider;

import com.tanglover.mall.bean.Product;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author TangXu
 * @create 2019-07-16 15:21
 * @description:
 */
public class ProductProvider extends SQL {

    public String update(Product product) {
        return new SQL() {
            {
                UPDATE("Product");
                SET("version = #{version} + 1");
//                if (0 != product.getStock()) {
//                    SET("stock = #{stock}");
//                }
                SET("stock = #{stock}");
                if (0 != product.getModify_time()) {
                    SET("modify_time = #{modify_time}");
                }
                WHERE("id = #{id}", "version = #{version}");
            }
        }.toString();
    }
}