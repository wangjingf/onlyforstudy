package study.eventbus;

import java.lang.reflect.Method;

//订阅者
public class Subscriber {

    //订阅事件的类
    private final Object target;

    //订阅事件的方法
    private final Method method;

    public Subscriber(Object target, Method method) {
        if (target == null || method == null) {
            throw new IllegalArgumentException("Target object and method must not be null");
        }
        this.target = target;
        this.method = method;
        //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查,可以提高性能
        this.method.setAccessible(true);
    }

    public void invoke(Object parameter) throws Exception {
        method.invoke(target, parameter);
    }
}
