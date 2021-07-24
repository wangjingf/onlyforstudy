package io.study.gateway.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.study.gateway.registry.IRegistry;

public class ProxyServer {
    int port;
    IRegistry registry;
    public ProxyServer(IRegistry registry,int port){
        this.registry =registry;
        this.port = port;
    }
    public void start() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//
        ServerBootstrap server =  new ServerBootstrap();
        server.group(bossGroup, bossGroup).option(ChannelOption.SO_BACKLOG,1024).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));
                        ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        ch.pipeline().addLast("proxyhandler", new ProxyHandler(registry));

                    }
                });
        try {
            ChannelFuture future = server.bind(port).sync();
            System.out.println("文件服务器启动了。。。。。。");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    static class ProxyHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
        IRegistry registry;
        public ProxyHandler(IRegistry registry){
            this.registry = registry;
        }
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            ProxyService proxyService = new ProxyService(registry,ctx);
            proxyService.start(msg);
        }
    }
}
