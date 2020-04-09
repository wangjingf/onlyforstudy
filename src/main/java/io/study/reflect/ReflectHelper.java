package io.study.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectHelper {
    public static Object invokeMethod(Object o,String methodName){
        return invokeMethod(o,methodName,new Object[]{});
    }
    public static Object invokeMethod(Object o,String methodName,Object... args) {
        if(o == null){
            return null;
        }
        int length = 0;
        if(args != null ){
            length = args.length;
        }
        Class<?>[] clazz = new Class[length];
        for (int i = 0; i < length; i++) {
            if(args[i] == null){
                clazz[i] = null;
            }else{
                clazz[i] = args[i].getClass();
            }
        }
        try {
            Method method = o.getClass().getMethod(methodName,clazz);
            try {
                return method.invoke(o,args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

}
