package com.tanglover.security.mybatis.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * @author TangXu
 * @create 2019-09-18 15:18
 * @description:
 */
public class RoleProvider extends SQL {

    public String userRoles(Map<String, Object> conditions) {
        return new SQL() {
            {
                SELECT("sys_role.sys_role_id, sys_role.`name`");
                FROM("sys_user_role");
                LEFT_OUTER_JOIN("sys_role ON sys_user_role.sys_role_id = sys_role.sys_role_id");
                if (0 < conditions.size()) {
                    conditions.forEach((key, value) -> {
                        WHERE(key + " =#{" + key + "}");
                    });
                }
            }
        }.toString();
    }
}