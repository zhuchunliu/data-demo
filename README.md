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

## 项目 master-slave-jpa

#### 1、通过MultiDataSourceConfig 创建多数据源 同master-salve项目

#### 2、通过RoutingDataSource 实现数据库路由 同master-salve项目

#### 3、通过JpaConfig类，创建LocalContainerEntityManagerFactoryBean对象，加载路由数据，指定默认主数据
##### 方式一：
```
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factory,
                                                                       @Qualifier(value = "routingDataSource") DataSource dataSource,
                                                                       JpaProperties properties) {
        Map<String, Object> jpaProperties = new HashMap<String, Object>();
        jpaProperties.putAll(properties.getHibernateProperties(dataSource));
        return factory.dataSource(dataSource).packages("com.person.demo.entity").properties(jpaProperties).build();
    }
```

##### 方式二：
```
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(value = "routingDataSource") DataSource dataSource,
                                                                       JpaVendorAdapter adapter) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setPackagesToScan("com.person.demo.entity");
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(adapter);
        return bean;
    }
```

#### 4、实现jpa处理器

##### 4.1实现后处理器 ：过滤方法，如果是查询走slave库，如果是更新方法， 走master库
```
public class CustomPostProcessor implements RepositoryProxyPostProcessor {
    @Override
    public void postProcess(ProxyFactory proxyFactory, RepositoryInformation repositoryInformation) {
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Method method = invocation.getMethod();
                DbContextHolder.DbType dbType = DbContextHolder.DbType.master;//默认设置使用主库
                boolean synchonizationActive = TransactionSynchronizationManager.isSynchronizationActive();//判断当前操作是否有事务
                if(!synchonizationActive){
                    if(0 == method.getAnnotations().length){
                        if(method.getName().startsWith("find") || method.getName().startsWith("get")){
                            dbType = DbContextHolder.DbType.slave;
                        }
                    }else{
                        if(null == method.getAnnotation(org.springframework.data.jpa.repository.Modifying.class)){
                            dbType = DbContextHolder.DbType.slave;
                        }
                    }

                }
                System.err.println(method.getName()+(synchonizationActive?"开启事物":"无事物")+"  数据源："+dbType.name());


                DbContextHolder.setDbType(dbType);
                return invocation.proceed();
            }
        });
    }
}
```

##### 4.2加载处理器 ：继承JpaRepositoryFactoryBean，实现自定义FactoryBean，注入CustomPostProcessor自定义处理器
```
public class CustomJpaRepositoryFactoryBean <R extends JpaRepository<T, I>, T,
        I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I>{
    public CustomJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }


    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        JpaRepositoryFactory jpaFac = new JpaRepositoryFactory(entityManager);
        jpaFac.addRepositoryProxyPostProcessor(new CustomPostProcessor());
        return jpaFac;
    }
}
```

#### 4.3 配置repositoryFactoryBeanClass，使repositoryFactoryBean生效
```
@Configuration
@EnableJpaRepositories(basePackages="com.person.demo.repository",repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class JpaConfig{
}
```