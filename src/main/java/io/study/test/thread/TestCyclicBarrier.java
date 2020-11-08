package io.study.test.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(12, new Runnable() {
            @Override
            public void run() {
                System.out.println("isBroker");
            }
        });
        for (int i = 0; i < 12; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(100);
    }
}
