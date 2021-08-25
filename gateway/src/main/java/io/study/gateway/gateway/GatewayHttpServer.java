package io.study.gateway.gateway;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayHttpServer {


	static class ProxyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
		static final Logger logger = LoggerFactory.getLogger(ProxyServerHandler.class);
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
			System.out.println("the read msg sis ::" + msg);
			//System.out.println(msg.getUri());;
			final String uri = msg.getUri();
				synchronized (ctx.channel()){
					Bootstrap bootstrap = createBootstrap(ctx.channel().eventLoop(),ctx,msg.copy());
					bootstrap.connect("127.0.0.1", 8080);

				}


		}
		 Bootstrap createBootstrap(EventLoop eventLoop,final ChannelHandlerContext ctx,final FullHttpRequest msg){
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(eventLoop.parent()).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast("encoder", new HttpRequestEncoder());
						ch.pipeline().addLast("decoder",new HttpResponseDecoder());
						//ch.pipeline().addLast(new ProxyService.ClientHandler(ctx, msg));
					}
				});
				return bootstrap;
			}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.error("exception error",cause);
			super.exceptionCaught(ctx, cause);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			logger.info("channel close::"+ctx.name());
			super.channelInactive(ctx);
		}
	}

	/**
	 * 一个简单的代理服务器
	 * @param args
	 */
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, bossGroup).option(ChannelOption.SO_BACKLOG,1024).channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				//ch.pipeline().addLast(new LineBasedFrameDecoder(120));
				ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
				ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));
				ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
				ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());

				ch.pipeline().addLast(new ProxyServerHandler());
				
			}
		});
		try {
			ChannelFuture future = b.bind(5121).sync();
			System.out.println("代理服务器启动了。。。。。。");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

