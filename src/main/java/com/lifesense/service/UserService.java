package com.lifesense.service;

import com.lifesense.entity.User;


public interface UserService {
	
	public User getUser(Integer id);
	
	public void saveUser(User user);
}
