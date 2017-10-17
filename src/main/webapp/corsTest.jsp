<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/jquery-ui/jquery-ui.js"></script>
	<script>
		function testCors(){
			$.ajax({
					headers:{
						Authorization:"xxxxxxx"
					},
					url:'http://localhost:8010/cxf/index.jsp',
					success:function(ret){console.log(ret);},
					failure:function(ret){alert("fail!");}}
				);
		}
		
	</script>
	<button onclick="testCors()">开始测试</button>
</body>
</html>