package wsdl;


import javax.xml.namespace.QName;

import org.reficio.ws.SoapContext;
import org.reficio.ws.builder.SoapBuilder;
import org.reficio.ws.builder.SoapOperation;
import org.reficio.ws.builder.core.Wsdl;
import org.reficio.ws.client.core.SoapClient;
import org.reficio.ws.server.core.SoapServer;
import org.reficio.ws.server.responder.AutoResponder;

public class SoapMessageBuildTest {
	public static void main(String[] args){
		Wsdl wsdl = Wsdl.parse("http://localhost:9010/hello?wsdl");
	    wsdl.printBindings();
	    QName binding = new QName("http://impl.service.test/", "HelloWorldPortBinding");
	    SoapBuilder builder =  wsdl.getBuilder(binding);
	    SoapOperation operation  = builder.operation().name("getName").find();;;
	    SoapContext context = SoapContext.builder()
	            .alwaysBuildHeaders(true)
	            .buildOptional(true)
	            .exampleContent(false)
	            .typeComments(true)
	            .valueComments(true)
	            .build();
	    String requestMessage = builder.buildInputMessage(operation,context);
	    System.out.println(requestMessage);
	   /* SoapBuilder builder = wsdl.binding()
	    	.localPart("CurrencyConvertorSoap")
	    	.find();
	    SoapOperation operation = builder.operation()
	    	.soapAction("http://www.webserviceX.NET/ConversionRate")
	    	.find();
	    String requestMessage = builder.buildInputMessage(operation);
	    System.out.println(requestMessage);*/
	}
}
