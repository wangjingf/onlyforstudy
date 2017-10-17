package simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Definition;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import junit.framework.TestCase;

public class TestParseWsdl extends TestCase {
/*	public void testParseWsdl() throws WSDLException{
		try{
			 WSDLFactory factory = WSDLFactory.newInstance();  
			   WSDLReader reader=factory.newWSDLReader();  
			 //  reader.setFeature("javax.wsdl.verbose",true);  
			//   reader.setFeature("javax.wsdl.importDocuments",true);  
			   Definition def=reader.readWSDL("http://localhost:9010/hello?wsdl");  
			         //解析服务名   
			   System.out.println("---------------------------------------------");  
			   System.out.println("nService Name:");  
		}catch(Exception e){
			e.printStackTrace();
		}
		 
	}*/
	public void testDownDwslToFile(){
		File f = new File("G:/workbase/wsdlDownloadLocation");
		String wsdlUrl = "http://localhost:9010/hello?wsdl";
		try {
			 WSDLFactory factory = WSDLFactory.newInstance();  
			   WSDLReader reader=factory.newWSDLReader();  
			 //  reader.setFeature("javax.wsdl.verbose",true);  
			//   reader.setFeature("javax.wsdl.importDocuments",true); 
			   WSDLReaderWrapper readerWrapper = new WSDLReaderWrapper();
			   Definition def = readerWrapper.readWSDL("http://localhost:8010/cxf/resource/service/service.wsdl");
			   Map schemas =  readerWrapper.getAllSchemas();
			   System.out.println("-------------------");
			   for(Object entry : schemas.entrySet()){
				   Map.Entry<Object, Object> elm= (Entry<Object, Object>) entry;
				   Object key = elm.getKey();
				   Object value = elm.getValue();
				   System.out.println(key);
			   }
			   System.out.println("-------------------");
			   Map<Schema, List<SchemaReference>>  map = readerWrapper.getAllSchemaRefMap();
			   Map<Schema,List<SchemaReference>> references =  readerWrapper.getAllSchemaRefMap();
			   for(Map.Entry<Schema, List<SchemaReference>> entry : references.entrySet()){
				   Schema schema = entry.getKey();
				   
				   String schemaLocation = entry.getKey().getDocumentBaseURI();
				   URL url = new URL(null,schemaLocation);
				   System.out.println();
			   }
			   Map imports =  def.getImports();
			   System.out.println("the imports is " + imports);
			   WSDLWriter writer = factory.newWSDLWriter();
			  writer.writeWSDL(def, System.out);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	public void replaceAllFileInfo(File file,String source,String target) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = reader.readLine())!=null){
			line.replaceAll(source, target);
		}
	}
	/*public void testWddlReaderReadWeeather(){
		try{
			String weatherWsdl  = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
			 WSDLFactory factory = WSDLFactory.newInstance();  
			   WSDLReader reader=factory.newWSDLReader();  
			 //  reader.setFeature("javax.wsdl.verbose",true);  
			//   reader.setFeature("javax.wsdl.importDocuments",true);  
			   System.out.println("----------------read weather info-----------------------------");  
			   Definition def=reader.readWSDL(weatherWsdl);  
			         //解析服务名   
			  
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	/*public void testDynamicCallWebService(){
		try{
			String weatherWsdl  = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
			DynamicClientFactory factory = DynamicClientFactory.newInstance();
			Client client = factory.createClient(weatherWsdl);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void testParseXml() throws DocumentException, MalformedURLException{
		try{
			SAXReader reader = new SAXReader();
			URL url = new URL("http://localhost:9010/hello?wsdl");
			Document document = reader.read(url);
			Element element =  document.getRootElement();
			System.out.println(element.asXML());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void testUrl(){
		try {
			URL context = new URL("http://www.baidu.com");
			URL url = new URL(context,"http://www.wjf.com");
			URL relativeUrl = new URL(context,"/index.html");
			URL relativeUrl1 = new URL(context,"../index.html");
			System.out.println(url);
			System.out.println(relativeUrl);
			System.out.println(relativeUrl1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}*/
}
