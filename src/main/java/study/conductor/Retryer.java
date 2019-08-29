package study.conductor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * retry的基本要素:
 * 超时重试、
 * 重试次数
 * 是否失败
 * 失败后的等待时间
 */
public class Retryer<V> {
    int retryCount;
    private  AttemptTimeLimiter  attemptTimeLimiter;
    Predicate<AttemptResult> rejectPredicate;
    StopStrategies stopStrategies;
    BlockStrategies blockStrategies;
    List<RetryListener> listeners = new LinkedList<>();
    public V call(Callable<V> callable){
        long startTime = System.nanoTime();
        for (int attemptNumber = 1; ; attemptNumber++) {
            AttemptResult<V> executeResult = null;
            try {
                Object result = attemptTimeLimiter.call(callable);
                executeResult = new AttemptResult(retryCount,result,null,TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            } catch (Exception e) {
                executeResult = new AttemptResult(retryCount,null,e,TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            }
            for (RetryListener listener : listeners) {
                listener.onRetry(executeResult);
            }
            if(!rejectPredicate.test(executeResult)){
                return executeResult.getResult();
            }
            if(stopStrategies.shouldStop(executeResult)){
                throw new RetryException(attemptNumber,executeResult);
            }else{
                long sleepTime =  blockStrategies.getBlockTime(executeResult);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RetryException(attemptNumber, executeResult);
                }
            }
        }
    }
  public static interface StopStrategies{
        boolean shouldStop(AttemptResult executeResult);
  }
  public static interface BlockStrategies{
        long getBlockTime(AttemptResult executeResult);
  }
  public static interface RetryListener{
        void onRetry(AttemptResult result);
    }
    public interface AttemptTimeLimiter {
        /**
         * @param callable to subject to the time limit
         * @return the return of the given callable
         * @throws Exception any exception from this invocation
         */
        Object call(Callable  callable) throws Exception;
    }
}

