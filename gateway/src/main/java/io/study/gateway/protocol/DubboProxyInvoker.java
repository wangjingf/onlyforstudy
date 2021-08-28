package io.study.gateway.protocol;

import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;

import java.net.SocketAddress;

public class DubboProxyInvoker implements ProxyInvoker {

    @Override
    public void invoke(ProxyContext context) {

    }

    @Override
    public SocketAddress getAddress() {
        return null;
    }
}
