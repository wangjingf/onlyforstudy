package io.study.gateway.proxy;

import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.HostConfig;
import io.study.gateway.config.ProxyConfig;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.invoker.impl.ClusterProxyInvoker;
import io.study.gateway.protocol.DubboProxyInvoker;
import io.study.gateway.protocol.HttpProxyInvoker;

import java.util.ArrayList;
import java.util.List;


public class ProxyFactory {

    public ProxyInvoker createProxyInvoker(ProxyContext context){
        String uri = context.getRequest().uri();
        ApiConfig apiConfig = context.getProxyConfig().getApiConfig(uri);
        List<HostConfig> activeAddresses = apiConfig.getActiveAddresses();
        List<ProxyInvoker> invokers = new ArrayList<>();
        for (HostConfig hostConfig : activeAddresses) {
            invokers.add(createProtocolInvoker(context.getProxyConfig().getProxyProtocol(),hostConfig));
        }
        ProxyInvoker proxyInvoker = new ClusterProxyInvoker(invokers,context.getProxyConfig().getPolicy());
        return proxyInvoker;
    }
    private ProxyInvoker createProtocolInvoker(ProxyProtocol proxyProtocol,HostConfig config){
        if(ProxyProtocol.Http1_1.equals(proxyProtocol)){
            return new HttpProxyInvoker(config.getAddress());
        }else {
            return new DubboProxyInvoker(config.getAddress());
        }
    }
}
