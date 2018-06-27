package com.person.demo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * Created by Darren on 2018-06-25
 **/

public class RoutingDataSource  extends AbstractRoutingDataSource {
    /**
     * 源码：AbstractRoutingDataSource 的determineCurrentLookupKey方法，定义选中的数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //此处的返回值，为targetDataSources设置的多数据源Map对象的key值
        return DbContextHolder.getDbType();
    }
}
