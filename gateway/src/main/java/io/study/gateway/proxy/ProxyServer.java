package io.study.gateway.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.study.gateway.channel.ProtocolMessageAggregator;
import io.study.gateway.client.CloseOnIdleHandler;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.gateway.Gateway;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.impl.DefaultFilterChain;
import io.study.gateway.message.http.HttpMessageInfo;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.stream.IStreamChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class ProxyServer {
    FilterLoader filterLoader = null;
    IRegistry registry;
    Gateway gateway = null;
    public ProxyServer(Gateway gateway) {
        this.gateway = gateway;
    }


    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//
        ServerBootstrap server = new ServerBootstrap();

        server.group(bossGroup, workerGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.SO_LINGER, -1)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));

                        ch.pipeline().addLast("httpEncoder", new HttpResponseEncoder());
                        //ch.pipeline().addLast("protocolAggregator",new ProtocolMessageAggregator(gatewaySetting.getHttpMaxChunkSize(),registry));
                        ch.pipeline().addLast("httpChunked", new ChunkedWriteHandler());
                        ch.pipeline().addLast("log", new LoggingHandler(LogLevel.TRACE));
                        ch.pipeline().addLast("idleHandler", new IdleStateHandler(0, 0, gateway.getSetting().getServerIdleTimeout() / 1000));
                        ch.pipeline().addLast("closeOnIdleHandler", new CloseOnIdleHandler());
                        ch.pipeline().addLast("proxyHandler", new ProxyHandler(gateway));

                    }
                });
        try {
            ChannelFuture future = server.bind(gateway.getSetting().getPort()).sync();
            System.out.println("文件服务器启动了。。。。。。");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
