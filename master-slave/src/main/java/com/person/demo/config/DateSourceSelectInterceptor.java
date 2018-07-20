package com.person.demo.config;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Properties;

/**
 * 数据源选择器
 *
 * Created by Darren on 2018-06-26
 **/
@Intercepts({@Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class DateSourceSelectInterceptor implements Interceptor {

    /**正则匹配 insert、delete、update操作*/
    private static final String REGEX = ".*insert\\\\u0020.*|.*delete\\\\u0020.*|.*update\\\\u0020.*";

    private Logger logger = LoggerFactory.getLogger(DateSourceSelectInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchonizationActive = TransactionSynchronizationManager.isSynchronizationActive();//判断当前操作是否有事务

        //获取执行参数
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];

        DbContextHolder.DbType dbType = DbContextHolder.DbType.master;//默认设置使用主库
        if (!synchonizationActive){
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)){//读方法
                //selectKey为自增主键（SELECT LAST_INSERT_ID()）方法,使用主库;自增表insert之后，会执行该语句
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    dbType = DbContextHolder.DbType.master;
                }else {
                    dbType = DbContextHolder.DbType.slave;
                    //此段代码没有发现意义
//                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
//                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replace("[\\t\\n\\r]"," ");
//                    if (sql.matches(REGEX)){//如果是insert、delete、update操作 使用主库
//                        dbType = DbContextHolder.DbType.master;
//                    }else {
//                        //使用从库
//                        dbType = DbContextHolder.DbType.slave;

//                    }
                }
            }
        }
        //设置数据源
        DbContextHolder.setDbType(dbType);
        logger.error("是否开启事物"+synchonizationActive+" ====== "+dbType);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor){ //如果是Executor（执行增删改查操作），则拦截下来
            return Plugin.wrap(target,this);
        }else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
