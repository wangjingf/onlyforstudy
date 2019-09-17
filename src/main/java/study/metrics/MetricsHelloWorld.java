package study.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import jdk.nashorn.internal.runtime.Context;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

public class MetricsHelloWorld extends TestCase {
    private final MetricRegistry metrics = new MetricRegistry();

    static final List list = new ArrayList<>();
    public void setUp(){
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }
    public void testMeters(){
        Meter requests = metrics.meter("requests");
        requests.mark();
    }
    public void testGauge(){
         Gauge<Integer> gauge = metrics.register(name(MetricsHelloWorld.class, "queue-size"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return list.size();
            }
        });
        System.out.println(gauge.getValue());

    }
    public void testCounter(){
        Counter counter = metrics.counter("counter");
        counter.inc(100);
    }

    /**
     * update方法添加要设置的值
     */
    public void testHistogram(){
        Histogram histogram = metrics.histogram("histogram");
        histogram.update(10);
        histogram.update(20);
    }
    void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Timer是一个Meter和Histogram的组合。这个度量单位可以比较方便地统计请求的速率和处理时间。对于接口中调用的延迟等信息的统计就比较方便了。
     * 如果发现一个方法的RPS（请求速率）很低，而且平均的处理时间很长，那么这个方法八成出问题了。
     */
    public void testTimer(){
        Timer timer = metrics.timer("timer");
        for(int i = 0;i<20;i++){
            Timer.Context context = timer.time();
            try{
                int time = (int)Math.random()*20;
                sleep(time);
            }finally {
                context.stop();
            }

        }

    }

    public void testHealthCheck(){
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        HealthCheck successCheck = new HealthCheck(){

            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        };
        HealthCheck failCheck = new HealthCheck(){

            @Override
            protected Result check() throws Exception {
                return Result.unhealthy("unhealth");
            }
        };
        healthCheckRegistry.register("success", successCheck);
        healthCheckRegistry.register("failure", failCheck);
        final Map<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        for (Map.Entry<String, HealthCheck.Result> entry : results.entrySet()) {
            if (entry.getValue().isHealthy()) {
                System.out.println(entry.getKey() + " is healthy");
            } else {
                System.err.println(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
                final Throwable e = entry.getValue().getError();
                if (e != null) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    protected void tearDown() throws Exception {
        Thread.sleep(2000);
    }
}
