## 项目 master-slave

#### 环境：mysql、mybatis、springboot 、tkmapper

#### 1、通过MultiDataSourceConfig 创建多数据源
指定@Primary初始数据库，以防报错

#### 2、通过RoutingDataSource 实现数据库路由
实现AbstractRoutingDataSource接口，determineCurrentLookupKey方法用来指定路由名称
```
    @Override
    protected Object determineCurrentLookupKey() {
        //此处的返回值，为targetDataSources设置的多数据源Map对象的key值
        return DbContextHolder.getDbType();
    }
```

#### 3、通过MybatisConfig类，创建SqlSessionFactoryBean对象，加载路由数据，指定默认主数据

```
    @Bean("routingDataSource")//必须 创建 bean
    public AbstractRoutingDataSource getRoutingDataSource() throws Exception{
        AbstractRoutingDataSource proxy = new RoutingDataSource();

        Map<Object, Object> targetDataSources  = new HashMap<Object,Object>();
        targetDataSources.put(DbContextHolder.DbType.master,masterDataSource);
        targetDataSources.put(DbContextHolder.DbType.slave,slaveDataSource);
        proxy.setTargetDataSources(targetDataSources);//设置数据源
        proxy.setDefaultTargetDataSource(masterDataSource);
        return proxy;
    }
```

#### 4、实现mybatis拦截器，DateSourceSelectInterceptor
实现Interceptor接口：通过plugin方法过滤需要代理的对象；intercept为代理调用方法,事物方法使用master库，非事物方法
根据类型，选择master或slave库

#### 注意事项
需要在mybatis配置文件中配置plugin:
```
<plugins>
   <plugin interceptor="com.person.demo.config.DateSourceSelectInterceptor"></plugin>
</plugins>
```

手动创建SqlSessionFactoryBean对象，需要手动配置MapperLocations和ConfigLocation
,application.yml配置的属性，不会自动加载:
```
SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
factory.setDataSource(this.getRoutingDataSource());
factory.setMapperLocations(this.getResource("mapping", "**/*.xml"));
factory.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
factory.setFailFast(true);
```