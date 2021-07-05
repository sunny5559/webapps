<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="application/json; charset=utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
<title>CM-MTLD Matching Application</title>

<script language="javascript" type="text/javascript">
function removeSpaces(string) {
 return string.split(' ').join('');
}
</script>

</head>
<body>
	<form:form method="POST" action="userLogin" commandName="UserBean"
		autocomplete="off">
		<div class="login_container">
			<div class="login_logo">
				<img src="images/logo.png" width="336" height="23">
			</div>
			<div class="project_name">CM-MTLD Matching Application</div>
			<div class="login_box">
				<div class="input_row">
					<form:input path="loginId" tabindex="1" class="input_box"
						name="loginId" id="loginId" maxlength="20"
						placeholder="TeachForAmerica ID" onkeypress="this.value=removeSpaces(this.value);"
						/>
					<br>
					<tr>
						<td></td>
						<td><font color="red"><form:errors path="loginId"></form:errors></td>
						</font>
					</tr>
				</div>

				<div class="input_row">
					<form:password path="password" tabindex="2" class="input_box"
						name="password" id="password" maxlength="20"
						placeholder="Password"
						 />
					<br>
					<tr>
						<td></td>
						<td><font color="red"><form:errors path="password"></form:errors></td>
						</font>
					</tr>
				</div>

				<!-- <div class="input_row">
					<div class="login_check_box">
						<input name="" type="checkbox" value="">
					</div>
					<div class="remember_text">Remember password</div>
					<div class="clear"></div>

				</div> -->

				<div class="input_row">
					<input name="" tabindex="3" height="true" type="submit"
						value="Login" class="green_btn">
				</div>
				<!-- 
				<div class="input_row">
					<a href="newForgotPassword"> Forgot Password? </a>
				</div> -->



				<div class="clear"></div>
			</div>

			<div class="clear"></div>
		</div>
	</form:form>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"
		type="text/javascript"></script>
</body>
</html>