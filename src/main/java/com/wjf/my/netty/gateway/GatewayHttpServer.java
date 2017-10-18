package com.wjf.my.netty.gateway;



import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedWriteHandler;

public class GatewayHttpServer {
	static class ClientHandler extends ChannelInboundHandlerAdapter {
		private ChannelHandlerContext serverCtx;
		private String uri;
		public ClientHandler(ChannelHandlerContext ctx,String uri){
			this.serverCtx = ctx;
			this.uri = uri;
		}
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			System.out.println(" the channel is active!!!");
			HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);
			request.headers().add("Host", "127.0.0.1");
			ctx.writeAndFlush(request);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			//super.channelRead(ctx, msg);
			 if (msg instanceof HttpResponse) {
		            serverCtx.writeAndFlush(msg);
		        } else if (msg instanceof DefaultLastHttpContent) {
		            //最后的消息          
		            serverCtx.writeAndFlush(msg);
		            ctx.channel().close();
		 
		        } else if (msg instanceof HttpContent) {
		            serverCtx.writeAndFlush(msg);
		        }	
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			super.channelReadComplete(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			super.exceptionCaught(ctx, cause);
		}

		
	}
	static class ProxyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
		
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
			System.out.println("the read msg sis ::" + msg);
			//System.out.println(msg.getUri());;
			final String uri = msg.getUri();
			Bootstrap bootstrap = createBootstrap(ctx.channel().eventLoop(),ctx,uri);
			bootstrap.connect("127.0.0.1", 8080);
		}
		 Bootstrap createBootstrap(EventLoop eventLoop,final ChannelHandlerContext ctx,final String uri){
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(eventLoop.parent()).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast("encoder", new HttpRequestEncoder());
						ch.pipeline().addLast("decoder",new HttpResponseDecoder());
						ch.pipeline().addLast(new ClientHandler(ctx, uri));
					}
				});
				return bootstrap;
			}
	}
	
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

