package io.study.gateway.gateway;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class GatewayClient {
	public static void connect(int port,String host) throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b =new Bootstrap();
			/**
			 * TCP_NODELAY收到请求后就立即发送
			 */
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
					ch.pipeline().addLast(new io.netty.handler.codec.string.StringDecoder());
					ch.pipeline().addLast(new GatewayClientHandler());
				}
			});
			ChannelFuture f = b.connect(host,port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
		
	}
	public static void main(String[] args) throws InterruptedException{
		int port = 5121;
		new GatewayClient().connect(port, "127.0.0.1");
	}
	static class GatewayClientHandler extends ChannelHandlerAdapter{

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}


		
	}
}
