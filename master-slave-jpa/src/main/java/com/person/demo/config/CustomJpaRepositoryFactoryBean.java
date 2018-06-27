package com.person.demo.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 继承JpaRepositoryFactoryBean，实现自定义FactoryBean，注入CustomPostProcessor自定义处理器
 *
 * Created by Darren on 2018-06-27
 **/
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
