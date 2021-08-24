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
    // 取消意味着消息不处理
    boolean canceled;

    public ChannelFuture writeAndFlush(Object response){
       return serverCtx.writeAndFlush(response);
    }



    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }



    public ChannelHandlerContext getServerCtx() {
        return serverCtx;
    }

    public void setServerCtx(ChannelHandlerContext serverCtx) {
        this.serverCtx = serverCtx;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel(boolean canceled) {
        this.canceled = canceled;
    }
}
