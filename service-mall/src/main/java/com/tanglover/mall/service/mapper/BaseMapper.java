package com.tanglover.mall.service.mapper;

import com.tanglover.mall.service.mapper.provider.BaseSqlProvider;
import org.apache.ibatis.annotations.*;

public interface BaseMapper<T> {
    //新增一条数据
    @InsertProvider(method = "add", type = BaseSqlProvider.class)
    @Options(useGeneratedKeys = true)
    int add(T bean);

    //根据主键删除一条数据
    @DeleteProvider(method = "delete", type = BaseSqlProvider.class)
    int delete(T bean);

    //根据主键获取一条数据
    @SelectProvider(method = "get", type = BaseSqlProvider.class)
    T get(T bean);

    //修改一条数据
    @UpdateProvider(method = "update", type = BaseSqlProvider.class)
    int update(T bean);
}
