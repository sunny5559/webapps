<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<html>
  <head>
    <title>Access Denied</title>
  </head>

  <body>
<% String context = request.getContextPath(); 
    String url = context+"/login";
%>
	<h2>Access Denied</h2>
	<p>We're sorry, but you are not authorized to perform the requested operation.
	click on <a  href=<%=url%>><span>Login</span></a></p>

  </body>
</html>
