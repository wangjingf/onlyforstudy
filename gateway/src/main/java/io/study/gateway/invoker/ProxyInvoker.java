package io.study.gateway.invoker;

import io.study.gateway.config.INode;
import io.study.gateway.proxy.ProxyContext;

public interface ProxyInvoker extends INode {
    public void invoke(ProxyContext context);
}
