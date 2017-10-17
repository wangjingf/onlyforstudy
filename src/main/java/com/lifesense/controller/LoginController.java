package com.lifesense.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/auth")
public class LoginController {
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(@RequestParam("userName")String userName,@RequestParam("password")String password,HttpSession session){
		Map<String,Object> result = new HashMap<String,Object>();
		if(userName == null || password == null){
			result.put("isSuccess", false);
		}
		if(userName.equals("wjf") && password.equals("123")){
			result.put("isSuccess",true);
			session.setAttribute("userName", userName);
			
		}
		return JSONObject.toJSONString(result);
	}
}
