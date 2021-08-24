package io.study.gateway.proxy;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.message.http.HttpMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyEndpoint {
    static final Logger logger = LoggerFactory.getLogger(ProxyEndpoint.class);
    StreamContext context;
    Promise result = null;
    public ProxyEndpoint(StreamContext context, Promise result){
        this.context = context;
        this.result =result;
    }
    public void proxy(){
        writeReq();
    }
    private void writeReq(){
        HttpMessageInfo request = context.getRequest();
        if(request.isFull()){
            Object req = transformToRpcReq(request);
            context.getToChannel().writeAndFlush(req).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(!future.isSuccess()){
                        logger.error("proxyfilter.proxy_error",future.cause());
                        result.setFailure(future.cause());
                    }else{
                        result.setSuccess(future.getNow());
                    }
                }
            });
        }else{
            if(request.isHasContent()){
                context.getToChannel().writeAndFlush(request.getContent()).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if(!future.isSuccess()){
                            logger.error("proxyfilter.proxy_error",future.cause());
                            result.setFailure(future.cause());
                        }else{
                            result.setSuccess(future.getNow());
                        }
                    }
                });
            }else{
                context.getToChannel().writeAndFlush(request.getRequest()).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if(!future.isSuccess()){
                            logger.error("proxyfilter.proxy_error",future.cause());
                            result.setFailure(future.cause());
                        }else{
                            result.setSuccess(future.getNow());
                        }
                    }
                });
            }

        }
    }

    /**
     * 转换成rpc协议
     * @param request
     * @return
     */
    private Object transformToRpcReq(HttpMessageInfo request) {
        return request;
    }

    public void responseFromOrigin(HttpResponse response){
        context.setResponse(response);
        context.getFromChannel().writeAndFlush(response);
    }
}
