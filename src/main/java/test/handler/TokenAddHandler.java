package test.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class TokenAddHandler implements SOAPHandler<SOAPMessageContext> {
	public static Logger logger = LoggerFactory.getLogger(TokenAddHandler.class);
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Map<String,Object> headers =  (Map<String, Object>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
		
		List<String> list = new ArrayList<String>();
		list.add("bear xxxxx");
		if(headers!=null)
			headers.put("authoization", list);
		logger.info("--------------------------"+context);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}


}
