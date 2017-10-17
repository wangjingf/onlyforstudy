package service;

import java.util.List;
import java.util.Map;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

public class CxfModelBuilder {
	public static void main(String[] args) throws WSDLException{
		 WSDLFactory factory = WSDLFactory.newInstance();  
		   WSDLReader reader=factory.newWSDLReader();  
		   Definition def =   reader.readWSDL("http://localhost:9010/hello?wsdl");
		   Map services =  def.getAllServices();
		   for(Object obj : services.values()){
			   Service service = (Service) obj;
			   Map ports = service.getPorts();
			   for(Object object : ports.values()){
				   Port port = (Port) object;
				   List<BindingOperation> operations = port.getBinding().getBindingOperations();
				   for(BindingOperation operation : operations){
					  System.out.print("operation name is :" + operation.getName());
					  Map parts =  operation.getOperation().getInput().getMessage().getParts();
					  int i = 0;
					  for(Object p : parts.values()){
						  Part  part = (Part) p;
						  QName element =  part.getElementName();
						  QName  type = part.getTypeName();
						  if(element!=null)
						  System.out.print("part " + i + "is "+part.getName());
					  }
				   }
			   }
		   }
	}
}
