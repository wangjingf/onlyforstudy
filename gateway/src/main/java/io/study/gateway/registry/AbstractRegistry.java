package io.study.gateway.registry;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.study.gateway.config.ApiConfig;

public abstract class AbstractRegistry implements IRegistry{
    MetricRegistry metricRegistry = new MetricRegistry();
    @Override
    public Counter newCounter(String name) {
        return metricRegistry.counter(name);
    }

    @Override
    public Meter newMeter(String name) {
        return metricRegistry.meter(name);
    }

    @Override
    public Timer newTimer(String name) {
        return metricRegistry.timer(name);
    }


}
