package io.study.gateway.proxy;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
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
import io.study.gateway.client.CloseOnIdleHandler;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyServer {
    GatewaySetting gatewaySetting;
    FilterLoader filterLoader = null;
    IRegistry registry;
    public ProxyServer(IRegistry registry,GatewaySetting gatewaySetting,FilterLoader filterLoader){
        this.gatewaySetting = gatewaySetting;
        this.registry = registry;
        this.filterLoader = filterLoader;
    }


    public void start() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//accepterGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();//
        ServerBootstrap server =  new ServerBootstrap();

        server.group(bossGroup, bossGroup)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_BACKLOG,1024)
                .option(ChannelOption.AUTO_READ,true)
                .option(ChannelOption.SO_LINGER, -1)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        //ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));

                        ch.pipeline().addLast("httpEncoder",new HttpResponseEncoder());
                        ch.pipeline().addLast("protocolAggregator",new ProtocolMessageAggregator(gatewaySetting.getHttpMaxChunkSize(),registry));
                        ch.pipeline().addLast("httpChunked",new ChunkedWriteHandler());
                        ch.pipeline().addLast("log",new LoggingHandler(LogLevel.TRACE));
                        ch.pipeline().addLast("idleHandler",new IdleStateHandler(0,0,gatewaySetting.getServerIdleTimeout()/1000));
                        ch.pipeline().addLast("closeOnIdleHandler",new CloseOnIdleHandler());
                        ch.pipeline().addLast("proxyHandler", new ProxyHandler(registry,filterLoader));

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
    static class ProxyHandler extends ChannelInboundHandlerAdapter{
        HttpRequest httpRequest = null;
        IRegistry registry;
        DefaultFilterChain filterChain = null;
        GatewaySetting gatewaySetting;
        static  final Logger logger = LoggerFactory.getLogger(ProxyHandler.class);
        public ProxyHandler(IRegistry registry,FilterLoader filterLoader){

            this.registry = registry;
            filterChain = new DefaultFilterChain( filterLoader.getFilters(),null);
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("proxyHandler.receive_msg:msg={}",msg);
            ProxyProtocol protocol = (ProxyProtocol) ctx.channel().attr(GatewayConstant.KEY_PROXY_PROTOCOL).get();
            StreamContext streamContext = new StreamContext();
            streamContext.setFromChannel(ctx.channel());
            HttpMessageInfo request = new HttpMessageInfo();
            streamContext.setRequest(request);
            if(ProxyProtocol.Rpc.equals(protocol)){
                request.setFull(true);
                request.setRequest((FullHttpRequest) request);
                filterChain.doFilter(streamContext);
            }else{
                if (msg instanceof HttpRequest) {
                    httpRequest = (HttpRequest) msg;
                    request.setRequest((HttpRequest) httpRequest);

                }else if(msg instanceof HttpContent){
                    request.setRequest((HttpRequest) httpRequest);
                    request.setContent((HttpContent) msg);
                }
                filterChain.doFilter(streamContext);
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            //super.channelInactive(ctx);
            logger.info("proxyHandler.channel_is_active");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //super.exceptionCaught(ctx, cause);
            logger.error("proxyHandler.exception_cause",cause);
            ctx.close();
        }
    }
}
