package io.study.gateway.message.http;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpMessageInfo {
    private HttpRequest request;
    private HttpContent content;
    boolean isFull;
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
        return content != null;
    }



    public boolean isLastPart() {
        return isHasContent() && content instanceof LastHttpContent;
    }

}
