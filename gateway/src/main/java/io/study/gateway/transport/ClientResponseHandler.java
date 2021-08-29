package io.study.gateway.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Promise;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.impl.LeastActiveLoadBalance;
import io.study.gateway.client.ClientTimeoutHandler;
import io.study.gateway.client.CloseOnIdleHandler;
import io.study.gateway.client.ConnectionPool;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.proxy.ProxyEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

public  class ClientResponseHandler extends ChannelInboundHandlerAdapter {

    static final Logger logger = LoggerFactory.getLogger(ClientResponseHandler.class);
    ChannelFutureListener writeListener = new  ChannelFutureListener(){

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(!future.isSuccess()){
                logger.error("write request error",future.cause());
            }
        }
    };
    private ArrayList<HttpContent> contents = new ArrayList<>();
    public   ClientResponseHandler(){
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("proxyClient.channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProxyEndpoint endpoint = (ProxyEndpoint) ctx.channel().attr(GatewayConstant.KEY_PROXY_ENDPOINT).get();
        //super.channelRead(ctx, msg);
        logger.info("proxyClient.read_message_success:msg={}",msg);
        //if (msg instanceof HttpResponse) {
        /*if(msg instanceof HttpResponse){

        }else if(msg instanceof HttpContent){
            endpoint.responseFromOrigin((HttpContent) msg);
        }*/
        endpoint.responseFromOrigin((FullHttpResponse) msg);
        ctx.fireChannelRead(msg);
       // }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client error cause:",cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel inactive");
        super.channelInactive(ctx);
    }
}