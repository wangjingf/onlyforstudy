package io.study.gateway.stat;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.proxy.StreamContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Request;

public class RequestStatFilter implements IFilter {
    static final Logger logger = LoggerFactory.getLogger(RequestStatFilter.class);
    StatCollector collector = new StatCollector();
    public RequestStatFilter(){

    }
    @Override
    public Promise<FullHttpResponse> filter(StreamContext context, IFilterChain filterChain) {
        logger.info("stat.handler_stat_filter:uri={}",context.getRequest().uri());
        Promise<FullHttpResponse> promise = filterChain.doFilter(context);
        RequestStat stat = new RequestStat();
        stat.setStartTime(System.currentTimeMillis());
        promise.addListener(new GenericFutureListener(){

            @Override
              public void operationComplete(Future future) throws Exception {
                stat.setEndTime(System.currentTimeMillis());
                stat.setCause(future.cause());
                stat.setUri(context.getRequest().uri());
                if(future.isSuccess()){
                    stat.setSuccess(true);
                }else{
                    stat.setSuccess(false);
                }
                collector.onStat(stat);
            }
        });
        return promise;
    }
}
