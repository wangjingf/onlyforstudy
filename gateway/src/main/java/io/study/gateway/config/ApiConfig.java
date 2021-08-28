package io.study.gateway.config;

import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.proxy.ProxyProtocol;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApiConfig {
    BalancePolicy policy;
    ProxyProtocol proxyProtocol;
    List<INode> servers = new CopyOnWriteArrayList<>();
    String srcUri;
    String destUri;
    int timeout;
    //每秒限流多少，为空时不限流
    Integer limit;

    public String getSrcUri() {
        return srcUri;
    }

    public void setSrcUri(String srcUri) {
        this.srcUri = srcUri;
    }

    public String getDestUri() {
        return destUri;
    }

    public void setDestUri(String destUri) {
        this.destUri = destUri;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<INode> getServers() {
        return servers;
    }

    public void setServers(List<INode> servers) {
        this.servers = servers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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
