package com.tanglover.security.mybatis.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author TangXu
 * @create 2019-09-18 15:18
 * @description:
 */
public class RoleProvider extends SQL {

    public String userRoles(Long sys_user_id) {
        return new SQL() {
            {
                SELECT("sys_role.sys_role_id, sys_role.`name`");
                FROM("sys_user_role");
                LEFT_OUTER_JOIN("sys_role ON sys_user_role.sys_role_id = sys_role.sys_role_id");
                WHERE("sys_user_role.sys_user_id = #{sys_user_id}");
            }
        }.toString();
    }
}