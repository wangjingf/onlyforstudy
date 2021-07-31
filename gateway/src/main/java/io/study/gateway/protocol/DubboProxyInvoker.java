package io.study.gateway.protocol;

import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;

import java.net.SocketAddress;

public class DubboProxyInvoker implements ProxyInvoker {
    public DubboProxyInvoker(SocketAddress address){
        throw new RuntimeException("not support");
    }
    @Override
    public void invoke(ProxyContext context) {
        throw new RuntimeException("not support");
    }

    @Override
    public String getUrl() {
        return null;
    }
}
