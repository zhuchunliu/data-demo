package com.person.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darren on 2018-06-27
 **/
@Configuration
@EnableJpaRepositories(basePackages="com.person.demo.repository",repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class JpaConfig{

    @Resource(name = "masterDataSource")
    private DataSource masterDataSource;

    @Resource(name = "slaveDataSource")
    private DataSource slaveDataSource;

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factory,
//                                                                       @Qualifier(value = "routingDataSource") DataSource dataSource,
//                                                                       JpaProperties properties) {
//        Map<String, Object> jpaProperties = new HashMap<String, Object>();
//        jpaProperties.putAll(properties.getHibernateProperties(dataSource));
//        return factory.dataSource(dataSource).packages("com.person.demo.entity").properties(jpaProperties).build();
//    }



    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(value = "routingDataSource") DataSource dataSource,
                                                                       JpaVendorAdapter adapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPackagesToScan("com.person.demo.entity");
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(adapter);
        return bean;
    }

    @Bean("routingDataSource")//必须 创建 bean
    public AbstractRoutingDataSource getRoutingDataSource() throws Exception{
        AbstractRoutingDataSource proxy = new RoutingDataSource();

        Map<Object, Object> targetDataSources  = new HashMap<Object,Object>();
        targetDataSources.put(DbContextHolder.DbType.master,masterDataSource);
        targetDataSources.put(DbContextHolder.DbType.slave,slaveDataSource);
        proxy.setTargetDataSources(targetDataSources);//设置数据源,获取connection用
        proxy.setDefaultTargetDataSource(masterDataSource);
        return proxy;
    }


}
