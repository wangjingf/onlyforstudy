package io.study.gateway.limit;

public interface ILimitService {
    public boolean canRequest(String key,Integer limit);
}
