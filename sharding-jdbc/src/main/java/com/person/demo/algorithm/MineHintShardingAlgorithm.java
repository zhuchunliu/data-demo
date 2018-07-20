package com.person.demo.algorithm;

import io.shardingsphere.core.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.hint.HintShardingAlgorithm;

import java.util.Collection;

/**
 * Created by Darren on 2018-07-18
 **/
public class MineHintShardingAlgorithm implements HintShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        return null;
    }
}
