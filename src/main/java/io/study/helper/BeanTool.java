package io.study.helper;


import io.study.exception.StdException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanTool {
    public static final Object[] EMPTY_ARGS = new Object[0];
    public static void setProperties(Object bean,Map<String,Object> props){
        if(props!=null){
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                setProperty(bean,entry.getKey(),entry.getValue());
            }
        }
    }
    public static void setProperty(Object bean,String name,Object value){
        if(bean  == null || name == null){
            return;
        }
        try {
            BeanUtils.setProperty(bean,name,value);
        } catch (IllegalAccessException e) {
            throw StdException.adapt(e,"bean.err_invalid_access");
        } catch (InvocationTargetException e) {
            throw StdException.adapt(e,"bean.err_invalid_invocation_target");
        }
    }
    public static Object getProperty(Object bean,String name){
        if(bean == null){
            return null;
        }else{
            try {
                //
                return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(bean,name);
            } catch (IllegalAccessException e) {
                throw StdException.adapt(e,"bean.err_invalid_access");
            } catch (InvocationTargetException e) {
                throw StdException.adapt(e,"bean.err_invalid_invocation_target");
            } catch (NoSuchMethodException e) {
                throw StdException.adapt(e,"bean.err_no_such_method");
            }
        }
    }
    public static Object getField(Object bean,String name){
        if(bean == null){
            return null;
        }
        try {
            Field field = bean.getClass().getField(name);
            field.setAccessible(true);
            return field.get(bean);
        } catch (NoSuchFieldException e) {
            throw StdException.adapt(e,"bean.err_no_such_field");
        }catch (IllegalAccessException e){
            throw StdException.adapt(e,"bean.err_illegal_access");
        }
    }
    public static PropertyDescriptor[] getProperties(Class beanClass){
        return BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(beanClass);
    }
    public static Map<String,PropertyDescriptor> getPropertiesMap(Class beanClass){
        PropertyDescriptor[] properties = getProperties(beanClass);
        Map<String,PropertyDescriptor> map = new HashMap<>();
        for (PropertyDescriptor property : properties) {
            map.put(property.getName(),property);
        }
        return map;
    }
    public static void setField(Object bean,String name,String value){
        if(bean == null || name == null){
            return ;
        }
        try {
            Field field = bean.getClass().getField("name");
            field.setAccessible(true);
            field.set(bean,value);
        } catch (NoSuchFieldException e) {
            throw StdException.adapt(e,"bean.err_no_such_field");
        } catch (IllegalAccessException e) {
            throw StdException.adapt(e,"bean.err_invalid_access");
        }
    }

    /**
     * todo 目前效率较低，需要加入缓存
     * 执行方法，注意：不匹配参数类型
     * @param bean
     * @param methodName
     * @param args
     * @return
     */
    public static Object invokeMethod(Object bean,String methodName,Object[] args){
        if(bean == null){
            return null;
        }
        if(args == null){
            args = EMPTY_ARGS;
        }
        List<Method> methodList = new LinkedList<>();
        for (Method method : bean.getClass().getMethods()) {
            if(method.getName().equals(methodName) && method.getParameterCount() == args.length){
                methodList.add(method);
            }
        }
        if(methodList.size() > 1){
            throw new StdException("bean.err_bean_find_multi_method").param("methodName",methodName);
        }
        try {
            return methodList.get(0).invoke(bean, args);
        } catch (IllegalAccessException e) {
            throw StdException.adapt(e,"bean.err_access_method");
        } catch (InvocationTargetException e) {
            throw StdException.adapt(e,"bean.err_invocation_target_exception");
        }
    }
}
