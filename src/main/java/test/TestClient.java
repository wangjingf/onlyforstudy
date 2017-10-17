package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
  
public class TestClient {
	//static String baseAddress = "http://localhost:8010/cxf/services/PersonService/personservices";
	static String baseAddress = "http://localhost:9000/customerservice/customers/12";
	public static void main(String[] args) throws IOException{
		  
		  
	       /* List<Object> providerList = new ArrayList<Object>();  
	  
	        Person  person= WebClient.create(baseAddress, providerList)  
	                .path("/person/1").accept(MediaType.APPLICATION_XML)  
	                .get(Person.class);  
	          System.out.println(person);*/
	        //  testPost1();
	          Response response = WebClient.create(baseAddress).get();
	  		InputStream result = (InputStream) response.getEntity();
	  		System.out.println(IOUtils.toString(result));
	}
	static void testPost() throws IOException{
		Response response = WebClient.create(baseAddress).type(MediaType.APPLICATION_JSON).path("/person/getPerson").post("{\"name\":\"wjf\",\"sex\":\"ÄÐ\"}");
		InputStream result = (InputStream) response.getEntity();
		System.out.println(IOUtils.toString(result));
	}
	static void testPost1() throws IOException{
		Response response = WebClient.create(baseAddress).type(MediaType.APPLICATION_JSON).path("/person/getPersons").post("[{\"name\":\"wjf\",\"sex\":\"ÄÐ\"}]");
		InputStream result = (InputStream) response.getEntity();
		System.out.println(IOUtils.toString(result));
	}
}
