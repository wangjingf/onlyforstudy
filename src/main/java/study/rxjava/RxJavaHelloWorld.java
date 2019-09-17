package study.rxjava;

import com.netflix.hystrix.metric.HystrixCommandCompletion;
import junit.framework.TestCase;
import net.sf.ehcache.Disposable;
import rx.*;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

public class RxJavaHelloWorld  extends TestCase {
    public void testX(){

    }
    static class RangeProducer implements Producer{
        int emited = 0;
        AtomicLong item = new AtomicLong(1);
        Subscriber subscriber = null;
        public RangeProducer(Subscriber subscriber){
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            //直接调用此方法会造成stackoverflow，正确的方法是增加标志位，若request内部正在调用onNext，则跳过即可。参考OnSubscribeRange的实现
            subscriber.onNext(n);
           /*
            while(true){
                item.addAndGet(n);
                subscriber.onNext(item.get());
                emited++;
                if(item.get()>10000){
                    subscriber.onCompleted();
                }
            }*/

        }
    }
    public void testBackPressure1(){
        AtomicInteger item = new AtomicInteger(1);
        Observable novel=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
             subscriber.setProducer(new RangeProducer(subscriber));
            }
        });
        Subscriber<Integer> reader=new Subscriber<Integer>() {
            @Override
            public void onStart() {
                request(1);
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(Integer s) {
                System.out.println("the value is ："+s);
                request(1);
            }
        };
        novel.subscribe(reader);

    }
    public void testBackPressure(){
        Observable novel=Observable.range(1,10000);
        /*novel.lift(new Observable.Operator() {
            @Override
            public Object call(Object o) {
                return null;
            }
        });*/
        Subscriber<Integer> reader=new Subscriber<Integer>() {
            @Override
            public void onStart() {
                request(1);
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(Integer s) {
                System.out.println("the value is ："+s);

                request(1);
            }
        };
        novel.subscribe(reader);

    }
    public void testConcurrency(){
        Observable.range(1,10).observeOn(Schedulers.computation()).map(v->v*v).toBlocking().subscribe(System.out::println);
    }
    public void testParallel(){
        Observable.range(1,10).flatMap(v->
            Observable.just(v).subscribeOn(Schedulers.computation())
                    .map(w->w*w)
        ).toBlocking().subscribe(System.out::println);
    }
    public void testRx(){
        Observable novel=Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.setProducer(new Producer() {
                    @Override
                    public void request(long l) {
                        System.out.println("xxxxxxxxxxx");
                    }
                });
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });
        /*novel.lift(new Observable.Operator() {
            @Override
            public Object call(Object o) {
                return null;
            }
        });*/
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
    public void testSubscribe(){
        Observable.range(1,10).subscribeOn(Schedulers.computation()).toBlocking().subscribe(v->{
            System.out.println("v:::"+v);
        });
        System.out.println("112321132");
    }
    public void testXX(){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(null);
            }
        });
    }
    static class MapSubscriber extends Subscriber{
        Function func;
        Subscriber origin;
        public MapSubscriber(Function func,Subscriber origin){
            this.func = func;
            this.origin = origin;
        }

        @Override
        public void onCompleted() {
            origin.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            //origin.onError(func.apply(e));;
        }

        @Override
        public void onNext(Object o) {
            origin.onNext(func.apply(o));;
        }
    }
    public void testDefer(){
        Observable.defer(()->{
            return Observable.range(1,10);
        }).map(r->{
            System.out.println("r value is :::"+r);
            return r;
        }).toBlocking().subscribe(System.out::println);

    }
    public void testObservable(){
        Subscriber subscriber = null;
        Observable.OnSubscribe subscribe = new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> o) {
            }
        };
        Observable observable = Observable.create(subscribe);
        List item = new ArrayList();
        Observable.from(item);
    }
    public void testSubject(){
        Subject subject = new SerializedSubject<HystrixCommandCompletion, HystrixCommandCompletion>(PublishSubject.<HystrixCommandCompletion>create());
      /*  subject.share().subscribe(s->{
            System.out.println(s);
        });*/
        Observable obs = Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                return subject;
            }
        });
        obs.subscribe(System.out::println);
        subject.onNext("123");;
        subject.onNext("1234");
        subject.onCompleted();

    }
    public void testColdObs(){
        Observable observable = Observable.just(1,3,4);
        observable.subscribe(i->{
            System.out.println("subscribe1::"+i);
        });
        observable.subscribe(i->{
            System.out.println("subscribe2::"+i);
        });
    }
    public void testCode2Hot(){
        ConnectableObservable observable = Observable.just(1,3,4).observeOn(Schedulers.newThread()).publish();

        observable.subscribe(i->{
            System.out.println("subscribe1::"+i);
        });
        observable.subscribe(i->{
            System.out.println("subscribe2::"+i);
        });
        observable.connect();
        while (true);
    }
    public void testColdObservable(){

        Observable<Long> observable = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                Observable.interval(10, TimeUnit.MILLISECONDS,Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(subscriber::onNext);
            }


        }).observeOn(Schedulers.newThread());

        observable.subscribe(s->{
            System.out.println("subscriber1: "+s);
        });
        observable.subscribe(s->{
            System.out.println("   subscriber2: "+s);
        });

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testCode2HotObservable(){

        ConnectableObservable<Long> observable = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                Observable.interval(10, TimeUnit.MILLISECONDS,Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(subscriber::onNext);
            }


        }).observeOn(Schedulers.newThread()).publish();
        observable.connect();
        observable.subscribe(s->{
            System.out.println("subscriber1: "+s);
        });
        observable.subscribe(s->{
            System.out.println("   subscriber2: "+s);
        });

        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testSubject2(){
        PublishSubject<Long> subject = PublishSubject.create();
        Observable observable = Observable.create(subscriber -> {
            subscriber.onNext(12L);
        });
        observable.subscribe(subject);
        //observable.subscribe(System.out::println);
        subject.subscribe(System.out::println);
    }
}
