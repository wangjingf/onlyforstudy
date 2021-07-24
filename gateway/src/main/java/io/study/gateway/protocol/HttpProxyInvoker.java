package io.study.gateway.protocol;


import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.transport.HttpTransport;

import java.net.SocketAddress;

public class HttpProxyInvoker implements ProxyInvoker {
    HttpTransport transport = null;

    public HttpProxyInvoker(SocketAddress address) {
        transport = new HttpTransport(address);
    }

    @Override
    public void invoke(ProxyContext context) {
         transport.proxy(context);
    }
}
