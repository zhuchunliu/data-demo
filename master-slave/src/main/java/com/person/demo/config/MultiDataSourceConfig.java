package com.person.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 配置多数据源
 *
 * Created by Darren on 2018-06-25
 **/
@Configuration
public class MultiDataSourceConfig {

    @ConfigurationProperties(prefix = "druid.master")
    @Primary //不加的话DataSourceAutoConfiguration初始化报错
    @Bean("masterDataSource")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @ConfigurationProperties(prefix = "druid.slave")
    @Bean("slaveDataSource")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }
}
