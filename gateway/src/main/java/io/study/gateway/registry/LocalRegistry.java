package io.study.gateway.registry;

import com.codahale.metrics.*;
import com.jd.vd.common.util.StringHelper;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LocalRegistry extends AbstractRegistry {
    Map<String/*domain*/,ApiConfig> registry = new ConcurrentHashMap<>();
    MetricRegistry metricRegistry = new MetricRegistry();

    @Override
    public void start() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);
    }

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
    public void setData(String key, Object data) {
    }


    public void register(String domain,ApiConfig apiConfig){
        registry.put(domain,apiConfig);
    }


}
