<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传实例 - 菜鸟教程</title>
</head>
<body>
<h1>文件上传实例 - 菜鸟教程</h1>
<form method="post" action="<%=request.getContextPath() %>/TomcatTest/UploadServlet" enctype="multipart/form-data">
	选择一个文件:
	<input type="file" name="uploadFile" />文件名称is：<input type="text" name="fileName"/>
	<br/><br/>
	<input type="submit" value="上传" />
	<button type="button" onclick="">删除</button>
</form>
</body>
</html>