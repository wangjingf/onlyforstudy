package com.wjf.my.netty.server;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jboss.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.thu.lang.exceptions.StdException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

public class HttpFileServer {
	private String host;
	private int port;
	public static final String url = "/file";
	static final Logger logger = LoggerFactory.getLogger(HttpFileServer.class); 
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
		EventLoopGroup workerGroup = new NioEventLoopGroup();//
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, bossGroup).option(ChannelOption.SO_BACKLOG,1024).channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
				ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));
				ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
				ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
				ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
				
			}
		});
		try {
			ChannelFuture future = b.bind(5121).sync();
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	  static final class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
		private String url;
		public static EventLoopGroup group = new NioEventLoopGroup();
		public HttpFileServerHandler(String url){
			url = url;
		}
		
		
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			// TODO Auto-generated method stub
			super.channelActive(ctx);
		}

        

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			super.userEventTriggered(ctx, evt);
		}



		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			super.channelWritabilityChanged(ctx);
		}



		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			// TODO Auto-generated method stub
			super.exceptionCaught(ctx, cause);
			cause.printStackTrace();
			ctx.close();
		}



		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
			String requestUrl =  URLDecoder.decode(request.uri());
			logger.info("the requestUri is :{}",requestUrl);
			if(!requestUrl.startsWith("/file")){
				redirectTo(ctx,"/file",request);
			}else if(requestUrl.equals("/file")){
				showDirectory(ctx,new File("E:/"),request);
			}else{
				File file = new File("E:/"+requestUrl.substring("/file".length()));
				if(file.isDirectory()){
					showDirectory(ctx,file,request);
				}else{
					downloadFile(ctx,file,request);
				}
			}
		}
		/**
		 * 重定位：目前未实现
		 * @param ctx
		 * @param path
		 * @param request
		 */
		private void redirectTo(ChannelHandlerContext ctx, String path, FullHttpRequest request) {
			HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.FOUND);
			
			boolean isKeepAlive = HttpUtil.isKeepAlive(request);
			if(isKeepAlive){
				response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
			}
			response.headers().set(HttpHeaderNames.LOCATION,"http://127.0.0.1:5121"+path);
			ChannelFuture future = ctx.writeAndFlush(response);
			
			if(!isKeepAlive){//非keepAlive时关闭Channel
				future.addListener(ChannelFutureListener.CLOSE);//future完毕之后关闭channel
			}
		}






		public String mimeMapping(String suffix){
			return "";
		}
		

		private void downloadFile(ChannelHandlerContext ctx, File file,FullHttpRequest request) throws IOException {
			if(file.isDirectory()){
				throw new StdException("file.CAN_err_invalid_file");
			}
			HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
			boolean isKeepAlive = HttpUtil.isKeepAlive(request);
			if(isKeepAlive){
				response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH,file.length());
			}
			ctx.write(response);
			RandomAccessFile rFile = new RandomAccessFile(file, "r");
			if(ctx.pipeline().get(SslHandler.class)==null){
				ctx.write(new DefaultFileRegion(rFile.getChannel(), 0, rFile.length()));
			}else{
				ctx.write(new ChunkedNioFile(rFile.getChannel()));
			}
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if(!isKeepAlive){//非keepAlive时关闭Channel
				future.addListener(ChannelFutureListener.CLOSE);//future完毕之后关闭channel
			}
		}



		private void showDirectory(ChannelHandlerContext ctx,File file,FullHttpRequest request) {
			if(!file.isDirectory()){
				throw new StdException("file.CAN_err_invalid_directory");
			}
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().set("content-type", "text/html;charset=utf-8");
			StringBuilder builder = new StringBuilder();
			builder.append("<!DOCTYPE html>");
			builder.append("<html>");
			builder.append("<body>");
			builder.append("<ul>");
			String path = URLEncoder.encode(file.getAbsolutePath().substring("e:/".length()));
			if(path!=null&& !path.equals("")){
				builder.append(String.format("<li><a href='%s'>../(上一级)</a></li>", "/file/"+path+"/../"));
			}
			for(File child:file.listFiles()){
				builder.append("<li>");
				builder.append(buildHrefFileLink(child));
				builder.append("</li>");
			}
			builder.append("</ul>");
			builder.append("</body>");
			builder.append("</html>");
			ByteBuf buf = Unpooled.copiedBuffer(builder,CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}
		String buildHrefFileLink(File f){
			String className="";
			if(f.isDirectory()){
				className="fileholder";
			}else{
				className="file";
			}
			String fileName="";
			if(f.isDirectory()){
				fileName=f.getName()+"(文件夹)";
			}else{
				fileName=f.getName();
			}
				
				
			return String.format("<a href='%s' class='%s'>%s</a>", "/file/"+URLEncoder.encode(f.getAbsolutePath().substring("e:/".length())),className,fileName);		
		}

		
	}
}

