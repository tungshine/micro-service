package com.tanglover.mall.service.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author TangXu
 * @create 2019-07-23 16:03
 * @description:
 */
public class UserProvider extends SQL {

    public String getUserList() {
        return new SQL() {
            {
                SELECT("");
                FROM("");

            }
        }.toString();
    }
}