package io.study.reflect;

import io.study.exception.StdException;
import io.study.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanTool {
    public static Object getPropertyValue(Object obj,String property){
        if(obj == null){
            return null;
        }
        String methodName = "get"+StringUtils.capitalize(property);

        try {
            Method method = obj.getClass().getMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (NoSuchMethodException e) {
            try {
                Field field = obj.getClass().getField(property);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e1) {
                throw StdException.adapt(e);
            } catch (IllegalAccessException e1) {
                throw StdException.adapt(e);
            }

        } catch (IllegalAccessException e) {
            throw StdException.adapt(e);
        } catch (InvocationTargetException e) {
            throw StdException.adapt(e);
        }
    }
}
