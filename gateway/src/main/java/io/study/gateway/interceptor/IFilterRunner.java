package io.study.gateway.interceptor;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.study.gateway.message.http.ZuulHttpRequest;
import io.study.gateway.message.http.ZuulMessage;

public interface IFilterRunner<I extends ZuulMessage,O extends ZuulMessage> {
    public void filter(I request);
    public void filter(I request, HttpContent content);
}
