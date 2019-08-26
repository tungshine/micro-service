package com.tanglover.security.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;
import java.util.Properties;

/**
 * @author TangXu
 * @create 2019-05-27 10:36
 * @description:
 */
//@Configuration
public class DBConfig {

    @Bean
    @Primary
    public AtomikosDataSourceBean master(Environment env) {
        AtomikosDataSourceBean ads = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.master.");
        ads.setXaDataSourceClassName("com.alibaba.druid.pool.DruidDataSource");
        ads.setUniqueResourceName("master");
        ads.setPoolSize(5);
        ads.setXaProperties(prop);
        return ads;
    }

    @Bean
    public AtomikosDataSourceBean slave(Environment env) {
        AtomikosDataSourceBean ads = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.slave.");
        ads.setXaDataSourceClassName("com.alibaba.druid.pool.DruidDataSource");
        ads.setUniqueResourceName("slave");
        ads.setPoolSize(5);
        ads.setXaProperties(prop);
        return ads;
    }

    private Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driverClassName", ""));
        prop.put("initialSize", env.getProperty(prefix + "initialSize", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "maxActive", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "maxWait", Integer.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements", Boolean.class));

        prop.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));

        prop.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));
        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("timeBetweenEvictionRunsMillis", env.getProperty(prefix + "timeBetweenEvictionRunsMillis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
        return prop;
    }

    @Bean
    public JtaTransactionManager jtaTransactionManager() {
        UserTransactionManager transactionManager = new UserTransactionManager();
        UserTransaction transaction = new UserTransactionImp();
        return new JtaTransactionManager(transaction, transactionManager);
    }
}