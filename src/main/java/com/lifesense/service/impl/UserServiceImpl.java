package com.lifesense.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lifesense.dao.UserDao;
import com.lifesense.entity.User;
import com.lifesense.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserDao userDao;
	
	/**
	 * 获取用户
	 * @param id 用户id
	 * @return
	 */
	public User getUser(Integer id){
		return userDao.get(User.class, id);	
	}
	
	/**
	 * 增加一个用户
	 * @param user 用户实体
	 */
	public void saveUser(User user){
		userDao.save(user);
	}

}
