package com.person.demo.dao;

import com.person.demo.entity.OrderItem;
import com.person.demo.util.TkMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-07-17
 **/
public interface OrderItemMapper extends TkMapper<OrderItem>{
    List<Map<Object,Object>> selectInfoUnionOrder();

    List<OrderItem> selectAllInfo();

    void truncate();
}
