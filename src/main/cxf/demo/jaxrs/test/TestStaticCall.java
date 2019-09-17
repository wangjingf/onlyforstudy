package test;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsClientFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import test.handler.TokenAddHandler;

public class TestStaticCall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JaxWsProxyFactoryBean client = new JaxWsProxyFactoryBean();
		client.setAddress("http://localhost:8081/spring_cxf");
		client.setServiceClass(HelloWorld.class);
		client.getInInterceptors().add(new LoggingInInterceptor());
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		client.getHandlers().add(new TokenAddHandler());
		HelloWorld hello = (HelloWorld) client.create();
		hello.hello("xxxxxxxxx");
	}

}
