package io.study.gateway.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.gateway.Gateway;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.IRequestFilter;
import io.study.gateway.interceptor.impl.DefaultFilterChain;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.stream.IStreamChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;


public class ProxyHandler extends ChannelInboundHandlerAdapter implements IStreamChannel {
    HttpRequest httpRequest = null;
    IRegistry registry;
    IRequestFilter requestFilter = null;
    DefaultFilterChain filterChain = null;
    GatewaySetting gatewaySetting;
    Channel channel = null;
    volatile boolean autoRead = true;
    Gateway gateway = null;
    static  final Logger logger = LoggerFactory.getLogger(ProxyHandler.class);
    LinkedList<Object> cacheReads = new LinkedList<>();
    public ProxyHandler(Gateway gateway){
        this.gateway = gateway;
        this.registry = gateway.getRegistry();
        filterChain = new DefaultFilterChain( gateway.getFilterLoader().getFilters(),null);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        logger.info("proxyHandler.receive_msg:msg={}",msg);
        if(requestFilter != null){
            boolean accept = requestFilter.filter(request);
            if(!accept){
                ReferenceCountUtil.release(request);
                return;
            }
        }
        if(!this.autoRead){
            this.cacheReads.add(request);
        }else{
            this.cacheReads.push(request);
            processMsg(ctx);
        }

    }
    boolean processMsg(ChannelHandlerContext ctx){
        FullHttpRequest request = null;
        while (((request= (FullHttpRequest) cacheReads.pollFirst())!=null) &&this.autoRead){
            ProxyProtocol protocol = (ProxyProtocol) ctx.channel().attr(GatewayConstant.KEY_PROXY_PROTOCOL).get();
            StreamContext streamContext = new StreamContext();
            streamContext.setFromChannel(this);
            streamContext.setRequest(request);
            filterChain.doFilter(streamContext);
        }
        return true;
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

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void suspendRead() {
        autoRead = false;
        this.channel.config().setOption(ChannelOption.AUTO_READ,false);
    }

    @Override
    public void resumeRead() {
        autoRead = true;
        this.channel.config().setOption(ChannelOption.AUTO_READ,true);
    }

    /**
     * 不可写的时候需要挂起
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        if(ctx.channel().isWritable()){
            logger.info("proxyHandler.resume_read_on_writability_changed");
            resumeRead();
        }else{
            logger.info("proxy.suspend_read_on_writability_changed");
            suspendRead();
        }
    }
}

