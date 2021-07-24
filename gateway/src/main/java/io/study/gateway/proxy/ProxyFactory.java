package io.study.gateway.proxy;

import io.study.gateway.config.ProxyConfig;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.protocol.HttpProxyInvoker;
import io.study.gateway.transport.HttpTransport;
;

public class ProxyFactory {
    public ProxyInvoker createProxyInvoker(ProxyConfig config){
        HttpTransport transport = new HttpTransport();
        HttpProxyInvoker proxyInvoker = new HttpProxyInvoker(transport);
        return proxyInvoker;
    }
}
