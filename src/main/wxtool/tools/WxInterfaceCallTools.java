package com.lifesense.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

import com.alibaba.fastjson.JSONObject;

public class WxInterfaceCallTools {
	static final String MENU_CREATE_INTERFACE="https://api.weixin.qq.com/cgi-bin/menu/create";
	public static String createMenu(String body) throws IOException{
		Response resp = WebClient.create(MENU_CREATE_INTERFACE).query("access_token", AccessTokenHelper.getInstance().getToken())
		.post(body);
		InputStream in = (InputStream) resp.getEntity();
		return IOUtils.toString(in);
	}
	public static void main(String[] args) throws IOException{
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Object> buttons = new ArrayList<Object>();
		map.put("type", "click");
		map.put("name", "我要签到");
		map.put("key", "signIn");
		buttons.add(map);
		Map<String,Object> body = new LinkedHashMap<String,Object>();
		body.put("button", buttons);
		String result = createMenu(JSONObject.toJSONString(body));
		System.out.println(result);
	}

}
