package io.study.gateway.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseOnIdleHandler extends ChannelInboundHandlerAdapter {
    static final Logger logger = LoggerFactory.getLogger(CloseOnIdleHandler.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if(evt instanceof IdleStateEvent){
            logger.info("proxyclient.close_idle_channel:addr={}",ctx.channel().localAddress());
/*
            if(IdleState.ALL_IDLE.equals( ((IdleStateEvent) evt).state())){

            }*/
            ctx.close();
        }
       // ctx.fireUserEventTriggered()

    }
}
