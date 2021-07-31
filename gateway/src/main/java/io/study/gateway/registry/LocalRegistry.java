package io.study.gateway.registry;

import com.jd.vd.common.util.StringHelper;
import io.study.gateway.config.ProxyConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry implements IRegistry {
    Map<String/*domain*/,ProxyConfig> registry = new ConcurrentHashMap<>();

    @Override
    public ProxyConfig getConfig(String uri) {
        if(StringHelper.isEmpty(uri)){
            return null;
        }
        if(uri.startsWith("/")){
            uri = uri.substring(1);
        }
        List<String> split = StringHelper.split(uri, "/");
        if(split.isEmpty()){
            return null;
        }
        return registry.get(split.get(0));
    }


    public void register(String domain,ProxyConfig proxyConfig){
        proxyConfig.setDomain(domain);
        registry.put(domain,proxyConfig);
    }

}
