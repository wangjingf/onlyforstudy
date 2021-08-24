package io.study.gateway.balance.impl;

import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.LoadBalanceContext;
import io.study.gateway.config.HostConfig;
import io.study.gateway.invoker.INode;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public class DefaultBalance implements ILoadBalance {

    @Override
    public INode select(List<INode> invokers) {
        return null;
    }
}
