<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.FileInputStream"%>
<%@page language="java" contentType="application/octet-stream" pageEncoding="utf-8"%>

<% 
response.reset();
	//response.setContentType("application/x-download");
	
	String filedownload = "G:/workbase/workspace/cxf3.1/cxf3.1/src/main/webapp/staticFile/xx.xlsx";
	String filedisplay = "xx.xls";
	 filedisplay = java.net.URLEncoder.encode(filedisplay, "UTF-8");
	response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
	java.io.OutputStream outp = null;
	java.io.FileInputStream in = null;
	try {
		outp = response.getOutputStream();
		in = new FileInputStream(filedownload);
		byte[] b = new byte[1024];
		int i = 0;
		while ((i = in.read(b)) > 0) {
			outp.write(b, 0, i);
		}
		outp.flush();
		//out.clear();
	//	out = pageContext.pushBody();
	} catch (Exception e) {
		System.out.println("Error!");
		e.printStackTrace();
	} finally {
		if (in != null) {
			in.close();
			in = null;
		}
	}
%>
