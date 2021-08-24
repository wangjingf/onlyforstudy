package io.study.gateway.interceptor.impl;


import io.netty.util.concurrent.Promise;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.proxy.StreamContext;
import org.apache.http.HttpResponse;

import javax.xml.ws.Response;
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
    public Promise<HttpResponseInfo> doFilter(StreamContext context) {
        if(pos >=filters.size()){
            if(callback != null){
                callback.apply(context);
            }
            Promise promise = context.getFromChannel().newPromise();
            promise.setSuccess(context.getResponse());
            return promise;
        }else{
            IFilter filter = filters.get(pos);
            pos++;
            return filter.filter(context,this);
        }
    }
}
