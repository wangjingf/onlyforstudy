package io.study.gateway.channel;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;
import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.registry.IRegistry;

public class ProtocolMessageAggregator extends HttpObjectAggregator {

    IRegistry registry;
    Meter requestMeter = null;
    public ProtocolMessageAggregator(int maxContentLength,IRegistry registry) {
        super(maxContentLength);
        this.registry = registry;
        requestMeter = registry.newMeter("io.proxy.request.meter");
    }
    boolean isAggregating = false;
    @Override
    protected boolean isAggregated(HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest){
            requestMeter.mark();

            String uri = ((HttpRequest) msg).uri();
            ApiConfig config = registry.getConfig(uri);
            if(config == null){
                return false;
            }
            // rpc协议才需要聚集操作，普通http协议不需要代理操作
            if(ProxyProtocol.Rpc.equals(config.getProxyProtocol())){
                isAggregating = true;
                ctx().channel().attr(GatewayConstant.KEY_PROXY_PROTOCOL).set(ProxyProtocol.Rpc);
                return false;
            }

        }else if(msg instanceof LastHttpContent){
            isAggregating = false;
        }
        ctx().channel().attr(GatewayConstant.KEY_PROXY_PROTOCOL).set(ProxyProtocol.Http1_1);
        return isAggregating;
    }
}
