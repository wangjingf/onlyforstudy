package study.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import junit.framework.TestCase;
import sun.rmi.server.Dispatcher;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventBusTests extends TestCase {
    public static class HelloEventListener{
        @Subscribe
        public void callback(HelloEvent event){
            System.out.println("name::"+event.getName());
        }
    }
    public static class HelloEvent{
        String name;

        public HelloEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public void testEventBus(){
        EventBus bus = new AsyncEventBus(Executors.newSingleThreadExecutor());
        bus.register(new HelloEventListener());
        bus.post(new HelloEvent("helloWorld"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
