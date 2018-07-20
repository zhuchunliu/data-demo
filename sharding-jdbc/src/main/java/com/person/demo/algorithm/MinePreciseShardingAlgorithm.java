package com.person.demo.algorithm;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * Created by Darren on 2018-07-18
 **/
public class MinePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {

        //测试案例：被切分值<=5,放到第一个数据库，其他值放到第二个数据库
        if(shardingValue.getValue()<=5){
            return availableTargetNames.toArray()[0].toString();
        }else{
            return availableTargetNames.toArray()[1].toString();
        }
//        for (String each : availableTargetNames) {
//            System.err.println(each+"====");
//            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
//                return each;
//            }
//        }
//        throw new UnsupportedOperationException();
    }
}
