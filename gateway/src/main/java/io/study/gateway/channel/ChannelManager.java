package io.study.gateway.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.client.ConnectionPool;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.HostConfig;
import io.study.gateway.invoker.INode;
import io.study.gateway.protocol.HttpProxyInvoker;
import io.study.gateway.proxy.ProxyEndpoint;
import io.study.gateway.proxy.StreamContext;
import io.study.gateway.registry.IServerList;
import io.study.gateway.registry.IServerUpdateListener;
import net.sf.ehcache.pool.Pool;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager implements IServerUpdateListener {
    public static AttributeKey CHANNEL_MANAGER = AttributeKey.valueOf("CHANNEL_MANAGER");
    ILoadBalance balance = null;
    IServerList serverList;
    ApiConfig apiConfig;

    public ChannelManager(IServerList serverList, ApiConfig apiConfig) {
        this.serverList = serverList;
        this.serverList.setChannelManager(this);
        this.serverList.start();
        this.apiConfig = apiConfig;
    }

    Map<SocketAddress, ConnectionPool> addressPool = new ConcurrentHashMap<>();

    public Promise<PooledConnection> acquire(StreamContext streamContext) {
        List<INode> invokers = new ArrayList<>();

        for (INode node : serverList.getActiveServers()) {
            invokers.add(new HttpProxyInvoker(node.getAddress()));
        }

        INode node = balance.select(invokers);
        ConnectionPool pool = addressPool.computeIfAbsent(node.getAddress(), vs -> {
            return new ConnectionPool(null, 1, 1);
        });
        Promise<PooledConnection> promise = pool.getActiveChannel(streamContext.getFromChannel().eventLoop(), streamContext);
        promise.addListener(new GenericFutureListener<Future<? super PooledConnection>>() {
            @Override
            public void operationComplete(Future<? super PooledConnection> future) throws Exception {
                if (future.isSuccess()) {
                    PooledConnection conn = (PooledConnection) future.getNow();
                    streamContext.setToChannel(conn.getChannel());
                    streamContext.setTargetAddress(node.getAddress());
                }
            }
        });
        return promise;
    }

    public boolean release(PooledConnection connection) {
        return true;
    }

    @Override
    public void onServerAdd(INode server) {

    }

    @Override
    public void onServerRemove(INode server) {
        addressPool.remove(server.getAddress());
    }
}
