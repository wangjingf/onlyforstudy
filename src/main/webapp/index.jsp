<%-- <%	
	System.out.println("index.jsp is found");
	String contextPath = request.getContextPath();
	response.sendRedirect(contextPath+"/corsTest.jsp");
%> --%>
<%@page import="com.lifesense.entity.User"%>
<%@page import="org.springframework.session.SessionRepository"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<body>

<h2>Hello32334343332</h2>
<%	

	// add a cookie
	session.setAttribute("userName", "wjf");
session.setAttribute("userName1", "wjf");
	Cookie useCookie = new Cookie("userName","wjf");
	useCookie.setComment("save the userName");
	response.addCookie(useCookie);
	Cookie[] cookies =  request.getCookies();
	for(Cookie cookie : cookies){
	
%>
cookieName is :<%=cookie.getName() %><br/>
cookieValue is :<%=cookie.getValue() %><br/>
CookieVersion is :<%=cookie.getVersion() %><br/>
cookieMaxAge is :<%=cookie.getMaxAge() %><br/>
cookiePath is :<%=cookie.getPath() %><br/>
cookieDomain is :<%=cookie.getDomain() %><br/>
cookiePath is :<%=cookie.getPath() %><br/>
<%
	}
%>
设置前:<%

%>
<%User user = (User)session.getAttribute("user");
String name = "";
if(user!=null){
	name = user.getName();
}
	%>user.getName():<%=name %>
<%
String userName = (String)session.getAttribute("userName");
session.setAttribute("user", new User());
%>userName:<%=userName %><!-- session目前使用redisRepository实现的，因此session.getAttribute直接访问的就是redis，若redis挂掉的话session将不能够使用了 -->
<br/>设置后
<% user = (User)session.getAttribute("user"); %>user.getName():<%=user.getName() %>
</body>
</html>
