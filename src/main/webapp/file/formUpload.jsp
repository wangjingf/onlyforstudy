<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<form id="formAdd" method="post"  enctype="multipart/form-data" action="<%=request.getContextPath() %>/TomcatTest/UploadServlet">
		文件名称:<input type="text" name="savedFileName"><br/>
		<input type="file" name="doc">
		<button>提交</button>
	</form>
</body>
</html>