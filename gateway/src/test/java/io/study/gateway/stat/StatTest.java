package io.study.gateway.stat;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import junit.framework.TestCase;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * counter的性能并没有比AtomicInteger类强，为什么还要用这个类呢
 *
 *这里说counter性能更好：：https://zhuanlan.zhihu.com/p/45489739
 */
public class StatTest extends TestCase {
    /**
     * 单线程下counter的性能是弱于AtomicInteger的
     */
    public void testSinleThreadCounter(){
        Counter counter = MetricSystem.newCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            counter.inc();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public void testSinleThreadAtomicCounter(){
        AtomicInteger counter = new AtomicInteger();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            counter.addAndGet(1);
        }
        System.out.println(System.currentTimeMillis() - start);

    }
    ExecutorService executors = Executors.newFixedThreadPool(4);
    /**
     * 多线程下耗时574
     */
    public void testMultiThreadCounter() throws ExecutionException, InterruptedException {
        Counter counter = MetricSystem.newCounter();
        long start = System.currentTimeMillis();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                counter.inc(1);
            }
        };
        for (int i = 0; i < 100000; i++) {
            Future future =executors.submit(task);
            future.get();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public void testMultiThreadAtomicCounter() throws ExecutionException, InterruptedException {
        AtomicLong counter = new AtomicLong();
        long start = System.currentTimeMillis();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                counter.addAndGet(1);
            }
        };
        for (int i = 0; i < 100000; i++) {

            Future future = executors.submit(task);
            future.get();
        }
        System.out.println(System.currentTimeMillis() - start);

    }

    public void testReporter() throws InterruptedException {
        MetricRegistry registry = new MetricRegistry();
        Counter counter = registry.register(name(StatTest.class, "cache-evictions"), new Counter());
        counter.inc();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
        Thread.sleep(5000);

    }
}
