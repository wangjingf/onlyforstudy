package io.study.gateway.config;

import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.proxy.ProxyProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConfig {
    BalancePolicy policy;
    ProxyProtocol proxyProtocol;

    Map<String/*uri*/,ApiConfig> apiConfig = new ConcurrentHashMap<>();

    public ApiConfig getApiConfig(String uri) {
        return apiConfig.get(uri);
    }

    public void setApiConfig(String uri,ApiConfig cfg){
        apiConfig.put(uri,cfg);
    }

    public BalancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(BalancePolicy policy) {
        this.policy = policy;
    }

    public ProxyProtocol getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(ProxyProtocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }


}
