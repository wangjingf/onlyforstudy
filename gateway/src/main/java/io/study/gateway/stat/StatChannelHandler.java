package io.study.gateway.stat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.util.concurrent.GenericFutureListener;
import io.study.gateway.gateway.Gateway;

public class StatChannelHandler extends ChannelDuplexHandler {
    MetricStreamChannelStats serverStats;
    Gateway gateway = null;
    int bytesRead = 0;
    public StatChannelHandler(Gateway gateway){
        this.gateway = gateway;
        this.serverStats = gateway.getServerStats();
    }
    public StatChannelHandler() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverStats.getConnectionMeter().mark();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        serverStats.getCloseMeter().mark();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            this.bytesRead += buf.readableBytes();
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.serverStats.getReadBytesMeter().mark(this.bytesRead);

        this.bytesRead = 0;

        ctx.fireChannelReadComplete();
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
        super.exceptionCaught(ctx, cause);
        serverStats.getExceptionMeter().mark();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf)msg;
            this.serverStats.getWriteBytesMeter().mark(buf.readableBytes());
        }
        super.write(ctx, msg, promise);
    }
}
