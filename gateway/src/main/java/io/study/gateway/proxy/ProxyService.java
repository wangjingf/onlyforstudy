package io.study.gateway.proxy;

import com.jd.vd.common.util.StringHelper;

import io.netty.channel.*;

import io.netty.handler.codec.http.*;

import io.study.gateway.balance.LoadBalanceContext;

import io.study.gateway.config.ProxyConfig;

import io.study.gateway.interceptor.IFilter;

import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.registry.IRegistry;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyService {
    static final Logger logger = LoggerFactory.getLogger(ProxyService.class);
    ChannelHandlerContext serverCtx;
    IRegistry registry;

    ProxyFactory factory = null;

    List<IFilter> filters = new ArrayList<>();
    Map<ProxyConfig, LoadBalanceContext> balanceContextMap = new ConcurrentHashMap<>();

    public ProxyService(ChannelHandlerContext serverCtx) {
        this.serverCtx = serverCtx;
    }

    void notFound() {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        serverCtx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    logger.error("write request error", future.cause());
                }
            }
        });
    }



    /**
     * 过滤并执行代理
     *
     * @param request
     */
    public void start(FullHttpRequest request) {
        ProxyContext context = new ProxyContext();
        String uri = context.getRequest().uri();
        if (StringUtils.isEmpty(uri)) {
            notFound();
            return;
        }
        if (uri.indexOf("/") != -1) {
            uri = StringHelper.firstPart(uri, '/');
        }
        ProxyConfig proxyConfig = registry.resolveTarget(uri);
        ProxyContext proxyContext = new ProxyContext();
        proxyContext.setProxyConfig(proxyConfig);
        proxyContext.setRequest(context.getRequest());
        for (IFilter filter : filters) {
            filter.filter(context);
        }
        ProxyInvoker invoker = factory.createProxyInvoker(proxyConfig);
        invoker.invoke(new ProxyContext());
        //限流
        //鉴权
        //协议转换
        //统计

    }

}
