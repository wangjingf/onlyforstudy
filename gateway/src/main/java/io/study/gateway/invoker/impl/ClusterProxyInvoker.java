package io.study.gateway.invoker.impl;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.study.gateway.balance.BalanceFactory;
import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.LoadBalanceContext;
import io.study.gateway.balance.impl.DefaultBalance;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.stream.ICircuitBreaker;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClusterProxyInvoker implements ProxyInvoker {

    @Override
    public void invoke(ProxyContext context) {

    }

    @Override
    public SocketAddress getAddress() {
        return null;
    }

    @Override
    public ICircuitBreaker getBreaker() {
        return null;
    }
}
