package io.study.gateway.proxy;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.config.INode;
import io.study.gateway.stream.IStreamChannel;

public class StreamContext {
    PooledConnection toChannel;
    IStreamChannel fromChannel;
    INode targetNode;
    /*HttpMessageInfo request;
    HttpResponse response;*/
    FullHttpRequest request;
    FullHttpResponse response;
    public PooledConnection getToChannel() {
        return toChannel;
    }

    public void setToChannel(PooledConnection toChannel) {
        this.toChannel = toChannel;
    }

    public IStreamChannel getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(IStreamChannel fromChannel) {
        this.fromChannel = fromChannel;
    }

    public INode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(INode targetNode) {
        this.targetNode = targetNode;
    }

    /* public HttpMessageInfo getRequest() {
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
    }*/

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public FullHttpResponse getResponse() {
        return response;
    }

    public void setResponse(FullHttpResponse response) {
        this.response = response;
    }
}
