
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.sun.xml.bind.v2.schemagen.xmlschema.Import"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:useBean id="dateValue" class="java.util.Date" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<!-- <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1"> -->

    <title></title>
    <script>var ctx = "${pageContext.request.contextPath}";</script>
    <script language="javascript" type="text/javascript">
        var submit = 0;
        var log = document.getElementById("logout");
        function CheckIsRepeat() { 
                 window.location = ctx+"/logout";
                $('#disablelogout').attr("onmousedown","");
        }
    
</script>
</head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
<title>CM-MTLD Matching Application</title>
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
<script src="js/jquery.min.js" type="text/javascript"></script>
<body>
	<div class="header">
		<div class="container">
			<div class="header_left">
				<a href="login"><img src="images/logo.png"
					alt="Teach For America"></a>
			</div>
			<div class="header_right">
				<a id="disablelogout" class="icon_btn logout" onmousedown="CheckIsRepeat();">
				  <img  src="images/logout.png" width="17" height="18" title="Logout">
				</a>
				<div class="user_name">
					<c:out value="${sessionScope.LOGGEDIN_USER_REGION.firstName}" />
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div class="container">
		<div id="banner"></div>
		<div class="menu">
			<ul>
				<li><a href="selectCriteria" class="selected"
					id="criteria_screen"><img src="images/criteria_select_icon.png"
						width="22" height="22"> <span>Criteria Select</span></a></li>
				<li><a href="#" id="matching_screen"><img
						src="images/cm_cohorts_icon.png" width="22" height="22"> <span>CM
							Cohorts</span></a></li>
				<li class="last"><a href="show" id="mtld_info_screen"><img
						src="images/mtld_info_icon.png" width="22" height="22"> <span>Upload
							Info</span></a></li>
			</ul>

		</div>
		<div class="region_name">
			Region Name: <a href="#"><c:out
					value="${sessionScope.LOGGEDIN_USER_REGION.region.regionCode}" /></a>
		</div>
	</div>
	<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
</body>
</html>