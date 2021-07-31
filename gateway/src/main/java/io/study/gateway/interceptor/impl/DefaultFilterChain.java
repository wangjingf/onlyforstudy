package io.study.gateway.interceptor.impl;


import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.proxy.ProxyContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DefaultFilterChain implements IFilterChain {
    int pos = 0;
    List<IFilter>  filters = new ArrayList<>();
    Function callback;
    public DefaultFilterChain(List<IFilter> filters,Function callback){
        this.filters = filters;
        this.callback = callback;
    }
    @Override
    public void doFilter(ProxyContext context) {
        if(pos >=filters.size()){
            callback.apply(context);
        }else{
            IFilter filter = filters.get(pos);
            pos++;
            filter.filter(context,this);
        }
    }
}
