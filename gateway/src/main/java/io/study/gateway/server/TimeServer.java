package io.study.gateway.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class TimeServer {
	public void bind(int port) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChildChannelHandler());
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			// TODO Auto-generated method stub
			ch.pipeline().addLast(new TimeServerHandler());
		}
		
	}
	public static void main(String[] args) throws InterruptedException{
		int port = 8080;
		if(args !=null &&args.length>0){
				port = Integer.valueOf(args[0]);
		}
		new TimeServer().bind(port);
	}
}
