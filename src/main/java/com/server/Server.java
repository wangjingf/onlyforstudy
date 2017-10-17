package com.server;  
  
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;  
  
import com.DAO.Person;  
import com.DAO.PersonService;
  
public class Server {  
  
    public static void main(String[] args) {  
        PersonService service = new PersonService();  
  
        // Service instance  
        JAXRSServerFactoryBean restServer = new JAXRSServerFactoryBean();  
        restServer.setResourceClasses(Person.class);  
        restServer.setServiceBean(service);  
        restServer.setAddress("http://localhost:9999/");  
        restServer.create();  
    
    }}  