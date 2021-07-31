package io.study.gateway.proxy;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.study.gateway.config.ProxyConfig;

import java.net.SocketAddress;

public class ProxyContext {
    ChannelHandlerContext serverCtx;
    ProxyConfig proxyConfig;
    FullHttpRequest request = null;
    String domain;
    String path;
    public ChannelFuture writeAndFlush(Object response){
       return serverCtx.writeAndFlush(response);
    }



    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
        String uri = request.uri();
        if(uri.startsWith("/")){
            uri = uri.substring(1);
        }
        int slashIndex = uri.indexOf("/");
        if(slashIndex == -1){
            domain = uri;
        }else{
            domain = uri.substring(0,slashIndex);
            path = uri.substring(slashIndex);
        }
    }

    public ChannelHandlerContext getServerCtx() {
        return serverCtx;
    }

    public void setServerCtx(ChannelHandlerContext serverCtx) {
        this.serverCtx = serverCtx;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }
}
