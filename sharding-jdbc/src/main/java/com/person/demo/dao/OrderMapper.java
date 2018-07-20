package com.person.demo.dao;

import com.person.demo.entity.Order;
import com.person.demo.util.TkMapper;

/**
 * Created by Darren on 2018-07-17
 **/
public interface OrderMapper extends TkMapper<Order> {

    Long insertOrder(Order model);

    Long insertOrderNullPrimaryKey(Order model);

    void truncate();
}
