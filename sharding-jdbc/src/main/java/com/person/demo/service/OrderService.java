package com.person.demo.service;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.google.common.base.Optional;
import com.person.demo.dao.OrderItemMapper;
import com.person.demo.dao.OrderMapper;
import com.person.demo.entity.Order;
import com.person.demo.entity.OrderItem;
import io.shardingsphere.core.jdbc.core.connection.ShardingConnection;
import io.shardingsphere.core.jdbc.unsupported.AbstractUnsupportedOperationConnection;
import io.shardingsphere.transaction.api.SoftTransactionManager;
import io.shardingsphere.transaction.api.config.NestedBestEffortsDeliveryJobConfiguration;
import io.shardingsphere.transaction.api.config.SoftTransactionConfiguration;
import io.shardingsphere.transaction.bed.BEDSoftTransaction;
import io.shardingsphere.transaction.constants.SoftTransactionType;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-07-17
 **/
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private DataSource dataSource;

    /**
     * 弱事物
     *
     * 参考AbstractConnectionAdapter.commit方法，引擎会遍历底层所有真正的数据库连接，一个个进行commit操作，如果任何一个出现了异常，直接捕获异常，
     * 但是也只是捕获而已，然后接着下一个连接的commit
     *
     * @throws Exception
     */
    public void weakTraction() throws Exception{
        ShardingConnection connection = (ShardingConnection)dataSource.getConnection();
        connection.setAutoCommit(false);
        try {

            connection.prepareStatement("insert into t_order(order_id,user_id,type) VALUES(14,2,'abc')").execute();
            connection.prepareStatement("insert into t_order(order_id,user_id,type) VALUES(17,7,1)").execute();

            connection.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            connection.rollback();
        }finally {
            connection.close();
        }

    }

    /**
     * 柔性事物；设置autocommit为true，记录sql日志，错误，则按照设置的重复次数，重复执行
     *
     * @throws SQLException
     * @throws SystemException
     */
    public void flexibleTractionInfo() throws SQLException, SystemException {
        BEDSoftTransaction transaction = null;
        Connection connection = null;
        try {
            //1、配置记录日志的数据库：程序会自动创建表，记录执行失败的sql，用来重复执行；
            // 第一次执行失败的日志，第二次重启服务的时候，不会再去执行，只限一次事物内
            BasicDataSource logDataSource = new BasicDataSource();
            logDataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
            logDataSource.setUrl("jdbc:mysql://localhost:3306/trans_log?characterEncoding=utf8&useSSL=true&serverTimezone=Hongkong");
            logDataSource.setUsername("root");
            logDataSource.setPassword("123456");

            //2.创建transaction配置信息
            SoftTransactionConfiguration transactionConfig = new SoftTransactionConfiguration(dataSource);
            transactionConfig.setBestEffortsDeliveryJobConfiguration(Optional.of(new NestedBestEffortsDeliveryJobConfiguration()));
            transactionConfig.setTransactionLogDataSource(logDataSource);

            // 3. 初始化柔性事务管理器
            SoftTransactionManager transactionManager = new SoftTransactionManager(transactionConfig);
            transactionManager.init();
            transaction = (BEDSoftTransaction) transactionManager.getTransaction(SoftTransactionType.BestEffortsDelivery);

             // 4. 开启事务
            transaction.begin(connection = dataSource.getConnection());

            // 5. 执行JDBC
            connection.prepareStatement("insert into t_order(order_id,user_id,type) VALUES(2,2,1)").execute();
            connection.prepareStatement("insert into t_order(order_id,user_id,type) VALUES(17,7,1)").execute();
            connection.prepareStatement("insert into t_order(order_id,user_id,type) VALUES(14,2,'abc')").execute();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            transaction.end();//关闭事物
            if (null != connection) {
                connection.close();
            }
        }
    }

    public void saveInfo() {
        orderMapper.truncate();
        orderItemMapper.truncate();

        for (int index = 1; index < 11; index++) {
            Order order = new Order();
            order.setOrderId(index);
            order.setUserId(index);
            order.setType("1");
//            orderMapper.insertOrder(order);
            orderMapper.insert(order); //使用tkmapper方式插入数据，必须手动设置orderId主键值，要不分库功能失效【即使设置@Id 和 @GeneratedValue也无效】

//            order = new Order();
//            order.setUserId(index);
//            order.setType("1");
//            orderMapper.insertOrderNullPrimaryKey(order);//使用sharding-jdbc自动生成的主键


            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(index);
            orderItem.setUserId(index);
            orderItem.setOrderId(index);
            orderItemMapper.insert(orderItem);

        }
    }





    public void getAllItem() {
        orderItemMapper.selectInfoUnionOrder().forEach(System.out::println);
//        orderItemMapper.selectAllInfo().forEach(System.out::println);

//        orderItemMapper.selectAll().forEach(System.out::println);
    }


}

