package test.impl;


import javax.jws.WebParam;

import test.HelloWorld;

public class HelloWorldImpl implements HelloWorld{

	public String hello(@WebParam(name="name")String name) {
		// TODO Auto-generated method stub
		return "xxxx"+ name;
	}

}
