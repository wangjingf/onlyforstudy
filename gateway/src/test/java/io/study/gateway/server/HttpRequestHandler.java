package io.study.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    Map<String, FullHttpResponse> responses = new HashMap<>();
    public HttpRequestHandler(Map<String, FullHttpResponse> responses){
        this.responses = responses;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        FullHttpResponse response = responses.get(uri);
        FullHttpResponse copy = response.copy();
        HttpUtil.setContentLength(copy,copy.content().readableBytes());
        ctx.writeAndFlush(copy).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    future.cause().printStackTrace();
                }else{
                    System.out.println("send success");
                }
            }
        });
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