package io.study.gateway.interceptor;

import io.study.gateway.proxy.ProxyContext;

public interface IFilterChain {
    public void doFilter(ProxyContext context);
}
