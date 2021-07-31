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

import java.util.ArrayList;
import java.util.List;

public class ClusterProxyInvoker implements ProxyInvoker {
    public ClusterProxyInvoker(List<ProxyInvoker> invokers,BalancePolicy balancePolicy) {
        this.invokers = invokers;
        this.policy = balancePolicy;
        balance = BalanceFactory.getLoadBalance(balancePolicy);
        assert balance != null;
    }
    ILoadBalance balance = null;
    BalancePolicy policy = null;
    List<ProxyInvoker> invokers = new ArrayList<>();
    @Override
    public void invoke(ProxyContext context) {
        LoadBalanceContext balanceContext = new LoadBalanceContext();
        balanceContext.setDomain(context.getDomain());
        balanceContext.setPath(context.getPath());
        ProxyInvoker invoker = balance.select(invokers, balanceContext);
        if(invoker == null){
            context.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(500)));
        }
        invoker.invoke(context);
    }

    @Override
    public String getUrl() {
        return null;
    }
}
