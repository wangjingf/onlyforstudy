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
	            // ����jdk���Դ���ת����ʵ��  
	        	Person obj = new Person();
	            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
	  
	            Marshaller marshaller = context.createMarshaller();  
	            // ��ʽ��xml����ĸ�ʽ  
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
	                    Boolean.TRUE);  
	            // ������ת�����������ʽ��xml  
	            marshaller.marshal(obj, sw);  
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }  
	        System.out.println(sw.toString());
	}
}
