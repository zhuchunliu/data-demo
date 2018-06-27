package com.person.demo.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 * 过滤方法，如果是查询走slave库，如果是更新方法， 走master库
 *
 * Created by Darren on 2018-06-27
 **/
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
