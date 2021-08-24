package io.study.gateway.interceptor;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.study.gateway.message.http.ZuulHttpRequest;
import io.study.gateway.message.http.ZuulMessage;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseFilterRunner implements IFilterRunner{
    static final Logger logger = LoggerFactory.getLogger(BaseFilterRunner.class);
    IFilter[] filters = null;
    IFilterRunner nextStage  = null;
    public BaseFilterRunner(IFilter[] filters,IFilterRunner nextStage){
        this.filters = filters;
        this.nextStage = nextStage;
    }

    @Override
    public void filter(ZuulMessage request) {
        ZuulMessage msg = request;
        for (IFilter filter : filters) {
            if(filter.shouldFilter(request)){
                msg = filter.apply(msg);
                if(msg == null){ // 终止过滤操作
                    msg = filter.getDefaultOutput(msg);
                    nextStage.filter(msg);
                    return ;
                }
            }
        }
        nextStage.filter(request);
    }

    @Override
    public void filter(ZuulMessage request, HttpContent content) {
        logger.info("filterrunner.begin_filter_request:",request);
        ZuulMessage msg = request;
        for (IFilter filter : filters) {
            if(filter.shouldFilter(request)){
                msg = filter.processContentChunk(msg,content);
                if(msg == null){ // 终止过滤操作
                    logger.info("filterrunner.has_skip_filter:filter={}",filter.name());
                    msg = filter.getDefaultOutput(msg);
                    nextStage.filter(msg,content);
                    return ;
                }
            }
        }
        nextStage.filter(request);
    }
}
