<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
</head>
<% String context = request.getContextPath(); 
    String url = context+"/login";
%>
<body class="error_logo">
	<div class="error_logo">
		<img src="images/logo.png" width="336" height="23">
	</div>
	<div class="project_name">CM-MTLD Matching Application</div>
	<hr>
	<c:if test="${not empty errCode}">
		<font color="red" size="3">${errCode} : System Errors, Please
			contact TFA System Administrator
        click on <a href=<%=url%>><span>Login</span></a> to re-Login to application.</font>
	</c:if>

	<c:if test="${empty errCode}">
		<font color="red" size="3">System Errors, Please contact TFA
			System Administrator
        click on <a href=<%=url%>><span>Login</span></a> to re-Login to application.</font>
	</c:if>

	<%-- <c:if test="${not empty errMsg}">
		<h4>${errMsg}</h4>
	</c:if> --%>

</body>
</html>