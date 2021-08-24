package io.study.gateway.client;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 写出数据后指定的时间内需要收到数据，收不到数据就需要关闭连接
 */
public class ClientTimeoutHandler extends ChannelInboundHandlerAdapter {
}
