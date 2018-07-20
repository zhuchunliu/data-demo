package com.person.demo.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * tk通用mapper
 * Created by Issac on 2017/9/20 0020.
 */
public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
