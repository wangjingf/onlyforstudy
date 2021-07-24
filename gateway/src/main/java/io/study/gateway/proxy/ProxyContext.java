package io.study.gateway.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.study.gateway.config.ProxyConfig;

import java.net.SocketAddress;

public class ProxyContext {
    ChannelHandlerContext serverCtx;
    ProxyConfig proxyConfig;
    FullHttpRequest request = null;
    public  void writeAndFlush(Object response){
        serverCtx.writeAndFlush(response);
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
    }

    public ChannelHandlerContext getServerCtx() {
        return serverCtx;
    }

    public void setServerCtx(ChannelHandlerContext serverCtx) {
        this.serverCtx = serverCtx;
    }
}
