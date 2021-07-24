package io.study.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHandler extends ChannelHandlerAdapter {
    Map<String, FullHttpResponse> responses = new HashMap<>();
    public HttpRequestHandler(Map<String, FullHttpResponse> responses){
        this.responses = responses;
    }
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        FullHttpResponse response = responses.get(uri);
        ctx.writeAndFlush(response);
         //ctx.writeAndFlush()
    }
    public void channelReadComplete(ChannelHandlerContext ctx){

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        ctx.close();
    }

}