package io.study.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHandler extends ChannelInboundHandlerAdapter {
    Map<String, FullHttpResponse> responses = new HashMap<>();
    static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
    public HttpRequestHandler(Map<String, FullHttpResponse> responses){
        this.responses = responses;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("httpServer.channel_active");
    }
    ChannelFutureListener futureListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(!future.isSuccess()){
                logger.error("httpServer.send_response_error:cause={}",future.cause());
            }else{
                logger.info("httpServer.send_response_flush_success");
            }
        }
    };
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        FullHttpResponse response = responses.get(uri);
        if(response == null){
            logger.info("httpServer.found_invalid_request:uri={}",uri);
            FullHttpResponse noFound = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.NOT_FOUND);
            HttpUtil.setContentLength(noFound,0);
            ctx.writeAndFlush(noFound);
            return;
        }
        logger.info("httpServer.handle_request_success:uri={}",uri);
        FullHttpResponse copy = response.copy();
        HttpUtil.setContentLength(copy,copy.content().readableBytes());
        ctx.writeAndFlush(copy).addListener(futureListener);
    }

    public void channelReadComplete(ChannelHandlerContext ctx){

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // TODO Auto-generated method stub
        ctx.close();
    }

}