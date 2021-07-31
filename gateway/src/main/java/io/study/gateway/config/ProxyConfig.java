package io.study.gateway.config;

import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.proxy.ProxyProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConfig {
    BalancePolicy policy;
    ProxyProtocol proxyProtocol;
    String domain;
    Map<String/*uri*/,ApiConfig> apiConfig = new ConcurrentHashMap<>();

    public ApiConfig getApiConfig(String uri) {
        uri = uri.substring(domain.length() +1);
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
