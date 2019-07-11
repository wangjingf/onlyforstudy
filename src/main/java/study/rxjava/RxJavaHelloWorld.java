package study.rxjava;

import junit.framework.TestCase;
import net.sf.ehcache.Disposable;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class RxJavaHelloWorld  extends TestCase {
    public void testRx(){
        Observable novel=Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });
        Observer<String> reader=new Observer<String>() {

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("next:"+s);
            }
        };
        novel.subscribe(reader);
    }
}
