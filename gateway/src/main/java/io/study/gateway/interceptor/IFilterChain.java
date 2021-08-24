package io.study.gateway.interceptor;

import io.netty.util.concurrent.Promise;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.StreamContext;


public interface IFilterChain {
    public Promise<HttpResponseInfo> doFilter(StreamContext context);
}
