package io.study.gateway.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import io.study.gateway.proxy.ProxyEndpoint;
import io.study.gateway.proxy.StreamContext;

import java.net.SocketAddress;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/***
 * 连接是否需要引用计数呢？
 * connectionPool是针对某台服务器的连接池
 * 但是总的连接池要不要也限制一下呢？每个服务都创建这么多，最后会不会崩溃？？
 *
 */
public class ConnectionPool {
   Deque<Channel> idleChannels = new ConcurrentLinkedDeque<>();
   Deque<Channel> activeChannels = new ConcurrentLinkedDeque<>();
   EventLoop eventLoop = null;
   int coreConnectionSize;
   int maxConnectionSize;
   ChannelInitializer channelInitializer;
   ReentrantLock mainLock = new ReentrantLock();
   AtomicInteger creatingCount = new AtomicInteger(0);
   AtomicBoolean active = new AtomicBoolean(true);
    public ConnectionPool(ConnectionPoolConfig poolConfig,int coreSize,int maxConnectionSize){
        this.connPoolConfig = poolConfig;
        this.coreConnectionSize = coreSize;
        this.maxConnectionSize = maxConnectionSize;
    }
   ConnectionPoolConfig connPoolConfig;
   public Promise<PooledConnection> getActiveChannel(EventLoop eventLoop, StreamContext context) {
       Channel channel  = null;
       Promise<PooledConnection> promise = eventLoop.newPromise();
       while ((channel = idleChannels.poll() )!= null){ // 移除连接
            if(isValidChannel(channel)){
                promise.setSuccess(new PooledConnection(this,channel));
                return promise;
            }else{
                channel.close();
            }
       }
       int idleCount = getIdleChannelCount();
       int activeCount = getActiveChannelCount();
       if(idleCount + activeCount > connPoolConfig.getMaxConnectionCount()){
           promise.setFailure(new RuntimeException("connection overflow"));
       }
       tryMakeNewConnection(promise,context);
       return promise;
   }
   public void tryMakeNewConnection(Promise<PooledConnection> promise,StreamContext context){
       SocketAddress targetAddress = context.getTargetAddress();
       final Bootstrap bootstrap = new Bootstrap()
               .channel(NioSocketChannel.class)
               .handler(channelInitializer)
               .group(eventLoop)
               .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connPoolConfig.getConnectTimeout())
               .option(ChannelOption.SO_KEEPALIVE, connPoolConfig.isTcpKeepAlive())
               .option(ChannelOption.TCP_NODELAY, connPoolConfig.isTcpNoDelay())
               .option(ChannelOption.SO_SNDBUF, connPoolConfig.getTcpSendBufferSize())
               .option(ChannelOption.SO_RCVBUF, connPoolConfig.getTcpReceiveBufferSize())
               .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, connPoolConfig.getNettyWriteBufferHighWaterMark())
               .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, connPoolConfig.getNettyWriteBufferLowWaterMark())
               .option(ChannelOption.AUTO_READ, connPoolConfig.isNettyAutoRead())
               .remoteAddress(targetAddress);
       ChannelFuture future= bootstrap.connect();
       creatingCount.incrementAndGet();
       future.addListener(new ChannelFutureListener() {
           @Override
           public void operationComplete(ChannelFuture future) throws Exception {
               if(future.isSuccess()){
                   activeChannels.add(future.channel());
                   createConnection(future,targetAddress,promise);

               }else{
                   promise.setFailure(future.cause());
               }
               creatingCount.decrementAndGet();
           }
       });
   }
    public int getActiveChannelCount(){
       return this.activeChannels.size();
    }
    public int getIdleChannelCount(){
       return this.idleChannels.size();
    }
    private void createConnection(ChannelFuture future,SocketAddress address, Promise<PooledConnection> promise) {
        PooledConnection connection = new PooledConnection(this,future.channel());
        promise.setSuccess(connection);
    }

    public boolean isValidChannel(Channel channel){
       return channel.isActive();
   }

    public boolean isIdleFull(){
       return this.idleChannels.size() > this.coreConnectionSize;
    }
    public void passivate(Channel channel,boolean broken){
       if(broken || isIdleFull() || isDestroying()){
           channel.close();
       }
        mainLock.lock();
       try{
          if(isIdleFull()){
              channel.close();
          }else{
              idleChannels.push(channel);
          }

       }finally {
          mainLock.unlock();
       }

    }
    public boolean isDestroying(){
        return active.get();
    }
    public void destroy(){
        if(active.compareAndSet(true,false)){
            for (Channel activeChannel : activeChannels) {
                activeChannel.close();
            }
        }
    }

}
