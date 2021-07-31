package io.study.gateway.balance;

import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public  abstract  class AbstractLoadBalance implements ILoadBalance {
    @Override
    public ProxyInvoker select(List<ProxyInvoker> invokers, LoadBalanceContext context) {
        assert invokers.size() > 0;
        if(invokers.size() > 1){
            return doSelect(invokers,context);
        }
        return invokers.get(0);
    }
    public abstract ProxyInvoker doSelect(List<ProxyInvoker> invokers, LoadBalanceContext context);
}
