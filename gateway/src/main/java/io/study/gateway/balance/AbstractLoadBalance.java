package io.study.gateway.balance;

import io.study.gateway.invoker.INode;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public  abstract  class AbstractLoadBalance implements ILoadBalance {
    @Override
    public INode select(List<INode> invokers) {
        assert invokers.size() > 0;
        if(invokers.size() > 1){
            return doSelect(invokers);
        }
        return invokers.get(0);
    }
    public abstract INode doSelect(List<INode> invokers);
}
