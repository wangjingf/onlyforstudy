package io.study.gateway.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class TimeClientHandler extends ChannelHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);
	private final ByteBuf firstMessage;
	public TimeClientHandler(){
		byte[] req = "QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
	}
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws UnsupportedEncodingException{
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("now is :"+body);
	}
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		logger.warn("unexpected exception from downstream :"+ cause.getMessage());
		ctx.close();
	}
}
