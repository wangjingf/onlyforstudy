package io.study.gateway.common;

import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpResponse;

public class CompleteEvent {
    HttpResponse response = null;
    public CompleteEvent(DefaultHttpContent content){
        this.response = content;
    }
}
