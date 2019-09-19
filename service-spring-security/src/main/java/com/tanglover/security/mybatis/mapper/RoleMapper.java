package com.tanglover.security.mybatis.mapper;

import com.tanglover.security.bean.SysRole;
import com.tanglover.security.mybatis.provider.RoleProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2019-09-17 16:57
 * @description:
 */
@Mapper
public interface RoleMapper {

    /**
     * 通过角色id查询角色
     *
     * @param sys_role_id
     * @return
     */
    @Select("SELECT * FROM sys_role WHERE sys_role_id = #{sys_role_id}")
    SysRole loadByRoleId(@Param("sys_role_id") long sys_role_id);

    /**
     * 查询用户角色列表
     *
     * @param conditions
     * @return
     */
    @SelectProvider(type = RoleProvider.class, method = "userRoles")
    List<SysRole> userRoles(Map<String, Object> conditions);
}