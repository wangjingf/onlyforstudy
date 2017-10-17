package future;

import java.util.concurrent.TimeUnit;

public interface ComputeFuture {  
      
        boolean isDone();  
      
        boolean isCancelled();  
      
        boolean isSuccess();  
      
        Throwable getCause();  
      
        boolean cancel();  
      
        boolean setSuccess(Object result);  
      
        boolean setFailure(Throwable cause);  
      
        void addListener(ComputeFutureListener listener);  
      
        void removeListener(ComputeFutureListener listener);  
      
        ComputeFuture sync() throws InterruptedException;  
      
        ComputeFuture syncUninterruptibly();  
      
        ComputeFuture await() throws InterruptedException;  
          
        ComputeFuture awaitUninterruptibly();  
      
        boolean await(long timeout, TimeUnit unit) throws InterruptedException;  
      
        boolean await(long timeoutMillis) throws InterruptedException;  
      
        boolean awaitUninterruptibly(long timeout, TimeUnit unit);  
      
        boolean awaitUninterruptibly(long timeoutMillis);  
          
         Object getResult() ;  
      
    }  