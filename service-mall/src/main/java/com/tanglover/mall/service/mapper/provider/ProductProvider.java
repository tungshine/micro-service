package com.tanglover.mall.service.mapper.provider;

import com.tanglover.mall.bean.Product;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-07-16 15:21
 * @description:
 */
public class ProductProvider<ProductProvider> extends SQL {

    public String getProductList(Map<String, Object> conditions) {
        return new SQL() {
            {
                this.SELECT("*");
                this.FROM("product");
                conditions.forEach((key, value) -> {
                    this.WHERE(key + " =#{" + key + "}");
                });
//                this.WHERE(buildConditions(conditions));
            }
        }.toString();
    }

    public List<String> buildConditions(Map<String, Object> conditions) {
        List<String> sqls = new ArrayList<String>();
        conditions.forEach((key, value) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key).append("=#{").append(key).append("}");
            sqls.add(stringBuilder.toString());
        });
        return sqls;
    }

    public String update(Product product) {
        return new SQL() {
            {
                UPDATE("Product");
                SET("version = version + 1");
                SET("stock = stock - #{stock}");
                if (0 != product.getModify_time()) {
                    SET("modify_time = #{modify_time}");
                }
                WHERE("id = #{id}", "version = #{version}");
            }
        }.toString();
    }

    public String increaseProductStock(Product product) {
        return new SQL() {
            {
                UPDATE("Product");
                SET("version = version + 1");
                SET("stock = stock + #{stock}");
                if (0 != product.getModify_time()) {
                    SET("modify_time = #{modify_time}");
                }
                WHERE("id = #{id}", "version = #{version}");
            }
        }.toString();
    }
}