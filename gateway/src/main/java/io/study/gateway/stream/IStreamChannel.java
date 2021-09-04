package io.study.gateway.stream;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicBoolean;

public interface IStreamChannel{
    public Channel getChannel();
    public void suspendRead();
    public void resumeRead();
}
