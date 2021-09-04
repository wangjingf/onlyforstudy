package io.study.gateway.stat;

import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.registry.IRegistry;

public class RequestStat {
    String uri;
    ProxyProtocol proxyProtocol;
    String target;
    long startTime;
    long endTime;
    boolean isSuccess;
    Throwable cause;

    public RequestStat(IRegistry registry){}
}
