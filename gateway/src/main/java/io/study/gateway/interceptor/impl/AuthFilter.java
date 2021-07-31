package io.study.gateway.interceptor.impl;

import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.proxy.ProxyContext;

public class AuthFilter implements IFilter {


    @Override
    public void filter(ProxyContext context, IFilterChain chain) {

    }
}
