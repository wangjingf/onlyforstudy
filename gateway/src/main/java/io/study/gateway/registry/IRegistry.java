package io.study.gateway.registry;

import io.study.gateway.config.ProxyConfig;

public interface IRegistry {
    public ProxyConfig getConfig(String uri);
}