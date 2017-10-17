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
	        // ʹ�÷��似���õ�T����ʵ����
	        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass(); // ��ȡ��ǰnew�Ķ���� ���͵ĸ��� ����
	        this.clazz = (Class<T>) pt.getActualTypeArguments()[0]; // ��ȡ��һ�����Ͳ�������ʵ����
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
