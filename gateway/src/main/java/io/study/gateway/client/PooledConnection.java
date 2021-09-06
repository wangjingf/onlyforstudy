package io.study.gateway.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.study.gateway.config.INode;


import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class PooledConnection {
    Channel channel;
    ConnectionPool pool;
    INode targetNode;
    AtomicBoolean active = new AtomicBoolean(true);
    public PooledConnection(ConnectionPool pool,Channel channel){
        this.pool = pool;
        this.channel = channel;

    }

    public Channel getChannel(){
        return channel;
    }

    public void passivate(){
      pool.passivate(this.channel,false);
    }

    public ChannelFuture close(){
        if(active.compareAndSet(true,false)){
            pool.passivate(this.channel,false);
        }
        return null;
    }
    public boolean isActive(){
        return active.get() &&  channel.isActive();
    }
    public boolean isSuccess(){
        return channel.isActive();
    }

    public INode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(INode targetNode) {
        this.targetNode = targetNode;
    }
}
