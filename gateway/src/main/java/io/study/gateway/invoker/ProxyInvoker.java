package io.study.gateway.invoker;

import io.study.gateway.proxy.ProxyContext;

public interface ProxyInvoker {
    public void invoke(ProxyContext context);
}
