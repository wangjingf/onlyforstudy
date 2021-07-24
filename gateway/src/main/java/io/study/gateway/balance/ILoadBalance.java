package io.study.gateway.balance;


import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public interface ILoadBalance {
    public ProxyInvoker select(List<ProxyInvoker> invokers, LoadBalanceContext context);
}
