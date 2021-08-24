package io.study.gateway.proxy;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.channel.ChannelManager;
import io.study.gateway.client.ConnectionPool;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.config.ProxyConfig;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.message.http.HttpMessageInfo;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.transport.ClientResponseHandler;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyFilter implements IFilter {
    IRegistry registry = null;
    ChannelManager channelManager = null;
    static final Logger logger = LoggerFactory.getLogger(ProxyFilter.class);
    @Override
    public Promise<HttpResponseInfo> filter(StreamContext context, IFilterChain filterChain) {
        String uri = context.getRequest().getRequest().uri();
        ProxyConfig config = registry.getConfig(uri);
        Promise result = context.fromChannel.newPromise();

        ProxyEndpoint endpoint = new ProxyEndpoint(context,result);
        Promise<PooledConnection> connectionPromise = channelManager.acquire(context, config.getApiConfig(uri));
        if(connectionPromise.isDone()){

        }else{
            connectionPromise.addListener(new GenericFutureListener<Future<? super PooledConnection>>() {
                @Override
                public void operationComplete(Future<? super PooledConnection> future) throws Exception {
                    if(future.isSuccess()){
                        PooledConnection connection = (PooledConnection) future.getNow();
                        connection.getChannel().pipeline().addLast(new ClientResponseHandler(endpoint));
                        context.setTargetAddress(connection.getChannel().localAddress());
                        context.setToChannel(connection.getChannel());
                       endpoint.proxy();
                    }
                }
            });
        }
        return result;
    }



}
