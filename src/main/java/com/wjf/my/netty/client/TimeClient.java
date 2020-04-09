package com.wjf.my.netty.client;

import java.util.concurrent.ExecutionException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class TimeClient {
	public void connect(int port,String host) throws InterruptedException, ExecutionException{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b =new Bootstrap();
			/**
			 * TCP_NODELAY收到请求后就立即发送
			 */
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});
			b.bind(8888);
			ChannelFuture f = b.connect(host,port).sync();
			f.addListener(new GenericFutureListener<Future<? super Void>>() {

				public void operationComplete(Future<? super Void> future) throws Exception {
					if(future.isSuccess()){
						System.out.println("succeed");
						f.channel().writeAndFlush("13").addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								System.out.println("futurefuture");
							}
						});
					}else{
						System.out.println("failure");
					}
				}
			});

			Object result =  f.get();
			f.channel().closeFuture().sync();//等待服务端链路关闭之后才退出main函数
			System.out.println("result is " + f.isSuccess());
		}finally{
			group.shutdownGracefully();//优雅退出，释放相关的资源
		}
		
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		int port = 8080;
		new TimeClient().connect(port, "127.0.0.1");
	}
}
