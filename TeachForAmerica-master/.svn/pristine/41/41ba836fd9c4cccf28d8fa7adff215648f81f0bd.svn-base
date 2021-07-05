<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="org.tfa.mtld.service.constants.TFAConstants"%>



<script src="http://jquery.bassistance.de/validate/jquery.validate.js"></script>
<script
	src="http://jquery.bassistance.de/validate/additional-methods.js"></script>
<script type="text/javascript" src="js/criteria.min.js"></script>


<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<meta charset="utf-8">
<title><spring:message code="label.title" /></title>
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">


<script src="js/jquery.screwdefaultbuttonsV2.js"></script>
<script src="js/jquery.validate.js"></script>
<script src="js/additional-methods.js"></script>

<script type="text/javascript">

var  ZERO_PRIORITY_VALUE=<%=TFAConstants.CRITERIA_PRIORITY_ZERO%> 

</script>




</head>



<body>



	<c:set var="ZeroPriorityValue"
		value="<%=TFAConstants.CRITERIA_PRIORITY_ZERO%>" />
	<c:set var="FirstPriorityValue"
		value="<%=TFAConstants.CRITERIA_PRIORITY_ONE%>" />
	<c:set var="SecondPriorityValue"
		value="<%=TFAConstants.CRITERIA_PRIORITY_TWO%>" />
	<c:set var="ThirdPriorityValue"
		value="<%=TFAConstants.CRITERIA_PRIORITY_THREE%>" />
	<c:set var="FourthPriorityValue"
		value="<%=TFAConstants.CRITERIA_PRIORITY_FOUR%>" />

	<div class="container">



		<div class="criteria_heading">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td valign="middle" align="center">&nbsp;</td>
					<td valign="middle" align="left">&nbsp;</td>
					<td valign="middle" align="left" class="gray11" colspan="2"><spring:message
							code="label.exclude" /></td>
					<td valign="middle" align="center">&nbsp;</td>
					<td valign="middle" align="right" class="gray11" colspan="2"><spring:message
							code="label.highestPriority" /></td>
					<td valign="middle" align="left">&nbsp;</td>
				</tr>


				<tr>
					<td width="5%" height="35" align="center" valign="middle">&nbsp;</td>
					<td width="45%" align="left" valign="middle"><spring:message
							code="label.criteriaHeading" /></td>
					<td width="6%" align="center" valign="middle"><spring:message
							code="label.priority0" /></td>
					<td width="6%" align="center" valign="middle"><spring:message
							code="label.priority1" /></td>
					<td width="6%" align="center" valign="middle"><spring:message
							code="label.priority2" /></td>
					<td width="6%" align="center" valign="middle"><spring:message
							code="label.priority3" /></td>
					<td width="6%" align="center" valign="middle"><spring:message
							code="label.priority4" /></td>
					<td width="20%" align="left" valign="middle"><img
						src="images/gray_arrow.png" width="25" height="25"
						style="float: left"> <spring:message
							code="label.setPriority" /></td>
				</tr>
			</table>

		</div>


		<form:form method="POST" action="makeMyMatches" id="criteriaForm"
			commandName="criteriaFormBean">
			<input type="hidden" id="isPriorityForAllTheCriteriaIsNotZero"
				name="isPriorityForAllTheCriteriaIsNotZero"
				value="${criteriaFormBean.isPriorityForAllTheCriteriaIsNotZero}" />
			<c:set var="priorityValues">
				<spring:message code="label.priorityValues" />
			</c:set>

			<c:forEach var="categoryList"
				items="${criteriaFormBean.criteriaCategoryBeanList}" varStatus="statusOuter">
				<div class="criteria_title">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="5%" height="35" align="center" valign="middle"
								class="expand_collaps_img"><img
								src="images/expand_icon.png" width="11" height="7"
								class="expand_icon" style="display: none"> <img
								src="images/collapes_icon.png" width="11" height="7"
								class="collapes_icon"></td>
							<td width="95%" align="left" valign="middle">${categoryList.categoryName}
								<input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].categoryName"
								value="${categoryList.categoryName}" /> <input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].id"
								value="${categoryList.id}" />
							</td>

						</tr>
					</table>

				</div>

				<div class="criteria_detail">

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						id="">

						<c:forEach var="criteriaList"
							items="${categoryList.criteriaBeans}" varStatus="statusInner">

							<input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].name"
								value="${criteriaList.name}" />
							<input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].id"
								value="${criteriaList.id}" />
							<input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].description"
								value="${criteriaList.description}" />
							<input type="hidden"
								name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].classAPI"
								value="${criteriaList.classAPI}" />


							<tr>
								<td width="5%" height="35" align="center" valign="middle">&nbsp;</td>
								<td width="45%" align="left" valign="middle">
									${criteriaList.name} <input type="hidden" name="id"
									value='${criteriaList.id}'> <br /> <span
									class="gray11"> ${criteriaList.description} </span>

								</td>
								<c:choose>
									<c:when test="${not empty criteriaList.priorityValue}">


										<c:choose>
											<c:when
												test="${criteriaList.priorityValue==ZeroPriorityValue}">
												<td width="6%" align="center" valign="middle"
													class="exclude setHelpText" id="${ZeroPriorityValue}">
													<input type="radio" class="priorityValue custome_radio"
													onclick="setFieldValidation(${criteriaList.id}, ${ZeroPriorityValue});"
													checked="checked"
													name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
													value="${ZeroPriorityValue}" />
												</td>
											</c:when>

											<c:otherwise>
												<td width="6%" align="center" valign="middle"
													id="${ZeroPriorityValue}" class="other setHelpText"><input
													type="radio" class="priorityValue custome_radio"
													onclick="setFieldValidation(${criteriaList.id}, ${ZeroPriorityValue});"
													name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
													value="${ZeroPriorityValue}" /></td>

											</c:otherwise>
										</c:choose>


										<c:forTokens items="${priorityValues}" delims="," var="value">


											<c:choose>
												<c:when test="${criteriaList.priorityValue==value}">
													<td id="${value}" class="other setHelpText"  valign="middle" align="center" ><input
														type="radio"
														onclick="setFieldValidation(${criteriaList.id}, ${value});"
														class="priorityValue custome_radio" checked="checked"
														name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
														value="${value}" /></td>
												</c:when>
												<c:otherwise>
													<td id="${value}" class="other setHelpText" valign="middle" align="center"><input
														type="radio"
														onclick="setFieldValidation(${criteriaList.id}, ${value});"
														class="priorityValue custome_radio"
														name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
														value="${value}" /></td>
												</c:otherwise>
											</c:choose>

										</c:forTokens>
									</c:when>

									<c:otherwise>
										<td width="6%" align="center" valign="middle" id="0"
											class="exclude setHelpText"><input type="radio"
											class="priorityValue custome_radio"
											onclick="setFieldValidation(${criteriaList.id}, ${ZeroPriorityValue});"
											checked="checked"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
											value="${ZeroPriorityValue}" />
										<td width="6%" align="center" valign="middle" id="1"
											class="other setHelpText"><input type="radio"
											class="priorityValue custome_radio"
											onclick="setFieldValidation(${criteriaList.id}, ${FirstPriorityValue});"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
											value="${FirstPriorityValue}" />
										<td width="6%" align="center" valign="middle" id="2"
											class="other setHelpText"><input type="radio"
											class="priorityValue custome_radio"
											onclick="setFieldValidation(${criteriaList.id}, ${SecondPriorityValue});"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
											value="${SecondPriorityValue}" />
										<td width="6%" align="center" valign="middle" id="4"
											class="other setHelpText"><input type="radio"
											class="priorityValue custome_radio"
											onclick="setFieldValidation(${criteriaList.id},  ${ThirdPriorityValue});"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
											value="${ThirdPriorityValue}" />
										<td width="6%" align="center" valign="middle" id="8"
											class="other setHelpText"><input type="radio"
											class="priorityValue custome_radio"
											onclick="setFieldValidation(${criteriaList.id}, ${FourthPriorityValue});"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].priorityValue"
											value="${FourthPriorityValue}" /></td>


									</c:otherwise>


								</c:choose>



								<td width="20%" align="left" valign="middle"><c:if
										test="${criteriaList.fieldType=='Text Field'}">

										<input type="hidden"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].fieldType"
											value="${criteriaList.fieldType}" />
										<input type="hidden"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].fieldPlaceholder"
											value="${criteriaList.fieldPlaceholder}" />
										<input type="hidden"
											name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].fieldValidation"
											value="${criteriaList.fieldValidation}" />

										<c:choose>
											<c:when
												test="${(empty criteriaList.priorityValue) ||  (criteriaList.priorityValue==ZeroPriorityValue)}">

												<input type="text"
													maxlength="${criteriaList.fieldValidation=='number_two_digit'?2:4}"
													disabled="disabled"
													onChange="setFieldValidation(${criteriaList.id},'none');"
													name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].fieldValue"
													value="${criteriaList.fieldValue=='0'?'':criteriaList.fieldValue}"
													id="textField_${criteriaList.id}"
													alt="${criteriaList.fieldValidation}"
													placeholder="${criteriaList.fieldPlaceholder}"
													class="input_box" />
												<br />
											</c:when>
											<c:otherwise>
												<input type="text"
													maxlength="${criteriaList.fieldValidation=='number_two_digit'?2:4}"
													onChange="setFieldValidation(${criteriaList.id},'none');"
													name="criteriaCategoryBeanList[${statusOuter.index}].criteriaBeans[${statusInner.index}].fieldValue"
													value="${criteriaList.fieldValue=='0'?'':criteriaList.fieldValue}"
													id="textField_${criteriaList.id}"
													alt="${criteriaList.fieldValidation}"
													placeholder="${criteriaList.fieldPlaceholder}"
													class="input_box" />
												<br />

											</c:otherwise>
										</c:choose>
										<label style="color: red; font-size: 10px;"
											class="customError_${criteriaList.id}"
											id="error_${criteriaList.id}"
											for="textField_${criteriaList.id}"></label>
									</c:if></td>

							</tr>
						</c:forEach>
					</table>
				</div>
			</c:forEach>

			<div class="make_matches_btn">
				<div class="left_col">
					<table width="100%" border="0" cellspacing="0" cellpadding="3">
						<tr>
							<td width="3%" align="left" valign="middle"><c:choose>
									<c:when test="${criteriaFormBean.includeMTLD}">
										<input type="checkbox" checked="checked" name="includeMTLD"
											id="includeMTLD">
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="includeMTLD" id="includeMTLD" />
									</c:otherwise>
								</c:choose></td>
							<td width="97%" align="left" valign="middle"><spring:message
									code="label.includeMTLD" /></td>
						</tr>
						<tr>
							<td align="left" valign="middle"><c:choose>
									<c:when test="${criteriaFormBean.includeUnhiredCM}">
										<input type="checkbox" name="includeUnhiredCM"
											checked="checked" id="includeUnhiredCM">
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="includeUnhiredCM"
											id="includeUnhiredCM">


									</c:otherwise>
								</c:choose></td>
							<td align="left" valign="middle"><spring:message
									code="label.includeCM" /></td>
						</tr>
						<!-- Added BY Lovely Ram -->
						<tr>
							<td align="left" valign="middle"></td>
							<td align="left" valign="middle"><span
								style="font-size: 16px; color: #000000;"><spring:message
										code="label.cohortCount" /></span> <input type="text"
								name="cohortCount" value="${criteriaFormBean.cohortCount}"
								maxlength="2" id="cohortCount" class="input_box"
								style="width: 50px; padding: 2px; font-size: 12px" /> <label
								style="color: red; font-size: 10px;" class="ErrorCohortCount"
								id="ErrorCohortCount"></label></td>
						</tr>
					</table>
				</div>
				<div class="right_col" style="padding-top: 12px;">
					<input name="" type="button" onclick="submitform();"
						value="<spring:message code="label.makeMatch"/>" class="green_btn">
				</div>
				<div class="clear"></div>
			</div>
		</form:form>

		<div class="status_bar" style="display: none;"></div>
</body>



</html>
