package com.tanglover.security.mybatis.mapper;

import com.tanglover.security.bean.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author TangXu
 * @create 2019-09-17 16:57
 * @description:
 */
public interface RoleMapper {

    /**
     * 通过角色id查询角色
     *
     * @param sys_role_id
     * @return
     */
    @Select("SELECT * FROM sys_role WHERE sys_role_id = #{sys_role_id}")
    SysRole loadByRoleId(@Param("sys_role_id") long sys_role_id);
}