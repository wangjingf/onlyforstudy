package study.future;

import io.netty.util.concurrent.CompleteFuture;
import junit.framework.TestCase;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * 怎么感觉有点类似js里的Promise呢？ https://blog.csdn.net/weixin_34928522/article/details/114774859
 */
public class CompleteFutureTests extends TestCase {
    public void testCompleteFuture() throws ExecutionException, InterruptedException {
        CompletableFuture future = new CompletableFuture();
        Executors.newCachedThreadPool().submit(()->{
            try {
                Thread.sleep(1000);
                future.complete("a");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Object result = future.get();
        assertEquals(result,"a");
    }
    public void testDirectCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.completedFuture("a");
        String s = future.get();
        assertEquals("a",s);
    }
    public void testSupply() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        Object result = future.get();
        assertEquals(1,result);
    }

    /**
     * 类似js里的Promise.then?
     */
    public  void testThen(){
        CompletableFuture.runAsync(()->{}).thenApply((result)->{
            System.out.println("complete");
            return null;
        });
    }

    /**
     * 取2个任务的结果
     */
    public void testCombine() throws ExecutionException, InterruptedException {
        CompletableFuture future1 = CompletableFuture.supplyAsync(()->{
            return 1;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            return 2;
        });
        CompletableFuture future3 = future2.thenCombine(future1, (result2, result1) -> {
            /*assertEquals(2,result2);
            assertEquals(1,result1);*/
            System.out.println("result2::" + result2 + " result1::" + result1);
            return null;
        });
        future3.get();
    }

    /**
     * compose里可以再返回一个CompletableFuture对象，有点类似js的Promise.then里再返回一个promise对象
     */
    public void testThenCompose() throws ExecutionException, InterruptedException {

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        assertEquals("Hello World", result.get());



    }
}
