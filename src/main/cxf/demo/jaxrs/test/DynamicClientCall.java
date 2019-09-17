package test;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.ServiceInfo;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class DynamicClientCall {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	/*	 Bus bus = CXFBusFactory.getThreadDefaultBus();
	JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:9010/hello?wsdl");
		client.getInInterceptors().add(new LoggingInInterceptor()) ;
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		 QName opName = new QName("http://test/", "hello");
		Object[] res = client.invoke(opName, " 你ssss");
		System.out.println("Echo response: " + res[0]);*/
		 String  wsdlURL = "http://localhost:8081/spring_cxf?wsdl";
	        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
	  Client client =      factory.createClient(wsdlURL);
	      Object[] ret =  client.invoke(new QName("http://test/", "hello"), "hello");
	      System.out.println(ret[0]);
		//callPersonService();
		//callPersonService1();
	}
	public static void callPersonService() throws Exception{
		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
		ClassLoader classLoader = 	Thread.currentThread().getContextClassLoader();
		Client client =  factory.createClient("http://localhost:9010/hello?wsdl",classLoader);
		ClassLoader loader1 = 	Thread.currentThread().getContextClassLoader();
		Object person1 = loader1.loadClass("test.service.Person").newInstance();
		//
		QName qName = new QName("http://service.test/","person");
		 //JAXBElement jaxbPerson = new JAXBElement(qName, person1.getClass(), person1);
		// System.out.println(convertToXml(jaxbPerson));;
		JSONObject object = JSONObject.fromObject(person1);
		System.out.println(object.toString());;
		String xml =  new XMLSerializer().write(object);
		System.out.println(xml);
	//	Object person = classLoader.loadClass("test.service.Person").newInstance();
	//	Method setPerson = person.getClass().getMethod("setName", String.class);
	//	setPerson.invoke(person, "zhang san");
		//ClassLoader loader =  Thread.currentThread().getContextClassLoader();
	//	QName opName = new QName("http://service.test/", "hello");
	//	Object[] res = client.invoke(opName, "21");
	//	Class p = loader.loadClass("test.service.Person");
		//System.out.println("Echo response: " + res[0]);
	}
	public static String convertToXml(Object obj) {  
        // 创建输出流  
        StringWriter sw = new StringWriter();  
        try {  
            // 利用jdk中自带的转换类实现  
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
        return sw.toString();  
    }  
	public static void callPersonService1() throws Exception{
		   String  wsdlURL = "http://localhost:8081/spring_cxf?wsdl";
	        System.out.println(wsdlURL);
	        
	        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
	        Client client = factory.createClient(wsdlURL);
	        ClientImpl clientImpl = (ClientImpl) client;
	        Endpoint endpoint = clientImpl.getEndpoint();
	        ServiceInfo serviceInfo = endpoint.getService().getServiceInfos().get(0);
	        QName bindingName = new QName("http://service.my/", 
	            "PersonServiceSoapBinding");
	        BindingInfo binding = serviceInfo.getBinding(bindingName);
	        //{
	        QName opName = new QName("http://service.my/", "getPersonName");
	        BindingOperationInfo boi = binding.getOperation(opName);
	        BindingMessageInfo inputMessageInfo = boi.getInput();
	        List<MessagePartInfo> parts = inputMessageInfo.getMessageParts();
	        // only one part.
	        MessagePartInfo partInfo = parts.get(0);
	        Class<?> partClass = partInfo.getTypeClass();//class my.service.GetPersonName
	        System.out.println(partClass.getCanonicalName()); // GetAgentDetails
	        Object inputObject = partClass.newInstance();
	        // Unfortunately, the slot inside of the part object is also called 'part'.
	        // this is the descriptor for get/set part inside the GetAgentDetails class.
	        PropertyDescriptor partPropertyDescriptor = new PropertyDescriptor("arg0", partClass);//arg0对应person类型
	        // This is the type of the class which really contains all the parameter information.
	        Class<?> partPropType = partPropertyDescriptor.getPropertyType(); // class my.service.Person
	        System.out.println(partPropType.getCanonicalName());
	        Object inputPartObject = partPropType.newInstance();
	        partPropertyDescriptor.getWriteMethod().invoke(inputObject, inputPartObject);
	        PropertyDescriptor numberPropertyDescriptor = new PropertyDescriptor("name", partPropType);
	        numberPropertyDescriptor.getWriteMethod().invoke(inputPartObject, "张三");

	        Object[] result = client.invoke(opName, inputPartObject);
	        Class<?> resultClass = result[0].getClass();
//	        System.out.println(resultClass.getCanonicalName()); // GetAgentDetailsResponse
//	        PropertyDescriptor resultDescriptor = new PropertyDescriptor("agentWSResponse", resultClass);
//	        Object wsResponse = resultDescriptor.getReadMethod().invoke(result[0]);
//	        Class<?> wsResponseClass = wsResponse.getClass();
//	        System.out.println(wsResponseClass.getCanonicalName());
//	        PropertyDescriptor agentNameDescriptor = new PropertyDescriptor("agentName", wsResponseClass);
//	        String agentName = (String)agentNameDescriptor.getReadMethod().invoke(wsResponse);
//	        System.out.println("Agent name: " + agentName);
	}
	//public static void allPerson
}
