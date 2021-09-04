package io.study.gateway.registry;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.jd.vd.common.util.StringHelper;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.ProxyConfig;

import java.util.List;

public interface IRegistry {
    public void start();
    public ApiConfig getConfig(String uri);
    public Object getData(String key);
    public void setData(String key,Object data);
    public Counter newCounter(String name);
    public Meter newMeter(String name);
    public Timer newTimer(String name);
    default String getApp(String url){
        if(StringHelper.isEmpty(url)){
            return null;
        }
        if(url.startsWith("/")){
            url = url.substring(1);
        }
        List<String> split = StringHelper.split(url, "/");
        if(split.isEmpty()){
            return null;
        }
        return split.get(0);
    }
}
