package io.study.gateway.interceptor;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.study.gateway.proxy.ProxyContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface IFilter {
    public void filter(ProxyContext context);
}
