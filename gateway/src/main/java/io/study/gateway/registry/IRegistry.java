package io.study.gateway.registry;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;

public interface IRegistry {
    public void start();
    public ApiConfig getConfig(String uri);
    public Object getData(String key);
    public Object setData(String key,Object data);
    public Counter newCounter(String name);
    public Meter newMeter(String name);
    public Timer newTimer(String name);
}
