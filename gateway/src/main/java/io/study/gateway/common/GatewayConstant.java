package io.study.gateway.common;

import io.netty.util.AttributeKey;

public interface GatewayConstant {
    static final AttributeKey KEY_PROXY_PROTOCOL = AttributeKey.valueOf("proxy_protocol");
}
