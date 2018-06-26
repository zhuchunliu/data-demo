package com.person.demo.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darren on 2018-06-25
 **/
@Configuration
public class MybatisConfig  {

    @Resource(name = "masterDataSource")
    private DataSource masterDataSource;

    @Resource(name = "slaveDataSource")
    private DataSource slaveDataSource;

    /**
     * 手动创建SqlSessionFactoryBean对象，需要手动配置MapperLocations和ConfigLocation
     * application.yml配置的属性，不会自动加载
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(this.getRoutingDataSource());
        factory.setMapperLocations(this.getResource("mapping", "**/*.xml"));
        factory.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
        factory.setFailFast(true);

        return factory;
    }

    /**
     * 装配路由数据源
     * @return
     * @throws Exception
     */
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

    /**
     * 仿照SpringBootVFS实现
     * @param basePackage
     * @param pattern
     * @return
     * @throws IOException
     */
    public org.springframework.core.io.Resource[] getResource(String basePackage, String pattern) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(basePackage)) + "/" + pattern;
        org.springframework.core.io.Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
        return resources;
    }
}
