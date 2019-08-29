package study.conductor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkflowTaskCoordinator {
    ExecutorService executorService = null;
    ScheduledExecutorService scheduledExecutorService = null;
    public void init(){
        scheduledExecutorService.schedule((Runnable) () -> execute(),  1000, TimeUnit.SECONDS);
    }
    void execute(){
        System.out.println("执行完毕!");
    }
}
