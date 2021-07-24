package io.study.gateway.balance;

import io.study.gateway.config.HostConfig;

import java.util.List;

public interface ILoadBalance {
    public HostConfig select(List<HostConfig> hosts, LoadBalanceContext context);
}
