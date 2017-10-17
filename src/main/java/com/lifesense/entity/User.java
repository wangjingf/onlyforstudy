package com.lifesense.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
/**
 * 用户实体类
 * @author Administrator
 *
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)  
public class User  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//主键id
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	private Integer age;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	
}
