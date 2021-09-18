package io.study.gateway.client;


import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Promise;
import io.study.gateway.config.ProxyConfig;

public interface ClientOrigin {


    public ProxyConfig getClientConfig();

    Promise<PooledConnection> connectToServer(EventLoop eventLoop);
}
