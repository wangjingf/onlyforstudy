package io.study.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

public class ProxyResponseMap {
    public static final Map<String,FullHttpResponse> responseMap = new HashMap<>();
    public static final Map<String,String> contentMap = new HashMap<>();
    static FullHttpResponse newResponse(int status,String msg,String contentType){
        byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
        ByteBuf content = Unpooled.wrappedBuffer(bytes);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(status),content);
        return response;
    }
    static void mapping(String path,FullHttpResponse response){
        responseMap.put(path,response);
        String content = response.content().toString(CharsetUtil.UTF_8);
        contentMap.put(path,content);

    }
    static{
        responseMap.put("/json",newResponse(200,"{\"a\":1}","application/json"));
        responseMap.put("/html",newResponse(200,"<!DOCTYPE html><html><body>312</body></html>","text/html"));
    }
}
