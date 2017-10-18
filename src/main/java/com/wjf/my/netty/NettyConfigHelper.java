package com.wjf.my.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyConfigHelper {
	public static void configClientBootstrap(Bootstrap bootstrap,int connectTimeout){
		bootstrap
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		
		EventLoopGroup group = bootstrap.group();
		if (group instanceof NioEventLoopGroup) {
			bootstrap.channel(NioSocketChannel.class);
		} else {
			bootstrap.channel(EpollSocketChannel.class);
		}
	}
	public static Bootstrap newClientBootstrap(EventLoopGroup group){
		Bootstrap b =new Bootstrap().group(group);
		configClientBootstrap(b,10*1000);
		return b;
	}
	public static void configServerBootstrap(ServerBootstrap bootstrap){
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.SO_REUSEADDR, true)
				// Server端处于TIME_WAIT状态时也允许在端口上启动server
				.option(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.TCP_NODELAY, true);
		
		EventLoopGroup acceptorPool = bootstrap.group();
		if (acceptorPool instanceof NioEventLoopGroup) {
			bootstrap.channel(NioServerSocketChannel.class);
		} else {
			bootstrap.channel(EpollServerSocketChannel.class);
		}
		
		bootstrap.option(ChannelOption.ALLOCATOR,
				PooledByteBufAllocator.DEFAULT);
	}
}
