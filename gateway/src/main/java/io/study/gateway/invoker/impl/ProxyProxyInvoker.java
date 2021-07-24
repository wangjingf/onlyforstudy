package io.study.gateway.invoker.impl;

import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;

import java.util.List;

public class ProxyProxyInvoker implements ProxyInvoker {
    List<ProxyInvoker> invokers = null;
    @Override
    public void invoke(ProxyContext context) {

    }
}
