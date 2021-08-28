package io.study.gateway.proxy;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.message.http.HttpMessageInfo;

import java.net.SocketAddress;

public class StreamContext {
    PooledConnection toChannel;
    Channel fromChannel;
    SocketAddress targetAddress;
    HttpMessageInfo request;
    HttpResponse response;
    public PooledConnection getToChannel() {
        return toChannel;
    }

    public void setToChannel(PooledConnection toChannel) {
        this.toChannel = toChannel;
    }

    public Channel getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(Channel fromChannel) {
        this.fromChannel = fromChannel;
    }

    public SocketAddress getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(SocketAddress targetAddress) {
        this.targetAddress = targetAddress;
    }

    public HttpMessageInfo getRequest() {
        return request;
    }

    public void setRequest(HttpMessageInfo request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
