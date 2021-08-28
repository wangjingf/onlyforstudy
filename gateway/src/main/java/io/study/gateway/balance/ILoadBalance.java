package io.study.gateway.balance;


import io.study.gateway.config.INode;
import io.study.gateway.invoker.ProxyInvoker;

import java.util.List;

public interface ILoadBalance {
    public INode select(List<INode> invokers);
}
