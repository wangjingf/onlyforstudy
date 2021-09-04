package io.study.gateway.limit.impl;

import io.study.gateway.limit.ILimitService;

public class LocalLimitService implements ILimitService {

    @Override
    public boolean canRequest(String key, Integer limit) {
        return false;
    }
}
