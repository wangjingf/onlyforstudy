package test;


import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsClientFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import test.handler.TokenAddHandler;
import test.impl.HelloWorldImpl;

public class RunService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			HelloWorld hello = new HelloWorldImpl();
			JaxWsServerFactoryBean svrFactoryBean = new JaxWsServerFactoryBean();
			svrFactoryBean.setServiceBean(hello);
			svrFactoryBean.setServiceClass(HelloWorld.class);
			List<Handler> handlers = new ArrayList<Handler>();
			handlers.add(new TokenAddHandler());
			svrFactoryBean.getInInterceptors().add(new LoggingInInterceptor());
			svrFactoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
			svrFactoryBean.addHandlers(handlers);
			svrFactoryBean.setAddress("http://localhost:8081/spring_cxf");
			svrFactoryBean.create();
			svrFactoryBean.addHandlers(handlers);
	}

}
