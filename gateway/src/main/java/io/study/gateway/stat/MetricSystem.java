package io.study.gateway.stat;

import com.codahale.metrics.*;

public class MetricSystem {
    static final MetricSystem _instance = new MetricSystem();
    public static MetricSystem instance() {
        return _instance;
    }
    public static Counter newCounter(){
        return new Counter();
    }
    public static Meter newMeter() {
        return new Meter();
    }

    public static Timer newTimer() {
        return new Timer();
    }

    public static Histogram newHistogram() {
        return new Histogram(new ExponentiallyDecayingReservoir());
    }
}
