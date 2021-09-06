package io.study.gateway.channel;

import com.jd.vd.common.exception.BizException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.impl.LeastActiveLoadBalance;
import io.study.gateway.client.ConnectionPool;
import io.study.gateway.client.ConnectionPoolConfig;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.config.INode;
import io.study.gateway.proxy.StreamContext;
import io.study.gateway.registry.IServerList;
import io.study.gateway.registry.IServerUpdateListener;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager implements IServerUpdateListener {
    public static AttributeKey CHANNEL_MANAGER = AttributeKey.valueOf("CHANNEL_MANAGER");
    ILoadBalance balance = new LeastActiveLoadBalance();
    IServerList serverList;
    ApiConfig apiConfig;
    ConnectionPoolConfig poolConfig;
    GatewaySetting setting = null;
    public ChannelManager(GatewaySetting gatewaySetting,ConnectionPoolConfig poolConfig, IServerList serverList, ApiConfig apiConfig) {
        this.poolConfig = poolConfig;
        this.serverList = serverList;
        this.serverList.setChannelManager(this);
        this.serverList.start();
        this.apiConfig = apiConfig;
        this.setting = gatewaySetting;
    }

    Map<SocketAddress, ConnectionPool> addressPool = new ConcurrentHashMap<>();

    /**
     * 失败了要重试一下呢，哈哈
     * @param streamContext
     * @return
     */
    public Promise<PooledConnection> acquire(StreamContext streamContext) {
        return acquire(streamContext,0);
    }
    private Promise<PooledConnection> acquire(StreamContext streamContext,final int retryCount) {
        List<INode> invokers = new ArrayList<>();

        for (INode node : serverList.getServers()) {
            if(!node.getBreaker().shouldBreak(streamContext)){
                invokers.add(node);
            }
        }

        INode node = balance.select(invokers);
        ConnectionPool pool = addressPool.computeIfAbsent(node.getAddress(), vs -> {
            return new ConnectionPool(setting,poolConfig, poolConfig.getCoreConnectionCount(), poolConfig.getMaxConnectionCount());
        });
        streamContext.setTargetNode(node);
        Promise<PooledConnection> promise = pool.getActiveChannel(streamContext.getFromChannel().getChannel().eventLoop(), streamContext);
        promise.addListener(new GenericFutureListener<Future<? super PooledConnection>>() {
            @Override
            public void operationComplete(Future<? super PooledConnection> future) throws Exception {
                if (future.isSuccess()) {
                    PooledConnection conn = (PooledConnection) future.getNow();
                    streamContext.setToChannel(conn);
                    streamContext.setTargetNode(node);
                    node.getBreaker().onSuccess(conn);
                }else{
                    node.getBreaker().onFailure(future.cause());
                    if(ChannelManager.this.apiConfig.isRetryWhenFail() && retryCount < serverList.getActiveServers().size()){
                        acquire(streamContext,retryCount+1);
                    }

                    throw new BizException("connection is too many");
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
