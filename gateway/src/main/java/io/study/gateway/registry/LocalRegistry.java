package io.study.gateway.registry;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.jd.vd.common.util.StringHelper;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry implements IRegistry {
    Map<String/*domain*/,ApiConfig> registry = new ConcurrentHashMap<>();
    MetricRegistry metricRegistry = new MetricRegistry();
    @Override
    public ApiConfig getConfig(String client) {
        if(StringHelper.isEmpty(client)){
            return null;
        }
        if(client.startsWith("/")){
            client = client.substring(1);
        }
        List<String> split = StringHelper.split(client, "/");
        if(split.isEmpty()){
            return null;
        }
        return registry.get(split.get(0));
    }

    @Override
    public Object getData(String key) {
        return null;
    }

    @Override
    public Object setData(String key, Object data) {
        return null;
    }

    @Override
    public Counter newCounter(String name) {
        return metricRegistry.counter(name);
    }

    @Override
    public Meter newMeter(String name) {
        return metricRegistry.meter(name);
    }


    public void register(String domain,ApiConfig apiConfig){
        registry.put(domain,apiConfig);
    }

}
