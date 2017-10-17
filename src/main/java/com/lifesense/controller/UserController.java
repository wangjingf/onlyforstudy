package com.lifesense.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lifesense.entity.User;
import com.lifesense.service.UserService;
/**
 * 用户控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	/**
	 * 获取用户
	 * @ResponseBody表示异步请求
	 * @return
	 */
	@RequestMapping(value="/get_user",method=RequestMethod.GET)
	@ResponseBody
	public User getUser(@RequestParam("id") Integer id){
		return  userService.getUser(id);
	}
	
	/**
	 * 添加一个用户，这里使用json格式传递参数
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/save_user",method=RequestMethod.POST)
	@ResponseBody
	public String saveUser(@RequestBody User user){
		userService.saveUser(user);
		return "success";
	}
			
			
	
	

}
