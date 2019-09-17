package com.lifesense.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

import com.alibaba.fastjson.JSONObject;

public class AccessTokenHelper {
	static AccessTokenHelper helper = new AccessTokenHelper();
	private AccessTokenHelper(){}
	public static AccessTokenHelper getInstance(){
		return helper;
	}
	String appId="wxcb798b0abf356c8b";
	String appSecret="a0cfcea6bb32fd23862bf55c6f3487c3";
	String accessToken;
	long expiresTime=0;
	public String getToken(){
		if(expiresTime!=0 && expiresTime>System.currentTimeMillis()){
			return accessToken;
		}else{
			return getAccessTokenByCredential();
		}
	}
	/**
	 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	 * @return
	 */
	private String getAccessTokenByCredential() {
		Response response = WebClient.create("https://api.weixin.qq.com/cgi-bin/token").accept(MediaType.APPLICATION_JSON)
		.query("grant_type", "client_credential")
		.query("appid", appId)
		.query("secret", appSecret)
		.get();
		InputStream input = (InputStream) response.getEntity();
		String result=null;
		try {
			result = IOUtils.toString(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Object> ret = (Map<String, Object>) JSONObject.parse(result);
		this.accessToken = (String) ret.get("access_token");
		this.expiresTime = System.currentTimeMillis()+(Integer)ret.get("expires_in");
		return accessToken;
	}
	static class TokenInfo {
		String accessToken;
		int expires_in=0;
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public int getExpires_in() {
			return expires_in;
		}
		public void setExpires_in(int expires_in) {
			this.expires_in = expires_in;
		}
		
	}
	public static void main(String[] args){
		String token = AccessTokenHelper.getInstance().getToken();
		System.out.println(token);
	}
}
