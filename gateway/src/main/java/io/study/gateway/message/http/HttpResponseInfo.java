package io.study.gateway.message.http;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

public class HttpResponseInfo {
    HttpResponse response;
    boolean isSuccess;
    Throwable e;
    HttpContent content;
    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public HttpContent getContent() {
        return content;
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }
}
