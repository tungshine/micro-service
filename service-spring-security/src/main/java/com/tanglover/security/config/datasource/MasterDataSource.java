package com.tanglover.security.config.datasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author TangXu
 * @create 2019-05-27 10:44
 * @description:
 */
public class MasterDataSource {

    public @Bean
    DataSource masterDataSource() {
        DataSourceBuilder.create().build();
        new DruidXADataSource();
        return DataSourceBuilder.create().build();
    }
}