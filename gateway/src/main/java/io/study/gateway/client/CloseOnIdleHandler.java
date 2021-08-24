package io.study.gateway.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class CloseOnIdleHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if(evt instanceof IdleStateEvent){
/*
            if(IdleState.ALL_IDLE.equals( ((IdleStateEvent) evt).state())){

            }*/
            ctx.close();
        }
       // ctx.fireUserEventTriggered()

    }
}
