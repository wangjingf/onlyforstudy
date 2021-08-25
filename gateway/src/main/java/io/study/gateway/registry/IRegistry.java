package io.study.gateway.registry;

import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;

public interface IRegistry {
    public ApiConfig getConfig(String uri);
    public Object getData(String key);
    public Object setData(String key,Object data);
}
