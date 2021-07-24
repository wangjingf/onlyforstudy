package io.study.gateway.registry;

import io.study.gateway.config.ProxyConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry implements IRegistry {
    Map<String,ProxyConfig> registry = new ConcurrentHashMap<>();
    @Override
    public ProxyConfig resolveTarget(String uri) {
        return registry.get(uri);
    }
}
