package test;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.DAO.Person;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;

public class TestJavaBeanToXml {
	public static void main(String[] args) throws IOException{
		 StringWriter sw = new StringWriter();  
	        try {  
	            // 利用jdk中自带的转换类实现  
	        	Person obj = new Person();
	            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
	  
	            Marshaller marshaller = context.createMarshaller();  
	            // 格式化xml输出的格式  
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
	                    Boolean.TRUE);  
	            // 将对象转换成输出流形式的xml  
	            marshaller.marshal(obj, sw);  
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }  
	        System.out.println(sw.toString());
	}
}
