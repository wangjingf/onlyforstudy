package io.study.gateway.message.http;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

public class HttpResponseInfo {
    HttpResponse response;
    boolean isSuccess;
    Throwable cause;
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

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public HttpContent getContent() {
        return content;
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }
}
