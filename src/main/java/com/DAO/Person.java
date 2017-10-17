package com.DAO;  
  
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;  
  
//@XmlRootElement(name="Person")  
public class Person {  
    private String name;  
    private String sex;  
    private Job job;
    private List<Person> friends;
    public List<Person> getFriends() {
		return friends;
	}
	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public String getSex() {  
        return sex;  
    }  
    public void setSex(String sex) {  
        this.sex = sex;  
    }
	@Override
	public String toString() {
		return "Person [name=" + name + ", sex=" + sex + "]";
	}  
	public static Person fromString(String s) {
	       JSONObject object = JSONObject.fromObject(s);
	       return (Person) JSONObject.toBean(object, Person.class); 
	   }
      
}