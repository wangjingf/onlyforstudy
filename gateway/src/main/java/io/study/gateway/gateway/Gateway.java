package io.study.gateway.gateway;

import io.study.gateway.config.GatewaySetting;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.impl.DefaultFilterChain;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.stat.MetricStreamChannelStats;

public class Gateway {
    IRegistry registry;
    FilterLoader filterLoader;
    MetricStreamChannelStats serverStats;
    GatewaySetting setting = null;

    public IRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IRegistry registry) {
        this.registry = registry;
    }

    public FilterLoader getFilterLoader() {
        return filterLoader;
    }

    public void setFilterLoader(FilterLoader filterLoader) {
        this.filterLoader = filterLoader;
    }

    public MetricStreamChannelStats getServerStats() {
        return serverStats;
    }

    public void setServerStats(MetricStreamChannelStats serverStats) {
        this.serverStats = serverStats;
    }

    public GatewaySetting getSetting() {
        return setting;
    }

    public void setSetting(GatewaySetting setting) {
        this.setting = setting;
    }
}
