<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<head>
 <meta http-equiv="Pragma" content="no-cache">
 <meta http-equiv="Cache-Control" content="no-cache">
 <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
  <link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
  
   <script type="text/javascript">
	$('#criteria_screen').removeClass('selected');
	$('#mtld_info_screen').addClass('selected');
	$('#matching_screen').removeClass('selected');
 
 </script>
  
</head>
<style>
<% String context = request.getContextPath(); 
    String url = context+"/login";
%>

body { font-family: "Trebuchet MS"; }
h1 { font-size: 1.5em; }
</style>
</head>
<body>
<div class="container">
	Data uploaded successfully,click <a href=<%=url%>><span>here</span></a> to go back to application.
	</div>
<!-- &nbsp;&nbsp;<input type="button" onClick="location.href='selectCriteria'" value="Home" class="green_btn" /> -->
</body>
</html>
