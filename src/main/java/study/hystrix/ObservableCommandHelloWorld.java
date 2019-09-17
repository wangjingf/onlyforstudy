package study.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import junit.framework.TestCase;
import rx.Observable;

public class ObservableCommandHelloWorld extends HystrixObservableCommand {
    protected ObservableCommandHelloWorld(HystrixCommandGroupKey group) {
        super(group);
    }

    @Override
    protected Observable construct() {
        return Observable.just(1);
    }
    public static class Test extends TestCase{
        public void test(){
            ObservableCommandHelloWorld cmd = new ObservableCommandHelloWorld(HystrixCommandGroupKey.Factory.asKey("group1"));
            cmd.observe().subscribe(i->{
                System.out.println(i);
            });
        }
    }

    /**
     * 执行失败的可通过此方法进行恢复操作
     * @return
     */
    @Override
    protected Observable resumeWithFallback() {
        return Observable.just("success");
    }
}
