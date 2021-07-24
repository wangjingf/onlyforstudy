package io.study.gateway.config;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApiConfig {
    List<HostConfig> targetAddress  = new CopyOnWriteArrayList<>();
    List<HostConfig> activeAddresses = new CopyOnWriteArrayList<>();
    String srcUri;
    String destUri;
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

    public List<HostConfig> getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(List<HostConfig> targetAddress) {
        this.targetAddress = targetAddress;
    }

    public List<HostConfig> getActiveAddresses() {
        return activeAddresses;
    }

    public void setActiveAddresses(List<HostConfig> activeAddresses) {
        this.activeAddresses = activeAddresses;
    }
}
