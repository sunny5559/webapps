<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
<link href="stylesheet/main_style.css" rel="stylesheet" type="text/css">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<script type="text/javascript">
	$('#criteria_screen').removeClass('selected');
	$('#mtld_info_screen').addClass('selected');
	$('#matching_screen').removeClass('selected');

	var _validFileExtensions = [ ".xlsx", ".xls", ".xlt", ".xla" ];
	var _validFileExtensionsforCorps = [ ".xlsx", ".xls" ];

	function Validate(oForm, uploadInfo) {

		var arrInputs = '';
		var oInput = '';

		if (uploadInfo == 'MTLD') {
			arrInputs = document.fileUpload.file;
			oInput = document.fileUpload.file;
		} else {
			arrInputs = document.corpsfileUpload.file;
			oInput = document.corpsfileUpload.file;
		}
		if (oInput.type == "file") {

			var sFileName = oInput.value;

			if (sFileName.length > 0) {

				var blnValid = false;
				if (uploadInfo == 'MTLD') {
					for ( var j = 0; j < _validFileExtensions.length; j++) {
						var sCurExtension = _validFileExtensions[j];

						if (sFileName.substr(
								sFileName.length - sCurExtension.length,
								sCurExtension.length).toLowerCase() == sCurExtension
								.toLowerCase()) {
							blnValid = true;
							break;
						}

					}
				} else {
					for ( var k = 0; k < _validFileExtensionsforCorps.length; k++) {
						var sCurrExtension = _validFileExtensionsforCorps[k];

						if (sFileName.substr(
								sFileName.length - sCurrExtension.length,
								sCurrExtension.length).toLowerCase() == sCurrExtension
								.toLowerCase()) {
							blnValid = true;

							$("#fileExtension")
									.val(sCurrExtension.toLowerCase());
							break;
						}
					}
				}

				if (!blnValid) {
					if (uploadInfo == 'MTLD') {
						alert("Sorry, " + sFileName
								+ " is invalid, only excel file are allowed");
						
					}
					else{
						$("#cmUploadFile").val("");
						alert("Sorry, " + sFileName
								+ " is invalid, only .xlsx or .xls file  are allowed");
					}
					
					return false;
				}
			} else {
				alert("Please select a file");
				return false;
			}
		}

		return true;
	}

	$(document).ready(function() {
		
		$("#cmUploadFile").val("");
		$("#info_mtld_btn").click(function() {

			$("#mtld_upload_div").show();
			$("#chohorts_upload_div").hide();
			$(".cmlabel").text("");
		});
		$("#info_chohorts_btn").click(function() {

			$("#chohorts_upload_div").show();
			$("#mtld_upload_div").hide();
			$(".cmlabel").text("");
		});

	});
</script>

</head>
<body>
	<div class="container">



		<div
			style="width: 80%; margin: 0 auto; margin-top: 25px; padding: 30px 10%; border: 1px solid #cccccc; background-color: #eeeeee;">


			<div
				style="float: left; width: 25%; border-right: 1px solid #cccccc; margin-right: 10%;">
				<input type="submit" id="info_mtld_btn" value="MTLD Info"
					class="green_btn" /> <input type="submit" id="info_chohorts_btn"
					value="CM Info" class="green_btn" style="margin-top: 30px;" />
			</div>


			<div style="width: 50%; float: left; display: none;"
				id="mtld_upload_div">
				<form:form method="post" action="save.html" id="fileUpload"
					name="fileUpload" modelAttribute="uploadForm"
					enctype="multipart/form-data"
					onsubmit="return Validate(this.form,'MTLD')">
					<div class="region_name" style="border: 0 none">Upload MTLD
						Info</div>
					<table id="fileTable">

						<tr>
							<td><input name="file" type="file" /></td>
						</tr>

						<tr>
							<td><c:out value="${errorMessage}" /></td>
						</tr>

					</table>
					<br />
					<input type="submit" value="Upload" class="green_btn" />&nbsp;&nbsp;<input
						type="button" onClick="location.href='selectCriteria'"
						value="Cancel" class="green_btn" />
				</form:form>
				<div class="clear"></div>
			</div>
			<c:choose>
				<c:when test="${null != success} }">
					<div class="Message">
						 <label class="errorLabel cmlabel">${success}</label>
					</div>
				</c:when>
				<c:otherwise>
					<c:if test="${null != uploadMessage}">
						<div class="Message">
							<label class="uploadMessage cmlabel">${uploadMessage}</label>
						</div>
					</c:if>

					<c:if test="${null != uploadError}">
						<div class="Error">
							<label class="uploadError cmlabel">${uploadError}</label>
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
			
			<br/>
			
			<c:choose>
				<c:when test="${null != portfolioSuccess}">
					<div class="Message">
						<label class="errorLabel cmlabel">${portfolioSuccess}</label>
					</div>
				</c:when>
				<c:otherwise>
					<c:if test="${null != portfolioUploadMessage}">
						<div class="Message">
							 <label class="uploadMessage cmlabel">${portfolioUploadMessage}</label>
						</div>
					</c:if>

					<c:if test="${null != portolioUploadError}">
						<div class="Error">
							 <label class="uploadError cmlabel">${portolioUploadError}</label>
						</div>
					</c:if>
					<c:if test="${null != portfolioNoRecordInserted}">
						<div class="Message">
							 <label class="uploadMessage cmlabel" >${portfolioNoRecordInserted}</label>
						</div>
					</c:if>
					<c:if test="${null != portfolioSheetNotExist}">
						<div class="Message">
							 <label class="uploadMessage cmlabel">${portfolioSheetNotExist}</label>
						</div>
					</c:if>
					
					
				</c:otherwise>
				
				
				
				
			</c:choose>
			
			
			
			
			
			<div style="width: 50%; float: left; display: none;"
				id="chohorts_upload_div">
				<form:form method="post" action="uploadCorpsmembers"
					id="corpsfileUpload" name="corpsfileUpload"
					modelAttribute="corpsUploadForm" enctype="multipart/form-data"
					onsubmit="return Validate(this.form,'CM')">
					<div class="region_name" style="border: 0 none">
						<spring:message code="label.uploadCorps" />
					</div>



					<input type="hidden" id="fileExtension" name="fileExtension"
						value="" />
					<table id="fileTable">
						<tr>
							<td><input name="file" type="file" id="cmUploadFile" /></td>
						</tr>

						<tr>
							<td><c:out value="${errorMessage}" /></td>
						</tr>

					</table>
					<br />
					<input type="submit" value="Upload" class="green_btn" />&nbsp;&nbsp;<input
						type="button" onClick="location.href='selectCriteria'"
						value="Cancel" class="green_btn" />
				</form:form>


				<div class="clear"></div>
			</div>

			<div class="clear"></div>

		</div>

		<div class="clear"></div>

	</div>
</body>
</html>
