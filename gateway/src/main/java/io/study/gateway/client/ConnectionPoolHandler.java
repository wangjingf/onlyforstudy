package io.study.gateway.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.proxy.ProxyEndpoint;
import io.study.gateway.proxy.StreamContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPoolHandler extends ChannelInboundHandlerAdapter {

    public ConnectionPoolHandler(){
    }
    static final Logger logger = LoggerFactory.getLogger(ConnectionPoolHandler.class);
    static final AttributeKey RESPONSE_KEY = AttributeKey.valueOf("http_response");
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            logger.info("connectionPool.close_connection_because_idle");
            closeConnection(ctx,true);
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpResponse){
            ctx.channel().attr(RESPONSE_KEY).set(msg);
        }
        if(msg instanceof LastHttpContent){
            HttpResponse response = (HttpResponse) ctx.channel().attr(RESPONSE_KEY).get();
            if(isCloseHeader(response)){
                logger.info("connectionPool.close_channel_because_close_header");
                closeConnection(ctx,true);
            }else{
                logger.info("connectionPool.release_channel_on_request_end");
                closeConnection(ctx,false);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("connectionPool.close_channel_because_exception",cause);
        closeConnection(ctx,true);
    }

    private boolean isCloseHeader(HttpResponse response) {
        String connection = response.headers().get(HttpHeaderNames.CONNECTION);
        return "close".equalsIgnoreCase(connection);
    }

    /**
     * // TODO: 2021/8/9 把连接池管理加上
     * @param ctx
     */
    void closeConnection(ChannelHandlerContext ctx,boolean forceClose){
        ProxyEndpoint endpoint = (ProxyEndpoint) ctx.channel().attr(GatewayConstant.KEY_PROXY_ENDPOINT).get();
        StreamContext streamContext = endpoint.getContext();
        if(forceClose){
            streamContext.getToChannel().close();
        }else{
            streamContext.getToChannel().passivate();

        }
    }
}
