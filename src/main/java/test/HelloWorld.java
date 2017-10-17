package test;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	public String hello(@WebParam(name="name")String name);
}
	