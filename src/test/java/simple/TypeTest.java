package simple;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class TypeTest extends TestCase {
	/*public void testParameterType(){
		Map<String,List<String>> map = new LinkedHashMap<String, List<String>>();
		ParameterizedType parameterType =  (ParameterizedType) map.getClass().getGenericSuperclass();
		Type[] type = parameterType.getActualTypeArguments();
		for (int i = 0; i < type.length; i++) {
			System.out.println(type[i]);
		}
	}*/


	@SuppressWarnings("unchecked")
	 class Person<T> {
	    private Class<T> clazz;
	    public Person() {
	        // 使用反射技术得到T的真实类型
	        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass(); // 获取当前new的对象的 泛型的父类 类型
	        this.clazz = (Class<T>) pt.getActualTypeArguments()[0]; // 获取第一个类型参数的真实类型
	        System.out.println("clazz ---> " + clazz);
	    }

	}
	 class Student extends Person<Student> {
	}
	public void testPersonParameterType(){
		try {
			Student person = new Student();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
