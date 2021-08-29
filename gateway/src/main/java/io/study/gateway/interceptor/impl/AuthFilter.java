package io.study.gateway.interceptor.impl;

import io.netty.util.concurrent.Promise;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.StreamContext;

public class AuthFilter implements IFilter {


    @Override
    public Promise<HttpResponseInfo> filter(StreamContext context, IFilterChain filterChain) {
        return null;
    }
}
