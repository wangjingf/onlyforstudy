package io.study.gateway.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.study.gateway.client.ClientTimeoutHandler;
import io.study.gateway.client.CloseOnIdleHandler;

public class OriginChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast("encoder", new HttpRequestEncoder());
        ch.pipeline().addLast("decoder",new HttpResponseDecoder());
        ch.pipeline().addLast("loggingHandler",new LoggingHandler());
        ch.pipeline().addLast("clientTimeoutHandler",new ClientTimeoutHandler());
        ch.pipeline().addLast("idleStateHandler",new IdleStateHandler(3000,3000,3000));
        ch.pipeline().addLast("closeOnIdleHandler",new CloseOnIdleHandler());
        /*ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));*/

        ch.pipeline().addLast("logger",new LoggingHandler());

    }
}
