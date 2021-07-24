package io.study.gateway.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoop;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.transport.HttpTransport;

public class HttpProxyInvoker implements ProxyInvoker {
    HttpTransport transport = null;

    public HttpProxyInvoker(HttpTransport transport) {
        this.transport = transport;
    }

    @Override
    public void invoke(ProxyContext context) {
         transport.proxy(context);

    }
}
