package io.study.gateway.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPoolHandler extends ChannelInboundHandlerAdapter {
    static final Logger logger = LoggerFactory.getLogger(ConnectionPoolHandler.class);
    static final AttributeKey RESPONSE_KEY = AttributeKey.valueOf("http_response");
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            logger.info("gateway.close_connection_because_idle");
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
                closeConnection(ctx,true);
            }else{
                closeConnection(ctx,false);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
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
        ctx.close();
    }
}
