package io.study.gateway.config;

import io.study.gateway.balance.BalancePolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConfig {
    BalancePolicy policy;
    String proxyProtocol;
    String proxyAddr;
    Map<String/*uri*/,ApiConfig> config = new ConcurrentHashMap<>();

    public ApiConfig resolveApi(String uri) {
        return config.get(uri);
    }

    public String getProxyAddr() {
        return proxyAddr;
    }

    public void setProxyAddr(String proxyAddr) {
        this.proxyAddr = proxyAddr;
    }

    public BalancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(BalancePolicy policy) {
        this.policy = policy;
    }

    public String getProxyProtocol() {
        return proxyProtocol;
    }

    public void setProxyProtocol(String proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public Map<String, ApiConfig> getConfig() {
        return config;
    }

    public void setConfig(Map<String, ApiConfig> config) {
        this.config = config;
    }
}
