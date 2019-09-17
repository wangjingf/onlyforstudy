package study.eventbus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventBus {

    //维护key是事件的class，value是这个事件所有的订阅者的映射关系
    private Map<Class<?>, List<Subscriber>> register;

    //存放事件的阻塞队列
    private BlockingQueue<Object> queue;

    public EventBus() {
        this.register = new HashMap<>();
        queue = new LinkedBlockingDeque<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //事件的消费者
                Consumer consumer = new Consumer(queue, register);
                consumer.start();
            }
        }).start();
    }

    /**
     * 把类的信息及其subscriber注册到map中去
     */
    public void register(Object listener) {
        if (listener == null) {
            return;
        }
        Class<?> clazz = listener.getClass();
        //找到当前类的所有带有Subscribe注解的方法
        for (Method method : getAnnotatedMethod(clazz)) {
            pushToResisterMap(listener, method);
        }
    }

    private void pushToResisterMap(Object listener, Method method) {
        if (listener == null || method == null) {
            return;
        }
        //获取方法的第一个参数
        Class<?> eventParamter = method.getParameterTypes()[0];
        List<Subscriber> subscriberList;
        if (register.containsKey(eventParamter)) {
            subscriberList = register.get(eventParamter);
            subscriberList.add(new Subscriber(listener, method));
        } else {
            subscriberList = new ArrayList<>();
            subscriberList.add(new Subscriber(listener, method));
            register.put(eventParamter, subscriberList);
        }
    }

    /**
     * 获取类的所有带有Subscribe注解的方法
     */
    private Set<Method> getAnnotatedMethod(Class<?> clazz) {
        Set<Method> annotatedMethods = new HashSet<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation != null && !method.isBridge()) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    /**
     * 向阻塞队列里发送事件
     */
    public void post(Object event) {
        if (event == null) {
            return;
        }
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
