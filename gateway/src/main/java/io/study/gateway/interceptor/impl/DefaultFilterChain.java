package io.study.gateway.interceptor.impl;


import io.study.gateway.interceptor.FilterHandler;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.proxy.ProxyContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultFilterChain implements IFilter {
    List<FilterHandler> handlers = new CopyOnWriteArrayList<>();
    int pos = 0;
    @Override
    public void filter(ProxyContext context) {
        if(pos < handlers.size()){
            FilterHandler handler = handlers.get(pos);
            handler.filter(context,this);
            return;
        }
    }
}
