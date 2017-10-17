<%-- <%	
	System.out.println("index.jsp is found");
	String contextPath = request.getContextPath();
	response.sendRedirect(contextPath+"/corsTest.jsp");
%> --%>
<%@page import="com.lifesense.entity.User"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<body>

	<h2>Hello32334343332</h2>
	<%
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
	%>
	cookieName is :<%=cookie.getName()%><br /> cookieValue is :<%=cookie.getValue()%><br />
	CookieVersion is :<%=cookie.getVersion()%><br /> cookieMaxAge is :<%=cookie.getMaxAge()%><br />
	cookiePath is :<%=cookie.getPath()%><br /> cookieDomain is :<%=cookie.getDomain()%><br />
	cookiePath is :<%=cookie.getPath()%><br />
	<%
		}
	%>
	<%User user = (User)session.getAttribute("user");
	   user.setName("wjffff");
	%>user.getName():<%=user.getName() %>
	<script type="text/javascript">
		function setCookie(name, value) {
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
			document.cookie = name + "=" + escape(value) + ";expires="
					+ exp.toGMTString();
		}
		function getCookie(name) {
			var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
			if (arr = document.cookie.match(reg))
				return unescape(arr[2]);
			else
				return null;
		}
		function delCookie(name) {
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval = getCookie(name);
			if (cval != null)
				document.cookie = name + "=" + cval + ";expires="
						+ exp.toGMTString();
		}
		function setCookie(name, value, time) {
			var strsec = getsec(time);
			var exp = new Date();
			exp.setTime(exp.getTime() + strsec * 1);
			document.cookie = name + "=" + escape(value) + ";expires="
					+ exp.toGMTString();
		}
		function getsec(str) {
			alert(str);
			var str1 = str.substring(1, str.length) * 1;
			var str2 = str.substring(0, 1);
			if (str2 == "s") {
				return str1 * 1000;
			} else if (str2 == "h") {
				return str1 * 60 * 60 * 1000;
			} else if (str2 == "d") {
				return str1 * 24 * 60 * 60 * 1000;
			}
		}
		window.onload = function(){
				console.log(document.cookie);
			
		}
	</script>
</body>
</html>
