package com.tanglover.security.mybatis.mapper;

import com.tanglover.security.bean.SysRole;
import com.tanglover.security.config.authentication.SecurityUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TangXu
 * @create 2019-08-21 15:20
 * @description:
 */
@Mapper
public interface UserMapper {

    /**
     * 通过用户名查询帐号
     *
     * @param username
     * @return
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SecurityUser loadByUsername(@Param("username") String username);

}