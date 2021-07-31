package io.study.gateway.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Promise;
import io.study.gateway.proxy.ProxyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ThreadFactory;

public class HttpTransport {

    private static final EventLoopGroup EVENT_LOOP_GROUP = eventLoopGroup(Constants.DEFAULT_IO_THREADS, "NettyClientWorker");
    SocketAddress address = null;
    public HttpTransport(SocketAddress address) {
        this.address = address;
    }

    public static EventLoopGroup eventLoopGroup(int threads, String threadFactoryName) {
        ThreadFactory threadFactory = new DefaultThreadFactory(threadFactoryName, true);
        return shouldEpoll() ? new EpollEventLoopGroup(threads, threadFactory) :
                new NioEventLoopGroup(threads, threadFactory);
    }
    private static boolean shouldEpoll() {


        return false;
    }
    public void proxy(ProxyContext ctx){
        Bootstrap bootstrap = new Bootstrap();
        String reqUri = ctx.getRequest().uri();
        String destUri = ctx.getProxyConfig().getApiConfig(reqUri).getDestUri();
        //String destUri = ctx.getRequest().uri();
        bootstrap.group(EVENT_LOOP_GROUP).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast("encoder", new HttpRequestEncoder());
                ch.pipeline().addLast("decoder",new HttpResponseDecoder());
                /*ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));*/

                ch.pipeline().addLast("logger",new LoggingHandler());
                ch.pipeline().addLast(new ClientHandler(destUri,ctx, ctx.getRequest()));
            }
        });
        bootstrap.connect(address);
    }

    public static class ClientHandler extends ChannelInboundHandlerAdapter {

        static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
        ChannelFutureListener writeListener = new  ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    logger.error("write request error",future.cause());
                }
            }
        };
        private ProxyContext serverCtx;
        private FullHttpRequest msg;
        String destUri;
        private ArrayList<HttpContent> contents = new ArrayList<>();
        public   ClientHandler(String uri,ProxyContext ctx,FullHttpRequest msg){
            this.serverCtx = ctx;
            this.msg = msg;
            this.destUri = uri;
        }
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Promise<Object> promise = ctx.executor().newPromise();
            //super.channelActive(ctx);
            System.out.println(" the channel is active!!!");
            String token = msg.headers().get("x-access-token");
            /*if(token == null){
                HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.UNAUTHORIZED);
                serverCtx.writeAndFlush(response);
            }*/
            HttpRequest request = msg;
            request.setUri(destUri);
            request.headers().add("Host", "127.0.0.1");

            ctx.writeAndFlush(request).addListener(writeListener);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            logger.info("channel read msg::"+msg);
            if (msg instanceof HttpResponse) {
               serverCtx.writeAndFlush(msg).addListener(writeListener);
            } else if (msg instanceof DefaultLastHttpContent) {
                //最后的消息
                serverCtx.writeAndFlush(msg).addListener(writeListener);
                ctx.channel().close();

            } else if (msg instanceof HttpContent) {
                serverCtx.writeAndFlush(msg).addListener(writeListener);
            }
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
}
