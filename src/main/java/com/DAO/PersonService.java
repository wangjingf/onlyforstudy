package com.DAO;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/personservices") 
@Produces("application/xml")
public class PersonService {
	@GET
	@Path("/person/{id}")
	@Consumes("application/xml")
	public Person getPerson(@PathParam("id") String id){
	Person person =	 new Person();
	person.setName("name");
		 return person;
	}
	@POST
	@Path("/person/getPerson")
	@Consumes("application/json")
	public Person getPerson(Person person){
		return person;
	}
	@POST
	@Path("/person/getPersons")
	@Consumes("application/json")
	public int getPersons(List<Person> persons){
		return 1;
	}
}
