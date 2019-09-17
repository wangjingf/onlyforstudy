package study.eventbus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private BlockingQueue<Object> queue;

    private Map<Class<?>, List<Subscriber>> register;

    public Consumer(BlockingQueue<Object> queue, Map<Class<?>, List<Subscriber>> register) {
        this.queue = queue;
        this.register = register;
    }

    public void start() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            Object event;
            try {
                //从队列里取事件,如果没有事件就阻塞住
                event = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            Class<?> clazz = event.getClass();
            if (!register.containsKey(clazz)) {
                System.out.println("Cannot found event's subscriber");
                continue;
            }
            //找到事件的subscriber,然后执行对应的事件
            List<Subscriber> subscriberList = register.get(clazz);
            for (Subscriber subscriber : subscriberList) {
                try {
                    subscriber.invoke(event);
                } catch (Exception e) {
                    System.out.println("Eventbus execute event failed , " + event + "/" + subscriber);
                }
            }
        }
    }
}
