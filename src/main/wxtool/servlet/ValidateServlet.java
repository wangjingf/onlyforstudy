package com.lifesense.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lifesense.tools.CheckOutUtils;
import com.lifesense.tools.WxMessage;
@Controller
public class ValidateServlet {
	@RequestMapping("/*")
	public void dump(Model model, HttpServletRequest request,HttpServletResponse response){
		System.out.println("==========begin dump===========");
		logger.info("requstURI is ::"+ request.getRequestURI());
	}
	static final Logger logger = LoggerFactory.getLogger(ValidateServlet.class);
	private boolean isValidRequest(HttpServletRequest request){
		 String signature = request.getParameter("signature");
         // 时间戳
         String timestamp = request.getParameter("timestamp");
         // 随机数
         String nonce = request.getParameter("nonce");
         return signature != null && CheckOutUtils.checkSignature(signature, timestamp, nonce);
	}
	/**
     * 微信消息接收和token验证
     * @param model
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/validate")
    public void validate(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        boolean isPost = request.getMethod().toLowerCase().equals("post");
        logger.info("receive message!");
        PrintWriter print;
        if(!isValidRequest(request)){
        	return;
        }
        if (isGet) {
            	logger.info("check complete");
                try {
                    print = response.getWriter();
                    print.write("");
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }else{
            	logger.info("check complete");
                try {
                	InputStream  in= request.getInputStream();
                	String content = IOUtils.toString(in);
                	logger.info("received content is :"+content);
                    print = response.getWriter();
                    String message = WxMessage.fromXml(content).createResponseMsg();
                    logger.info("the ret is {}",message);
                    print.write(message);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
