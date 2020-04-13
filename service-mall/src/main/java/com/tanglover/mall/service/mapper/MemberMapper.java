package com.tanglover.mall.service.mapper;

import com.tanglover.mall.bean.Member;
import com.tanglover.mall.bean.Product;
import com.tanglover.mall.service.mapper.provider.MemberProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author TangXu
 * @create 2020-04-05 14:47
 * @description:
 */
@Mapper
public interface MemberMapper extends BaseMapper {

    /**
     * 通过主键查询会员信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM member WHERE id = #{id}")
    Member selectByKey(@Param("id") Long id);

    /**
     * 获取会员列表
     *
     * @param conditions
     * @return
     */
    @SelectProvider(type = MemberProvider.class, method = "getMemberList")
    List<Member> getMemberList(Map<String, Object> conditions);

    /**
     * 修改会员
     *
     * @param product
     * @return
     */
    @UpdateProvider(type = MemberProvider.class, method = "update")
    long updateMember(Product product);

}