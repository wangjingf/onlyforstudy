package io.study.gateway.common;

import io.netty.util.AttributeKey;
import io.study.gateway.proxy.ProxyEndpoint;
import io.study.gateway.proxy.StreamContext;

public interface GatewayConstant {
    static final AttributeKey KEY_PROXY_PROTOCOL = AttributeKey.valueOf("proxy_protocol");
    static final AttributeKey KEY_STREAM_CONTEXT = AttributeKey.valueOf(StreamContext.class.getName());
    static final AttributeKey KEY_PROXY_ENDPOINT = AttributeKey.valueOf(ProxyEndpoint.class.getName());

    static final String REGISTRY_APPLICATION_CONFIG_PATH = "/application/config";
}
