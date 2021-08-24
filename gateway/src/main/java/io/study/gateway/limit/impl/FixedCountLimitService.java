package io.study.gateway.limit.impl;

import io.study.gateway.limit.ILimitService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedCountLimitService implements ILimitService {
    int total;
    Map<String,Object> count = new ConcurrentHashMap<>();
    public FixedCountLimitService(int total){
        this.total = total;
    }
    @Override
    public boolean canRequest(String key, Integer limit) {
        return false;
    }
}
