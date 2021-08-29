package io.study.gateway.interceptor.impl;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.StreamContext;
import io.study.gateway.registry.IRegistry;

public class StatFilter implements IFilter {
    public IRegistry registry = null;
    public StatFilter(IRegistry registry){
        this.registry = registry;
    }
    @Override
    public Promise<FullHttpResponse> filter(StreamContext context, IFilterChain filterChain) {
        Meter meter = registry.newMeter("gateway.new_request_counter");
        Timer timer = registry.newTimer("gateway.new_request_timer");
        meter.mark();
        Timer.Context ctx = timer.time();
        Promise<FullHttpResponse> result = filterChain.doFilter(context);
        result.addListener(new GenericFutureListener<Future<? super FullHttpResponse>>() {
            @Override
            public void operationComplete(Future<? super FullHttpResponse> future) throws Exception {
                ctx.stop();
            }
        });
        return result;
    }
}
