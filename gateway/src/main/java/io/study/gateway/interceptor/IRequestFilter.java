package io.study.gateway.interceptor;

import io.netty.handler.codec.http.HttpRequest;

public interface IRequestFilter {
    public boolean filter(HttpRequest request);
}
