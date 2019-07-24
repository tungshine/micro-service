package com.tanglover.mall.service.mapper;

import com.tanglover.mall.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author TangXu
 * @create 2019-07-23 16:07
 * @description:
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectByKey(@Param("id") Long id);
}