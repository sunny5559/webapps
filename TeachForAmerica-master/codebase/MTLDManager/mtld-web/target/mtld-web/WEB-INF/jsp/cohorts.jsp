<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="org.tfa.mtld.service.constants.TFAConstants" %>
    <%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
    <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">

<title><spring:message code="label.title"/></title>
<script>var ctx = "${pageContext.request.contextPath}";</script>
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
<script src="js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.js" type="text/javascript"></script>
<script src="js/jquery.blockUI.js" type="text/javascript"></script>

<script src="js/cohorts.js" type="text/javascript"></script>

<!--  google map  -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDY0kkJiTPVd2U7aTOAwhc9ySH6oHxOIYM&sensor=false"></script>
<script>




var NO_RECORD_FOUND="No Record Found";

$.ajaxSetup({ cache: false });

$(document).ajaxError(function( event, jqxhr, settings, exception ) {	
	if (jqxhr.status == '601') {
		window.location = ctx+"/login";
	}
});

</script>




<!--    slim scroll bar    -->
<link href="stylesheet/jquery.mCustomScrollbar.css" rel="stylesheet" />
<!--  Custome Radio BTN ----------- -->

<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
    

<script src="js/jquery.screwdefaultbuttonsV2.js"></script>

</head>
<body class="loadingImage">
<div class="container">
	<c:choose>
		<c:when test="${errorInCohortFormation!=null}">
			<div class="Error"><label class="errorLabel">${errorInCohortFormation}</label></div>	
		</c:when> 
		<c:otherwise>
			<div class="Error"><label class="errorLabel"></label></div>
		</c:otherwise>
	</c:choose>


  	<div id="progress_div">
    <div id="progress_heading"><strong><spring:message code="label.progressBarLabel"/></strong> <a href="javascript:void();" class="progress_popup_icon"><img src="images/popup_icon.png" width="20" height="18"></a></div>
        <div id="progress_criteria" class="scrollbar_box">
        
        <table width="100%" border="0" cellspacing="2" cellpadding="0" class="progress_bar_table">
			<c:forEach var="entry" items="${cohortsdata}">
				 <c:if test="${entry.key =='Criteria Map'}">
				  <c:forEach var="list" items="${entry.value}">
					  <c:forEach var="entry1" items="${list}">
					  <c:if test="${entry1.key =='Overall Progress'}">
						  <tr>   
						    <td width="41%" class="overall_progress_row" align="left" alt="${entry1.key}" valign="middle"><strong class="overall_progress_row_text">${entry1.key}</strong></td>
						    <td width="59%" align="left" class="overall_progress_row_value" valign="middle"><div class="progress" style="width:${entry1.value}%" >${entry1.value}%</div></td>
						  </tr>
					  </c:if>
					  </c:forEach>
					  <c:forEach var="entry2" items="${list}" varStatus="loop">
					  <c:if test="${entry2.key !='Overall Progress'}">
						  <tr>
						    <td width="41%" class="criteria_progress_row" id="criteria_progress_row_${loop.index}" align="left" alt="${entry2.key}" valign="middle"><label id="criteria_progress_row_text_${loop.index}">${fn:substring(entry2.key,0,24)}...</label></td>
						    <td width="59%" align="left" valign="middle"><div class="progress" style="width:${entry2.value}%" >${entry2.value}%</div></td>
						  </tr>
					  </c:if>
					  </c:forEach>
				  </c:forEach>
				 </c:if>
			</c:forEach>  
  
        </table>

        
        
        
        </div>
    </div> 
    <div id="googleMap" class="region_map"></div>
    
    <div class="clear"></div>
    
    
    <div id="cohorts_container">
    
    <div id="cohorts_div">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="heading">
  <tr>
    <td width="65%" align="left" valign="middle"><strong><spring:message code="label.corpMembersLabel"/></strong></td>
    <td width="35%" align="right" valign="middle">
    <select name="select" id="cmmtldpicklist">
 <!-- Starting: CM and MTLD count for drop down List -->
		<c:set var="hired" value="<%=TFAConstants.HIRED_CORPS_MEMBER%>"/>
		<c:set var="unhired" value="<%=TFAConstants.UNHIRED_CORPS_MEMBER%>"/>
		<c:set var="mtld" value="<%=TFAConstants.MTLD_LIST%>"/>
	    <c:forEach var="entry" items="${cohortsdata}">
			<c:if test="${entry.key == mtld}">
	   			<option value="mtld"><c:out value="${entry.value.size()}" /> MTLDs</option>
	   		</c:if>
	   		<c:if test = "${entry.key == hired}">
	   			<option value="cm" selected="selected"><c:out value="${entry.value.size()}" /> CMs</option>
	   		</c:if>
			<c:if test="${entry.key == unhired}">
	   			<option value="unhired"><c:out value="${entry.value.size()}" /> Unhired</option>
	   		</c:if>
	   	</c:forEach>
<!-- Ending: CM and MTLD count for drop down List -->
      </select>
      </td>
  </tr>
</table>
	<div id="filterDiv" style="display:none;"><select name="" id="filter" style="width:100%;">
    <option>Filter By</option>
	<option><spring:message code="label.filter1YearCM"/></option>
	<option><spring:message code="label.filter2YearCM"/></option>
    </select></div>
    
    
<!--Starting: Side Panel for CM and MTLD List -->
 <div id="cmlist">   
    <ul class="cohorts_list cohorts_list1 scrollbar_box cmlist">
    <c:set var="hired" value="<%=TFAConstants.HIRED_CORPS_MEMBER%>"/>
	<c:set var="unhired" value="<%=TFAConstants.UNHIRED_CORPS_MEMBER%>"/>
	<c:set var="mtld" value="<%=TFAConstants.MTLD_LIST%>"/>
   	<c:forEach var="entry" items="${cohortsdata}">
   		<c:if test="${entry.key == hired}">
   			<c:forEach var="list" items="${entry.value}" varStatus="loop">
   			<c:choose>
   				<c:when test="${list.cmYear!=null}">
   					<c:set var="cmYear" value="${list.cmYear}"/>
   					<c:if test="${list.gradeLevel!=null || list.subjectGroup!=null}">
   						<c:set var="cmYear" value="${list.cmYear},"/>
   					</c:if>
   				</c:when>
   				<c:otherwise>
   					<c:set var="cmYear" value=""/>
   				</c:otherwise>
   			</c:choose>
   			<c:choose>
   				<c:when test="${list.gradeLevel!=null}">
   					<c:set var="gradeLevel" value="${list.gradeLevel} School"/>
   					<c:if test="${list.subjectGroup!=null}">
   						<c:set var="gradeLevel" value="${list.gradeLevel} School,"/>
   					</c:if>
   				</c:when>
   				<c:otherwise>
   					<c:set var="gradeLevel" value=""/>
   				</c:otherwise>
   			</c:choose>

   			
   				<li class="cohorts_item" id="li_cm_${list.id}">
   				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="table_cm_${list.id}">
					  <tr>
					    <td><a href="javascript:void(0);" class="name open_corp setHelpText" name="Corp Member"  id="cm_${list.id}">${list.firstName} ${list.lastName}</a> ${cmYear} ${gradeLevel} ${list.subjectGroup} 
					    	<input type="hidden" id="cm_first_name_char_${list.id}"  value="${fn:substring(list.firstName, 0, 1)}" />
					    	<input type="hidden" id="cm_last_name_char_${list.id}"  value="${fn:substring(list.lastName, 0, 1)}" />
					    	<input type="hidden" id="cm_name_${list.id}" value="${list.firstName} ${list.lastName}" /> 
					    	<input type="hidden" id="cm_corps_year_${list.id}" value="${list.cmYear}" />
					     	<input type="hidden" id="cm_grade_level_${list.id}" value="${list.gradeLevel}" />
					      	<input type="hidden" id="cm_primary_subject_${list.id}" value="${list.subjectGroup}" />
					       	<input type="hidden" id="cm_subject_modifier_${list.id}" value="${list.subjectModifier}" />
					        <input type="hidden" id="cm_school_name_${list.id}" value="${list.schoolBean.schoolName}" />
					        <input type="hidden" id="cm_school_neighborhood_${list.id}" value="${list.schoolBean.neighborhood}" />
					    
					    </td>
					    <td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" id="move_cm_${list.id}" class="icon_btn copy_cohorts"><img src="images/icon_drag.png" width="20" height="17"></a></td>
					    <td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_cohorts" id="delete_cm_${list.id}_${list.isHired}"><img src="images/icon_delete.png" width="18" height="18"></a></td>
					  </tr>
				</table>
				</li>
   				
   			</c:forEach>
   		</c:if>
   	</c:forEach> 
    </ul>
  </div>
  <div id="mtldlist" style="display:none;"> 
    <ul class="cohorts_list cohorts_list1 scrollbar_box mtldlist">
   	<c:forEach var="entry" items="${cohortsdata}">
   		<c:if test="${entry.key ==mtld}">
   		<c:forEach var="list" items="${entry.value}" varStatus="loop">
   				<li class="cohorts_item" id="li_seededMember_MTLD_${list.id}">
   				
   				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="table_mtld_${list.id}">
					  <tr>
					  	<td><a href="javascript:void(0);" class="name open_mtld setHelpText" name="MTLD" id="mtld_${list.id}">${list.firstName} ${list.lastName} </a>
				<c:if test="${not empty list.tenureRole}">
					<c:choose>
	   					<c:when test="${list.tenureRole>1}">
	   						 ${list.tenureRole} Years MTLD
	   					</c:when>
	   					<c:otherwise>
	   						 ${list.tenureRole} Year MTLD
	   					</c:otherwise>
	   				</c:choose> 
				</c:if>	    	
					    	<input type="hidden" id="mtld_first_name_char_${list.id}"  value="${fn:substring(list.firstName, 0, 1)}" />
					    	<input type="hidden" id="mtld_last_name_char_${list.id}"  value="${fn:substring(list.lastName, 0, 1)}" />
					    	
					    	<input type="hidden" id="mtld_name_${list.id}"  value="${list.firstName} ${list.lastName}" />
					    	<input type="hidden" id="mtld_tenureRole_${list.id}"  value="${list.tenureRole}" />
					      	<input type="hidden" id="mtld_alum_${list.id}"  value="${list.alum}" />
					        <input type="hidden" id="mtld_subject_taught_${list.id}"  value="${list.subjectTaught}" />
					        <input type="hidden" id="mtld_cmo_affiliation_${list.id}"  value="${list.cmoAffiliation}" />
					        <input type="hidden" id="mtld_corps_region_${list.id}"  value="${list.corpsRegionName}" />
					 		<input type="hidden" id="mtld_corps_district_${list.id}"  value="${list.corpsSchoolDistrict}" />
			 		        <input type="hidden" id="mtld_person_color_${list.id}"  value="${list.personColor}" />
					        <input type="hidden" id="mtld_low_income_background_${list.id}"  value="${list.lowIncomeBackground}" />
					        <input type="hidden" id="mtld_neighborhood_${list.id}"  value="${list.neighborhood}" />
					    </td>
					    <td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" class="icon_btn copy_seeded" id="move_MTLD_${list.id}"><img src="images/icon_drag.png" width="20" height="17"></a></td>
					    <td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_seeded" id="delete_seededMember_MTLD_${list.id}"><img src="images/icon_delete.png" width="18" height="18"></a></td>
					  </tr>
				</table>
				</li>
		</c:forEach> 
   		</c:if>
   	</c:forEach> 
    </ul>
  </div>
     <div id="unhiredlist" style="display:none;">   
    <ul class="cohorts_list cohorts_list1 scrollbar_box cmlist">
   	<c:forEach var="entry" items="${cohortsdata}">
   		<c:if test="${entry.key == unhired}">
   			<c:forEach var="list" items="${entry.value}" varStatus="loop">
   				<c:choose>
   				<c:when test="${list.cmYear!=null}">
   					<c:set var="cmYear" value="${list.cmYear}"/>
   					<c:if test="${list.gradeLevel!=null || list.subjectGroup!=null}">
   						<c:set var="cmYear" value="${list.cmYear},"/>
   					</c:if>
   				</c:when>
   				<c:otherwise>
   					<c:set var="cmYear" value=""/>
   				</c:otherwise>
   				</c:choose>
	   			<c:choose>
	   				<c:when test="${list.gradeLevel!=null}">
	   					<c:set var="gradeLevel" value="${list.gradeLevel} School"/>
	   					<c:if test="${list.subjectGroup!=null}">
   							<c:set var="gradeLevel" value="${list.gradeLevel} School,"/>
   						</c:if>
	   				</c:when>
	   				<c:otherwise>
	   					<c:set var="gradeLevel" value=""/>
	   				</c:otherwise>
	   			</c:choose>
   				<li class="cohorts_item" id="li_cm_${list.id}">
   				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="table_cm_${list.id}">
					  <tr>
					    <td><a href="javascript:void(0);" class="name open_corp setHelpText" name="Corp Member" id="cm_${list.id}">${list.firstName} ${list.lastName}</a> ${cmYear} ${gradeLevel} ${list.subjectGroup}  
					     	<input type="hidden" id="cm_first_name_char_${list.id}"  value="${fn:substring(list.firstName, 0, 1)}" />
					    	<input type="hidden" id="cm_last_name_char_${list.id}"  value="${fn:substring(list.lastName, 0, 1)}" />
					     	<input type="hidden" id="cm_name_${list.id}" value="${list.firstName} ${list.lastName}" /> 
					    	<input type="hidden" id="cm_corps_year_${list.id}" value="${list.cmYear}" />
					     	<input type="hidden" id="cm_grade_level_${list.id}" value="${list.gradeLevel}" />
					      	<input type="hidden" id="cm_primary_subject_${list.id}" value="${list.subjectGroup}" />
					       	<input type="hidden" id="cm_subject_modifier_${list.id}" value="${list.subjectModifier}" />
					        <input type="hidden" id="cm_school_name_${list.id}" value="${list.schoolBean.schoolName}" />
					        <input type="hidden" id="cm_school_neighborhood_${list.id}" value="${list.schoolBean.neighborhood}" />
					   </td>
					     <td>
						    <input type="hidden" id="cmyear_${list.cmYear}" class="cmyear Corpsyear" value="${list.cmYear}"/>
					    </td> 
					    
					    <td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" id="move_cm_${list.id}" class="icon_btn copy_cohorts"><img src="images/icon_drag.png" width="20" height="17"></a></td>
					    <td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_cohorts" id="delete_cm_${list.id}_${list.isHired}"><img src="images/icon_delete.png" width="18" height="18"></a></td>
					  </tr>
				</table>
				</li>
   			</c:forEach>
   		</c:if>
   	</c:forEach> 

    </ul>
  </div>
<!--Ending: Side Panel for CM and MTLD List -->

  <div class="clear"></div>
</div>  
  	
  <div id="group_container">
  	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="heading">
  <tr>
    <td width="60%" align="left" valign="middle"><strong><spring:message code="label.cohorts"/></strong></td>
    <td width="24%" align="right" valign="middle">
<!-- Starting:Total CM Count -->
	<c:set var="corpsMemberCount" value="<%=TFAConstants.CORPS_MEMBERS_COUNT%>"/>
    <c:forEach var="entry" items="${cohortsdata}">
    		<c:if test="${entry.key ==corpsMemberCount}">
	   			 	<span id="groupCMCount" >${entry.value[0]}</span> CMs
	   		</c:if>
	   	</c:forEach>
	   	
<!-- Ending:Total CM Count -->
     </td>
     <!-- <td width="6%" align="right" valign="middle"><a href="javascript:void(0);" class="icon_btn"><img src="images/ad_group.png" alt="Create New Group" title="Create New Group" width="32" height="17"></a></td> -->
    <td width="3%" align="right" valign="middle"> <c:if test="${not empty cohortCSVID}"> <a href="export/<c:out value="${cohortCSVID}" />"> </c:if>  <img src="images/excel.png" alt="Export Excel" title="Export Excel" width="13" height="15"></a></td>
    <td width="3%" align="right" valign="middle"><a href="selectCriteria" class="icon_btn"><img src="images/back_btn.png" alt="Rematch" title="Rematch" width="17" height="17"></a></td>
  </tr>
</table>

 	<div class="clear"></div>	

<!-- Starting MTLD Detail List -->
<c:set var="cohortList" value="<%=TFAConstants.COHORT_LIST%>"/>
<c:set var="count" value="1"/>
<c:forEach var="entry" items="${cohortsdata}">
	<c:if test="${entry.key == cohortList}">
		<c:forEach var="list" items="${entry.value}" varStatus="loop">
		<c:if test="${count == '1'}">	
				<c:choose>
					<c:when test="${list.seededMtldBean !=null}">
						<c:set var="id" value="${list.seededMtldBean.id}" />
						<c:set var="name" value="${list.seededMtldBean.firstName} ${list.seededMtldBean.lastName}" /> 
						<c:set  var="seededMember" value="MTLD" ></c:set>
						<c:set var="mtld_name" value="${list.seededMtldBean.firstName} ${list.seededMtldBean.lastName}" ></c:set>
						<c:set var="fNameCharacter" value="${fn:substring(list.seededMtldBean.firstName, 0, 1)}" />
						<c:set var="lNameCharacter" value="${fn:substring(list.seededMtldBean.lastName, 0, 1)}" /> 
						<c:if test="${not empty list.seededMtldBean.tenureRole}">
							<c:choose>
		   					<c:when test="${list.seededMtldBean.tenureRole>1}">
								<c:set var="tenure_role" value="${list.seededMtldBean.tenureRole} Years MTLD" />
							</c:when>	   					
		   					<c:otherwise>
		   						<c:set var="tenure_role" value="${list.seededMtldBean.tenureRole} Year MTLD" />	   					
		   					</c:otherwise>
		   					</c:choose> 
						</c:if>
					</c:when>
					<c:otherwise>
						<c:set var="id" value="${list.seededCMBean.id}" /> 
						<c:set var="name" value="${list.seededCMBean.firstName} ${list.seededCMBean.lastName}" />
						<c:set var="fNameCharacter" value="${fn:substring(list.seededCMBean.firstName, 0, 1)}" />
						<c:set var="lNameCharacter" value="${fn:substring(list.seededCMBean.lastName, 0, 1)}" />  
						<c:set var="isSeededMemberHired" value="${list.seededCMBean.isHired}"/>
						<c:set  var="seededMember" value="CM" ></c:set>
						<c:set var="tenure_role" value="${list.seededCMBean.cmYear}" />
					</c:otherwise>
				</c:choose>
				<c:set var="cohortId" value="${list.id}" />  
				<c:set var="cmId" value="${list.seededCMBean.id}" />  
				<c:set var="mtldId" value="${list.seededMtldBean.id}" /> 
      		<div id="group_detail">
			<div id="group_detail_heading" class="heading_${id}_${cohortId}">
				<c:choose>
					<c:when test="${list.isFinalCohort==true}">
						<input type="hidden" class="isFinalCohort_${list.id}" value="true"/>
					</c:when>
					<c:otherwise>
						<input type="hidden" class="isFinalCohort_${list.id}" value="false"/>
					</c:otherwise>
				</c:choose>
				<input type="hidden" value="${list.id}" id="defaultCohortId"/>
      		  <table width="100%" border="0" cellspacing="0" cellpadding="0" id="cohorts_detail_${id}_${cohortId}">
				<tr>
			          <%-- <td width="25%" rowspan="2" align="left" valign="middle"><div class="user_img">${fn:toUpperCase(fNameCharacter)}${fn:toUpperCase(lNameCharacter)}</div></td> --%>
			          <td width="75%" align="left" valign="bottom">
			          <a href="javascript:void(0);"><spring:message code="label.groupInfoLabel"/></a>
			            <c:choose>
			               <c:when test="${seededMember=='MTLD'}">
			          		<%-- <a href="javascript:void(0);" class="open_mtld setHelpText" name="MTLD id="mtld_${id}_${cohortId}"><c:out value="${name}"/></a> --%>
			                	
					       </c:when>
			           <c:otherwise>
			            <%-- <a href="javascript:void(0);" class="open_corp setHelpText" name="Corp Member" id="cm_${id}"><c:out value="${name}"/></a> --%>
			            
			           
			           </c:otherwise>
			           </c:choose>     
			         
			          </td>
			          <td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id="${cohortId}" class="group_popup_icon setHelpText" name="Cohort/Group" ><img src="images/popup_icon.png" width="20" height="18"></a></td>
					  <c:set var="count" value="2"/>
			   </tr>
			   	<tr>
		           <td colspan="2" align="left" valign="top"> <%-- ${tenure_role} <br> --%> 
		           	<c:choose>
			          	<c:when test="${list.schoolRep=='1'}">
			          		<strong class="schools_represented_${cohortId}">${list.schoolRep}</strong> School Represented <br>
			          	</c:when>
			          	<c:otherwise>
			          		<strong class="schools_represented_${cohortId}">${list.schoolRep}</strong> Schools Represented <br>
			          	</c:otherwise>
			          </c:choose>
		          <c:choose>
		         <c:when test="${not empty list.maxDistance && $list.overDistance==true}">   
		           <strong>${list.maxDistance}</strong> Miles apart (max)
				    	<img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" > 
				    </c:when>
				    
				    <c:otherwise>
				     <c:if test="${not empty list.maxDistance}">
				      <strong>${list.maxDistance}</strong> Miles apart (max)
				     </c:if>
				     
				    </c:otherwise>
				     </c:choose><br> 
		           <table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">
						 <tr>
						    <td width="73%" align="left" valign="middle"><span title="Basics Score"><%=TFAConstants.CRITERIA_CATEGORY_BASICS%></span></td>
						    <td width="27%" align="left" valign="middle"><span title="Content Score">${list.basicsCriteriaPer}%</span></td>
						  </tr>
						  <tr>
						    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_CONTENT%></td>
						    <td align="left" valign="middle">${list.contentCriteriaPer}%</td>
						  </tr>
						  <tr>
						    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY%></td>
						    <td align="left" valign="middle">${list.geographicCriteriaPer}%</td>
						  </tr>
						  <tr>
						    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS%></td>
						    <td align="left" valign="middle">${list.relationshipsCriteriaPer}%</td>
						  </tr> 

				  </table>
		          </td>
        		</tr>
			 	<tr></tr>
			  </table>
			  </div>
				<div class="group_detail_counting">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				  	<c:choose>
					  	<c:when test="${list.cohortDetailBean.size()==null}">
					  		<c:set var="corpsMemberCountText" value="0 Corps Members"/>
					  	</c:when>
					  	<c:when test="${list.cohortDetailBean.size()==1}">
					  		<c:set var="corpsMemberCountText" value="1 Corp Member"/>
					  	</c:when>
					  	<c:otherwise>
					  		<c:set var="corpsMemberCountText" value="${list.cohortDetailBean.size()} Corps Members"/>
					  	</c:otherwise>
				  	</c:choose>
				    <td width="90%"><%-- "${entry.value.CohortBean.corps.length}" --%><span class="cmcount_${list.id}">${corpsMemberCountText}</span>
				    <%--  <c:if test="${list.overDistance==true}">
				    	<img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" > 
				     </c:if> --%>
				    </td>
				    <td width="10%">&nbsp;</td>
				  </tr>
				</table>
			    </div>
    			<ul class="cohorts_list scrollbar_box">
    			<!--Start: Added for seeded member (CM/MTLD) -->
    			<c:if test="${seededMember=='MTLD'}">
    				<li class="cohorts_item" id="li_seededMember_${seededMember}_${id}">
    				<table width="100%" border="0" cellspacing="0" cellpadding="0">
    					<tr>
    						<td><a href="javascript:void(0);" name="MTLD" class="open_mtld setHelpText open_status open_mtld" id="${seededMember}_${id}">${name}</a> ${tenure_role}
    						
    						<!-- hidden fields for Popup.Start -->
    				<input type="hidden"  id="mtld_name_${id}" value="${list.seededMtldBean.firstName} ${list.seededMtldBean.lastName}" />
						  <input type="hidden" id="mtld_first_name_char_${id}" value="${fNameCharacter}" />
						  <input type="hidden" id="mtld_last_name_char_${id}" value="${lNameCharacter}" /> 
						 <input type="hidden" id="mtld_tenureRole_${id}" value="${list.seededMtldBean.tenureRole}" />
					 <input type="hidden" id="mtld_alum_${id}" value="${list.seededMtldBean.alum}" />
						 <input type="hidden" id="mtld_subject_taught_${id}" value="${list.seededMtldBean.subjectTaught}" />
						 <input type="hidden" id="mtld_corps_region_${id}" value="${list.seededMtldBean.corpsRegionName}" >
						 <input type="hidden" id="mtld_corps_district_${id}"  value="${list.seededMtldBean.corpsSchoolDistrict}" />
						 <input type="hidden" id="mtld_person_color_${id}" value="${list.seededMtldBean.personColor}" />
						 <input type="hidden" id="mtld_low_income_background_${id}" value="${list.seededMtldBean.lowIncomeBackground}" >
						 <input type="hidden" id="mtld_neighborhood_${id}" value="${list.seededMtldBean.neighborhood}" /> 
						 <input type="hidden" id="mtld_cmo_affiliation_${id}" value="${list.seededMtldBean.cmoAffiliation}" />
					
					
    						<!-- End -->
    						
    						</td>
    						<td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" class="icon_btn copy_seeded" id="move_${seededMember}_${id}"><img src="images/icon_drag.png" width="20" height="17"></a></td>
    						<td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_seeded" id="delete_seededMember_${seededMember}_${id}"><img src="images/icon_delete.png" width="18" height="18"></a></td>
					  </tr>
					</table>
					</li>
				</c:if>
    			<!--End: Added for seeded member (CM/MTLD) -->
					<c:forEach var="list1"  items="${list.cohortDetailBean}" varStatus="loop1">
						<c:choose>
			   				<c:when test="${list1.corpsMember.cmYear!=null}">
			   					<c:set var="cmYear" value="${list1.corpsMember.cmYear}"/>
			   					<c:if test="${list1.corpsMember.gradeLevel!=null || list1.corpsMember.subjectGroup!=null}">
   									<c:set var="cmYear" value="${list1.corpsMember.cmYear},"/>
   								</c:if>
			   				</c:when>
			   				<c:otherwise>
			   					<c:set var="cmYear" value=""/>
			   				</c:otherwise>
			   			</c:choose>
			   			<c:choose>
			   				<c:when test="${list1.corpsMember.gradeLevel!=null}">
			   					<c:set var="gradeLevel" value="${list1.corpsMember.gradeLevel} School"/>
			   					<c:if test="${list1.corpsMember.subjectGroup!=null}">
   									<c:set var="gradeLevel" value="${list1.corpsMember.gradeLevel} School,"/>
   								</c:if>
			   				</c:when>
			   				<c:otherwise>
			   					<c:set var="gradeLevel" value=""/>
			   				</c:otherwise>
			   			</c:choose>
					<li class="cohorts_item" id="li_cm_${list1.corpsMember.id}">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><a class="open_corp setHelpText" name="Corp Member" id="cm_${list1.corpsMember.id}" href="javascript:void(0);">${list1.corpsMember.firstName} ${list1.corpsMember.lastName}</a>
							
						${cmYear} ${gradeLevel} ${list1.corpsMember.subjectGroup} 
						 	<input type="hidden" id="cm_first_name_char_${list1.corpsMember.id}"  value="${fn:substring(list1.corpsMember.firstName, 0, 1)}" />
					    	<input type="hidden" id="cm_last_name_char_${list1.corpsMember.id}"  value="${fn:substring(list1.corpsMember.lastName, 0, 1)}" />
						 	<input type="hidden" id="cm_name_${list1.corpsMember.id}" value="${list1.corpsMember.firstName} ${list1.corpsMember.lastName}" /> 
						 	<input type="hidden" id="cm_corps_year_${list1.corpsMember.id}" value="${list1.corpsMember.cmYear}" />
					     	<input type="hidden" id="cm_grade_level_${list1.corpsMember.id}" value="${list1.corpsMember.gradeLevel}" />
					      	<input type="hidden" id="cm_primary_subject_${list1.corpsMember.id}" value="${list1.corpsMember.subjectGroup}" />
					       	<input type="hidden" id="cm_subject_modifier_${list1.corpsMember.id}" value="${list1.corpsMember.subjectModifier}" />
					        <input type="hidden" id="cm_school_name_${list1.corpsMember.id}" value="${list1.corpsMember.schoolBean.schoolName}" />
					        <input type="hidden" id="cm_school_neighborhood_${list1.corpsMember.id}" value="${list1.corpsMember.schoolBean.neighborhood}" />
						<c:if test="${list1.notFitForCohort}"><span id="redDot_${list1.corpsMember.id}"><img src="images/dot_red.png" title="check for fit" alt="check for fit" width="14" height="14"></span></c:if>
						</td>
						 <td>
						    <input type="hidden" id="cmyear_${list1.corpsMember.cmYear}" class="cmyear Corpsyear" value="${list1.corpsMember.cmYear}"/>
					    </td> 
						<td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" class="icon_btn copy_cohorts" id="move_cm_${list1.corpsMember.id}"><img src="images/icon_drag.png" width="20" height="17"></a></td>
						<td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_cohorts" id="delete_cm_${list1.corpsMember.id}_${list1.corpsMember.isHired}"><img src="images/icon_delete.png" width="18" height="18"></a></td>
					  </tr>
					</table>
					</li>
					</c:forEach>
				</ul>
				<div class="clear"></div>
				</div>
			</c:if> 
			</c:forEach>
		</c:if>
</c:forEach>
 <!-- Ending MTLD Detail List -->

<div id="total_groups" class="scrollbar_box">
	
<!-- Starting:Cohort formation -->
<c:set var="cohortList" value="<%=TFAConstants.COHORT_LIST%>"/>
<c:set var="count" value="1"/>
<c:forEach var="entry" items="${cohortsdata}">
	<c:if test="${entry.key == cohortList}">
		<c:forEach var="list" items="${entry.value}" varStatus="loop">
		<!-- Added by Arun -->
		 <div id="cohortDetailsList" style="display: none;">${list}</div>
		 <!-- Added by Arun Ends -->
			<c:choose>
					<c:when test="${list.seededMtldBean !=null}">
						<c:set var="id" value="${list.seededMtldBean.id}" /> 
						<c:set var="name" value="${list.seededMtldBean.firstName} ${list.seededMtldBean.lastName}" /> 
						<c:set var="fNameCharacter" value="${fn:substring(list.seededMtldBean.firstName, 0, 1)}" />
						<c:set var="lNameCharacter" value="${fn:substring(list.seededMtldBean.lastName, 0, 1)}" />  
						<%-- <c:set var="tenure_role" value="${list.seededMtldBean.tenureRole} Year MTLD" /> --%>
						<c:if test="${not empty list.seededMtldBean.tenureRole}">
						<c:choose>
	   						<c:when test="${list.seededMtldBean.tenureRole>1}">
	   							<c:set var="tenure_role" value="${list.seededMtldBean.tenureRole} Years MTLD" /> 
	   						</c:when>
	   						<c:otherwise>
	   							<c:set var="tenure_role" value="${list.seededMtldBean.tenureRole} Year MTLD" /> 
	   						</c:otherwise>
	   						</c:choose> 
						</c:if>
					</c:when>
					<c:otherwise>
						<c:set var="id" value="${list.seededCMBean.id}" /> 
						<c:set var="name" value="${list.seededCMBean.firstName} ${list.seededCMBean.lastName}" /> 
						<c:set var="fNameCharacter" value="${fn:substring(list.seededCMBean.firstName, 0, 1)}" />
						<c:set var="lNameCharacter" value="${fn:substring(list.seededCMBean.lastName, 0, 1)}" />  
						<c:set var="tenure_role" value="" />
						<c:if test="${list.seededCMBean.cmYear!=null}">
							<c:set var="tenure_role" value="${list.seededCMBean.cmYear}" />
						</c:if>
					</c:otherwise>
			</c:choose>
			<c:set var="cohortId" value="${list.id}" />
			<c:choose>
					<c:when test="${list.isFinalCohort==true}">
						<input type="hidden" class="isFinalCohort_${cohortId}" value="true"/>
					</c:when>
					<c:otherwise>
						<input type="hidden" class="isFinalCohort_${cohortId}" value="false"/>
					</c:otherwise>
			</c:choose> 
			<div class="cohortGroup" id="cohortGroup_${cohortId}"> 
			<c:choose>
				<c:when test="${count==1}">
					<div class="each_group each_group_selected" id="each_group_${id}_${cohortId}">
					<c:set var="count" value="2"/>
				</c:when>
				<c:otherwise>
					<div class="each_group" id="each_group_${id}_${cohortId}">
				</c:otherwise>
			</c:choose>	
      		
      		
      		 <table width="99%" border="0" cellspacing="0" align="left" cellpadding="0" id="cohort_table_${id}_${cohortId}">
				<tr>
			          <td width="20%" rowspan="2" align="left" valign="top"><div class="user_img">${fn:toUpperCase(fNameCharacter)}${fn:toUpperCase(lNameCharacter)}</div></td>
			          <td width="75%" align="left" valign="bottom"><a href="javascript:void(0);" name="expandCohort" class="showCohortDetails setHelpText" id="cohort_name_${id}_${cohortId}"><c:out value="${name}"/></a></td>
			          <td width="9%" align="right" valign="top"><a href="javascript:void(0);" id="${cohortId}" class="group_popup_icon setHelpText" name="Cohort/Group"><img src="images/popup_icon.png" width="20" height="18"></a></td>
			   </tr>
			  <tr>
		          <td colspan="2" align="left" valign="top">${tenure_role}<br>
		          <c:choose>
			          	<c:when test="${list.schoolRep=='1'}">
			          		<strong>${list.schoolRep}</strong class="schools_represented_${cohortId}"> School Represented <br>
			          	</c:when>
			          	<c:otherwise>
			          		<strong class="schools_represented_${cohortId}">${list.schoolRep}</strong> Schools Represented <br>
			          	</c:otherwise>
			          </c:choose>
			         <c:choose>  
			       <c:when test="${not empty list.maxDistance && list.overDistance==true}">   
		           <strong>${list.maxDistance}</strong> Miles apart (max)
				    	<img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" > 
				    </c:when>
				    
				    <c:otherwise>
				     <c:if test="${not empty list.maxDistance}">
				      <strong>${list.maxDistance}</strong> Miles apart (max)
				     </c:if>
				     
				    </c:otherwise>
				     </c:choose>
				    
				    <br> 
		           <table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">
				 	 <tr>
					    <td width="73%" align="left" valign="middle"><span title="Basics Score"><%=TFAConstants.CRITERIA_CATEGORY_BASICS%></span></td>
					    <td width="27%" align="left" valign="middle"><span title="Content Score">${list.basicsCriteriaPer}%</span></td>
					  </tr>
					  <tr>
					    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_CONTENT%></td>
					    <td align="left" valign="middle">${list.contentCriteriaPer}%</td>
					  </tr>
					  <tr>
					    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY%></td>
					    <td align="left" valign="middle">${list.geographicCriteriaPer}%</td>
					  </tr>
					  <tr>
					    <td align="left" valign="middle"><%=TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS%></td>
					    <td align="left" valign="middle">${list.relationshipsCriteriaPer}%</td>
					  </tr> 

				</table></td>
        	  </tr>

			 	
			  </table>
			<div class="group_detail_counting">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				  	<c:choose>
					  	<c:when test="${list.cohortDetailBean.size()==null}">
					  		<c:set var="corpsMemberCountText" value="0 Corps Members"/>
					  	</c:when>
					  	<c:when test="${list.cohortDetailBean.size()==1}">
					  		<c:set var="corpsMemberCountText" value="1 Corp Member"/>
					  	</c:when>
					  	<c:otherwise>
					  		<c:set var="corpsMemberCountText" value="${list.cohortDetailBean.size()} Corps Members"/>
					  	</c:otherwise>
				  	</c:choose>
				    <td align="left"><%-- "${entry.value.CohortBean.corps.length}" --%><span style="white-space: nowrap;" class="cmcount_${list.id}" >${corpsMemberCountText}</span>
				   <%--  <c:if test="${list.overDistance==true}">
				    	<img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" > 
				    </c:if> --%>
				    </td>
				    
				    <c:choose>
				    	<c:when test="${list.isFinalCohort==false}">
				    			
				    			<td align="right"><input name="" type="button" class="save_btn" value="save" id="cohort_save_${cohortId}"><input name="" type="button" value="Unlock" class="unlock_btn" id="cohort_unlock_${cohortId}" style="display:none;"></td>
				    	</c:when>
				    	<c:otherwise>
				    			
				    			<td align="right"><input name="" type="button" class="save_btn" value="save" id="cohort_save_${cohortId}" style="display:none;"><input name="" type="button" value="Unlock" class="unlock_btn" id="cohort_unlock_${cohortId}"></td>
				    	</c:otherwise>
				    </c:choose>
				  </tr>
				</table>
			    </div>
			    	<div class="clear"></div>
			  	</div>
			  </div>
		</c:forEach>
		</c:if> 
	</c:forEach>
	
<!-- Ending:Cohort formation-->
    	
    <div class="clear"></div>
    </div>        
            
  <div class="clear"></div>
</div>



  <div class="clear"></div>
</div>
 
  
  
	<div class="clear"></div>
</div>



<div class="body_overlay" style="display:none;"></div>

<!--  Custome Scrollbar  -->
<script src="js/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript">
$(document).ready(function(){

	/* Custome Radio Btn and Check Box   */
	$('input:radio').screwDefaultButtons({
				image: 'url("images/custom_radio.png")',
				width: 22,
				height: 22
			});
			
			/* $('input:checkbox').screwDefaultButtons({
				image: 'url("images/checkboxSmall.jpg")',
				width: 43,
				height: 43
			});
			*/
			
			

	/* ----------------------  Custome Scroll BAr  ---------------------  */
	$(".scrollbar_box").mCustomScrollbar({
		scrollButtons:{
			enable:true
		}
		
});



});
</script>
<br/>
		
<div class="status_bar" style="display: none;"></div>

<jsp:include page="cohortPopups.jsp"></jsp:include>
</body>

</html>
