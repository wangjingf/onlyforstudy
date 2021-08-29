package io.study.gateway.interceptor;


import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.Promise;
import io.study.gateway.message.http.HttpMessageInfo;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.StreamContext;
import org.apache.http.HttpResponse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息不进行聚合操作
 */
public interface IFilter {
   // 获取filter的名称，方便调试呢
   default String name(){
      return this.getClass().getSimpleName();
   }
   public Promise<HttpResponseInfo> filter(StreamContext context, IFilterChain filterChain);

}
