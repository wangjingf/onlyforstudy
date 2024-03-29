package io.study.gateway.proxy;

import com.jd.vd.common.exception.BizException;
import com.jd.vd.common.lang.Guard;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.message.http.HttpMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    public StreamContext getContext(){
        return this.context;
    }
    private void writeReq(){
        logger.info("proxyEndpoint.write_request_to_server");
        FullHttpRequest request = context.getRequest();
        Channel toChannel = context.getToChannel().getChannel();
        GenericFutureListener futureListener = new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(!future.isSuccess()){
                    Throwable cause = future.cause();
                    if(cause instanceof IOException){
                        context.getTargetNode().getBreaker().onFailure(cause);
                    }
                    logger.error("proxyEndpoint.proxy_error", cause);
                }else{
                }
            }
        };
        /*if(request.isFull()){*/
            Object req = transformToRpcReq(request);
            toChannel.writeAndFlush(req).addListener(futureListener);

       /* }else{
            if(request.isHasContent()){
                toChannel.write(request.getRequest());
                toChannel.write(request.getContent()).addListener(futureListener);
            }else{
                toChannel.writeAndFlush(request.getRequest()).addListener(futureListener);
            }

        }*/
        toChannel.read();
    }

    /**
     * 转换成rpc协议
     * @param request
     * @return
     */
    private Object transformToRpcReq(FullHttpRequest request) {
        return request;
    }

    public void responseFromOrigin(FullHttpResponse response){
        logger.info("proxyEndpoint.receive_client_response:response={}",response);
        if(response instanceof HttpResponse){
            context.setResponse( response);
        }
       /* if(response instanceof LastHttpContent){
            LastHttpContent content = (LastHttpContent) response;
        }*/
        Guard.assertTrue(context.getFromChannel().getChannel().isActive(),"proxyEndpoint.err_from_channel_must_be_active:channel="+context.getFromChannel());
        context.getFromChannel().getChannel().writeAndFlush(response).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(!future.isSuccess()){
                    logger.error("proxyEndpoint.response_from_channel_error",future.cause());
                    result.setFailure(future.cause());
                }else{
                    logger.info("proxyEndpoint.write_response_success");
                    result.setSuccess(future.getNow());
                }
            }
        });
    }
}
