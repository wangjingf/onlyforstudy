package io.study.gateway.proxy;

import com.jd.vd.common.util.StringHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.study.gateway.channel.ProtocolMessageAggregator;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.config.ProxyConfig;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.impl.DefaultFilterChain;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.message.http.HttpMessageInfo;
import io.study.gateway.registry.IRegistry;
import org.apache.commons.lang.StringUtils;

public class ProxyServer {
    GatewaySetting gatewaySetting;
    FilterLoader filterLoader = null;
    IRegistry registry;
    public ProxyServer(IRegistry registry,GatewaySetting gatewaySetting){
        this.gatewaySetting = gatewaySetting;
        this.registry = registry;
    }

    public void start() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//
        ServerBootstrap server =  new ServerBootstrap();

        server.group(bossGroup, bossGroup).option(ChannelOption.SO_REUSEADDR,true).option(ChannelOption.SO_BACKLOG,1024).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        //ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));

                        ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                        ch.pipeline().addLast("protocol-aggregator",new ProtocolMessageAggregator(gatewaySetting.getHttpMaxChunkSize()));
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        ch.pipeline().addLast("log",new LoggingHandler(LogLevel.TRACE));
                        ch.pipeline().addLast("idle-handler",new IdleStateHandler(0,0,gatewaySetting.getClientIdleTimeout()/1000));
                        ch.pipeline().addLast("proxyhandler", new ProxyHandler(registry,filterLoader));

                    }
                });
        try {
            ChannelFuture future = server.bind(gatewaySetting.getPort()).sync();
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
    void addFilters(){

    }
    /**
     * // TODO: 2021/8/1   空闲的时候需要关闭channel
     * @param pipeline
     */
    private void addTimeoutHandlers(ChannelPipeline pipeline) {

    }
    static class ProxyHandler extends SimpleChannelInboundHandler<HttpObject>{
        HttpRequest httpRequest = null;
        IRegistry registry;
        DefaultFilterChain filterChain = null;
        public ProxyHandler(IRegistry registry,FilterLoader filterLoader){
            this.registry = registry;
            filterChain = new DefaultFilterChain( filterLoader.getFilters(),null);
        }
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            ProxyProtocol protocol = (ProxyProtocol) ctx.channel().attr(GatewayConstant.KEY_PROXY_PROTOCOL).get();
            StreamContext streamContext = new StreamContext();
            streamContext.setFromChannel(ctx.channel());
            HttpMessageInfo request = new HttpMessageInfo();
            if(ProxyProtocol.Rpc.equals(protocol)){
                request.setFull(true);
                request.setRequest((FullHttpRequest) request);
                filterChain.doFilter(streamContext);
            }else{

                if (msg instanceof HttpRequest) {
                    httpRequest = (HttpRequest) msg;
                    request.setRequest((HttpRequest) request);
                    request.setHasContent(true);
                }else if(msg instanceof HttpContent){
                    httpRequest = (HttpRequest) msg;
                    request.setRequest((HttpRequest) request);
                    request.setContent((HttpContent) msg);
                }
                filterChain.doFilter(streamContext);
            }
        }
    }
}
