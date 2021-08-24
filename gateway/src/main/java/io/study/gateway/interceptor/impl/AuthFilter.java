package io.study.gateway.interceptor.impl;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.proxy.ProxyContext;

public class AuthFilter implements IFilter {


    @Override
    public void filter(HttpRequest request, IFilterChain chain) {

    }

    @Override
    public void filter(HttpRequest request, HttpContent content, IFilterChain chain) {

    }
}
