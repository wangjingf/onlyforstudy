package io.study.gateway.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.study.gateway.proxy.ProxyProtocol;

import java.util.Map;

public class SimpleHttpServer {
    ServerBootstrap server = null;
    public void start() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//
         server =  new ServerBootstrap();
        server.group(bossGroup, bossGroup).option(ChannelOption.SO_BACKLOG,1024).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));
                        ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        ch.pipeline().addLast("loggerHandler",new LoggingHandler());
                        ch.pipeline().addLast("fileServerHandler",new HttpRequestHandler(responseMap));

                    }
                });
        try {
            ChannelFuture future = server.bind(port).sync();
            System.out.println("文件服务器启动了:port="+port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    Map<String, FullHttpResponse> responseMap = null;
    int port;
    public  SimpleHttpServer(int port,Map<String, FullHttpResponse> responseMap){
        this.responseMap = responseMap;
        this.port = port;
    }
    public static void main(String[] args) throws InterruptedException{


        SimpleHttpServer server = new SimpleHttpServer(80,ProxyResponseMap.responseMap);
        server.start();
     }
}
