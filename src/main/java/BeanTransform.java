import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;

import com.DAO.Job;
import com.DAO.Person;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class BeanTransform {
	static Object instanceClass(Class clazz) throws InstantiationException, IllegalAccessException{
		Object instance = null;
		if(clazz.isArray()){
			Object array = Array.newInstance(clazz.getComponentType(), 1);
			Object value = instanceClass(clazz.getComponentType());
			Array.set(array, 0, value);
			instance =  array;
		}else if(clazz.isAssignableFrom(Collection.class) ){//
			return null;
		}else if(isBasicType(clazz)){
			
		}
		return null;
	}
	public static boolean isBasicType(Class clazz){
		Class[] basicType = new Class[]{
				String.class,Double.class,double.class,
				int.class,Integer.class,float.class,Float.class,
				Long.class,long.class,byte.class,Byte.class
				
		};
		return Arrays.asList(basicType).contains(clazz);
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Person p = new Person();
		p.setName("name");
		p.setJob(new Job());
		List<Person> friends = new ArrayList<Person>();
		friends.add(new Person());
		p.setFriends(friends);
		JSONObject object = JSONObject.fromObject(p);
		
		System.out.println(object.toString());;
		String xml =  new XMLSerializer().write(object);
		//System.out.println(transformJavaBean(p));
		System.out.println(xml);
		
		/*XStream xs = new XStream();

		// OBJECT --> XML
		String result = xs.toXML(p);
		System.out.println(result);*/
	}
	public static String transformJavaBean(Object javaBean) throws IOException{
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = new XMLWriter();
		Element e = new BaseElement("person");
		writer.write(e);
		writer.setWriter(stringWriter);
		stringWriter.close();
		return stringWriter.toString();
	}
}
