package study.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import junit.framework.TestCase;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.Future;

public class CommandHelloWorld extends HystrixCommand<String> {
    String name;
    protected CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "Hello "+name+"!";
    }
    public static class UnitTest extends TestCase{
        
        public void testSynchronous() {
            assertEquals("Hello World!", new CommandHelloWorld("World").execute());
            assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());
        }

        
        public void testAsynchronous1() throws Exception {
            assertEquals("Hello World!", new CommandHelloWorld("World").queue().get());
            assertEquals("Hello Bob!", new CommandHelloWorld("Bob").queue().get());
        }

        
        public void testAsynchronous2() throws Exception {

            Future<String> fWorld = new CommandHelloWorld("World").queue();
            Future<String> fBob = new CommandHelloWorld("Bob").queue();

            assertEquals("Hello World!", fWorld.get());
            assertEquals("Hello Bob!", fBob.get());
        }

        
        public void testObservable() throws Exception {

            Observable<String> fWorld = new CommandHelloWorld("World").observe();
            Observable<String> fBob = new CommandHelloWorld("Bob").observe();

            // blocking
            assertEquals("Hello World!", fWorld.toBlocking().single());
            assertEquals("Hello Bob!", fBob.toBlocking().single());

            // non-blocking 
            // - this is a verbose anonymous inner-class approach and doesn't do assertions
            fWorld.subscribe(new Observer<String>() {

                @Override
                public void onCompleted() {
                    // nothing needed here
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(String v) {
                    System.out.println("onNext: " + v);
                }

            });

            // non-blocking
            // - also verbose anonymous inner-class
            // - ignore errors and onCompleted signal
            fBob.subscribe(new Action1<String>() {

                @Override
                public void call(String v) {
                    System.out.println("onNext: " + v);
                }

            });

            // non-blocking
            // - using closures in Java 8 would look like this:

            //            fWorld.subscribe((v) -> {
            //                System.out.println("onNext: " + v);
            //            })

            // - or while also including error handling

            //            fWorld.subscribe((v) -> {
            //                System.out.println("onNext: " + v);
            //            }, (exception) -> {
            //                exception.printStackTrace();
            //            })

            // More information about Observable can be found at https://github.com/Netflix/RxJava/wiki/How-To-Use

        }

    }
}
