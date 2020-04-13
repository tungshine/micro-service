package com.tanglover.mall.service.mapper.provider;

import com.tanglover.mall.bean.Product;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2020-04-05 14:48
 * @description:
 */
public class MemberProvider extends SQL {
    public String getMemberList(Map<String, Object> conditions) {
        return new SQL() {
            {
                this.SELECT("*");
                this.FROM("member");
                conditions.forEach((key, value) -> {
                    this.WHERE(key + " =#{" + key + "}");
                });
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
                UPDATE("Member");
                SET("version = version + 1");
                SET("stock = stock - #{stock}");
                if (0 != product.getModify_time()) {
                    SET("modify_time = #{modify_time}");
                }
                WHERE("id = #{id}", "version = #{version}");
            }
        }.toString();
    }

}