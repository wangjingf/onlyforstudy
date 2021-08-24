package io.study.gateway.message.http;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpMessageInfo {
    private HttpRequest request;
    private HttpContent content;
    boolean isFull;
    boolean hasContent;
    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpContent getContent() {
        return content;
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public boolean isLastPart() {
        return hasContent && content instanceof LastHttpContent;
    }

}
