/**
 * 
 */

function createCriteriaList(){
	
	var isValidForm=true;
	
	var formValid=$("#criteriaForm").valid();
	
	isValidForm = validateCohortCount($('#cohortCount').val());
	
	
	
	
	//alert("formValid---"+formValid);
	//alert(formValid);
	if(isValidForm){
	setBasicsValue();

	}
	function validateCohortCount(cohortCount) {
		$('#ErrorCohortCount').text("");
		  //Two digits, so anything between and including 0 and 99
		if(!(/^[0-9 ]*$/.test(cohortCount.toLowerCase())))
		{
			$('#ErrorCohortCount').text("Cohort count value should be in the range of (0-99) whole number");
			$( "#ErrorCohortCount" ).focus();
			return false;
		}
        if(!(/^\d{1,2}$/.test(parseInt(cohortCount))) || cohortCount.indexOf(".")!=-1)
        {
           // alert(helperMsg);
        	//Cohort Count value should be in the range of (0-99) whole number
        	$('#ErrorCohortCount').text("Cohort count value should be in the range of (0-99) whole number");
        	 $( "#ErrorCohortCount" ).focus();
            return false;
        }
        
        return true;

		
	}
	
	
	
	function setBasicsValue() {

		var rowCount = $('#Basics tr').length;
		var basicsArray = [];
		for (var i = 0; i <= rowCount; i++) {
			basicsArray[i] = iterateClass("Basics" + i);

		}

		/*
		 * $.each(myArray, function( key, value ) {
		 * 
		 * 
		 * alert(myArray[key] + "array Id--" +value ); // alert("array value--
		 * "+value.priorityValue ); // alert("array value-- "+value.fieldValue);
		 * });
		 */

		$("#basicsValues").val(basicsArray);

		if (isValidForm) {
			setContentValue();

		} else {

			return false;
		}

	}
     
	 function setContentValue() {

		var rowCount = $('#Content tr').length;
		var contentArray = [];
		for (var i = 0; i <= rowCount; i++) {
			contentArray[i] = iterateClass("Content" + i);

		}

		$("#contentValues").val(contentArray);

		if (isValidForm) {
			setGeographyValue();

		} else {

			return false;
		}

	}
        

	 function setGeographyValue() {

		var rowCount = $('#Geographic tr').length;
		var geographyArray = [];
		for (var i = 0; i <= rowCount; i++) {
			geographyArray[i] = iterateClass("Geographic" + i);

		}

		$("#geographyValues").val(geographyArray);
		if (isValidForm) {
			setRelationshipsValue();
		} else {

			return false;
		}

	}
        

	  function setRelationshipsValue() {

		var rowCount = $('#Relationships tr').length;
		var relationshipsArray = [];
		for (var i = 0; i <= rowCount; i++) {
			relationshipsArray[i] = iterateClass("Relationships" + i);

		}

		$("#relationshipsValues").val(relationshipsArray);

	}
   	
        
        
        function iterateClass(className){

            var priorityValue="none";
            var criteriaId="none";
            var fieldValue = 0;
            var criteriaValues;
            
         
            $("."+className +" input").each(function () {
            	if(isValidForm){
            	
                  switch ($(this)[0].type) {
                          case "hidden":
                             criteriaId = $(this).val();
                          //alert("hidden--"+criteriaId);
                             break;
                           
                          case "radio":
                          //alert("type----"+$(this)[0].type);
                          var varName=$(this).attr("name");
                 
                   priorityValue = $('input[name='+varName+']:checked').val(); 
                        //alert("selValue---"+priorityValue);
                          break;
                          
                           case "text" :
                        	 
                         	  fieldValue =   $(this).val().trim();
                         	  var fieldId=$(this).attr("id");
                         	 // alert("name---"+fieldId.substr(fieldId.indexOf("_")+1 , fieldId.length));
                         	 isValidForm= setFieldValidation(fieldId.substr(fieldId.indexOf("_")+1 , fieldId.length),"none");
                         	 if(!isValidForm){
                         		 return false;
                         	 }
                         	// alert("isValidForm--"+isValidForm);
                         	  if(fieldValue==""  || fieldValue.length==0){
                         		 fieldValue=0; 
                         	  }
                         	  break;
                         	 
      
                  }
                  criteriaValues = criteriaId+"#"+priorityValue+"#"+fieldValue;
            	}
       });
          
            
           //found1.priorityValue=priorityValue;
            //found1.criteriaId=criteriaId;
            //found1.fieldValue = fieldValue;
            
           // alert("value -- "+found1.priorityValue  + "id" + found1.criteriaId + "filed value --"+ found1.fieldValue );
            
            return criteriaValues;
            }

        
        if(formValid  && isValidForm   && $("#criteriaForm").attr("novalidate")!="novalidate" ){
        	$( "#criteriaForm" ).submit();
        	}
        
        
        
     }



function setFieldValidation(criteriaId,priorityValue){
	
	var isValidForm=true;
	//var hasAnyError=false;
	
	//alert("default---"+priorityValue);
	
	if(priorityValue=="none"){
		
		priorityValue = $('input[name='+criteriaId+']:checked').val() ;
	}
	//alert("default 3---"+priorityValue);
	
	if(priorityValue==ZERO_PRIORITY_VALUE){
		if($('#textField_'+criteriaId).length){
			//alert("in if----Rule removed");
			$('#textField_'+criteriaId).rules("remove", "required");
			isValidForm=true;
			$(".customError_"+criteriaId).html("");	
			$('#textField_'+criteriaId).val("");
			$('#textField_'+criteriaId).prop('disabled', true);
			
		}
		
	}else{
		if($('#textField_'+criteriaId).length){
			//alert("in else----Rule added");
		//$('#textField_'+criteriaId).rules("add","required");
			$('#textField_'+criteriaId).prop('disabled', false);
		var fieldName=$('#textField_'+criteriaId).attr("name");
		var fieldValue=$('#textField_'+criteriaId).val();
		
		
		//alert("name=="+fieldName+"value--"+fieldValue);
		
	/*	$('#textField_'+criteriaId).rules( "add", {
			  required: true,
			  regexp:  /^\+?(0|[1-9]\d*)$/.test(fieldValue),
			 // number: true,
			  maxlength: 2,
			  messages: {
			    required: "Required input",
			   regexp: jQuery.format("Please, value should be in the range of (1-99) with while number only.")
			  }
			});*/
		
		/*$.validator.addMethod("regx", function(fieldValue, element, regexpr) {          
		    return regexpr.test(value);
		}, "Please enter a valid pasword.")*/
		
		if(fieldName=="number_two_digit"){
	
		
			if(fieldValue.length>0 &&   (fieldValue>=1 && fieldValue<=99)  && (!isNotWholeNumber(fieldValue))  && (fieldValue.indexOf(".")==-1) ){
			
				$(".customError_"+criteriaId).html("");
				isValidForm=true;
				$('#textField_'+criteriaId).val(fieldValue.trim());
			//	$('#textField_'+criteriaId).rules("remove", "required");
			}else{
				
				$(".customError_"+criteriaId).html("Value should be in the range of (1-99) whole number");	
				isValidForm=false;
				
			
				
				
				$("#criteriaForm").attr('novalidate', 'novalidate');
			//	hasAnyError=true;
				return false;
				
			}
	
		}
	/*	if(fieldName=="number_one_digit"){
			
			if( fieldValue.length>0 && (fieldValue>=1 && fieldValue<=9) ){
				$(".customError_"+criteriaId).html("");
				isValidForm=true;
				//$('#textField_'+criteriaId).rules("remove", "required");
				
			}else{
				$(".customError_"+criteriaId).html("Value should be in the range of 1-9");	
				isValidForm=false;
				$("#criteriaForm").attr('novalidate', 'novalidate');
				//hasAnyError=true;
				return false;
				
			}
			
		
	
		}*/
		if(fieldName=="decimal_tenth_place"){
			if( fieldValue.length>0  && isValidTenthDecimalNumber(fieldValue)  &&  (fieldValue>=0.1 && fieldValue<=99)  ){
				$(".customError_"+criteriaId).html("");
				isValidForm=true;
				if(fieldValue.indexOf(".")==0){
					$('#textField_'+criteriaId).val("0"+fieldValue.trim());
				}
				//$('#textField_'+criteriaId).rules("remove", "required");
				
			}else{
				$(".customError_"+criteriaId).html("Value should in the range of (0.1-99) or upto tenth decimal");	
				isValidForm=false;
				$("#criteriaForm").attr('novalidate', 'novalidate');
				//hasAnyError=true;
				return false;
			}
			
	
		}
		
		}
	}
	
	//alert("isValidForm function---"+isValidForm);
	isValidForm?$("#criteriaForm").removeAttr('novalidate'):$("#criteriaForm").attr('novalidate', 'novalidate');
		
	return isValidForm;
	
	
}

function isNotWholeNumber(str){
	
	return str % 1 != 0;
}

function isValidInteger(str) {
	var ex=/^[1-9]*$/;
	//alert("integer----"+ex.test(str));
	return ex.test(str);
	
    //return /^\+?(0|[1-9]\d*)$/.test(str);
}

function isValidTenthDecimalNumber(str) {
	//alert("valid decimal---"+/^[0-9]\d{0,9}(\.\d{1,1})?%?$/.test(str));
	  return /^\d*(\.\d{1,1})?%?$/.test(str);
	
	  //return /^\d+(\.\d{1,1})?$/.test(str);
}






$(document).ready(function(){
	
	$('.criteria_title').click(function(){
		
		if ($(this).closest('.criteria_title').next('.criteria_detail').is(':hidden')) {
			
			$(this).closest('.criteria_title').next('.criteria_detail').slideDown("fast");
			$(this).closest('.criteria_title').find('.expand_icon').hide();
			$(this).closest('.criteria_title').find('.collapes_icon').show();
			
		}else{
			$(this).closest('.criteria_title').next('.criteria_detail').slideUp("fast");
			$(this).closest('.criteria_title').find('.expand_icon').show();
			$(this).closest('.criteria_title').find('.collapes_icon').hide();
		}
		
	});
	
	

	/* Custome Radio Btn and Check Box   */
	$('.custome_radio').screwDefaultButtons({
		image: 'url("images/custom_radio.png")',
		width: 22,
		height: 22
});

$(".exclude .custome_radio").addClass("red_radio");

$(".exclude .custome_radio").click(function(){

$(this).addClass("red_radio");

});

$(".other .custome_radio").click(function(){ 

	if($(this).parent().attr("id")==ZERO_PRIORITY_VALUE){ 
		
	//$(this).parent(".other").parent("tr").find(".other .custome_radio").removeClass("other");
	$(this).parent().removeClass( "other" ).addClass( "exclude" );
	$(this).addClass("red_radio");
	}else{

	$(this).parent(".other").parent("tr").find(".exclude .custome_radio").removeClass("red_radio");
	}
});


 

	
	$("#criteriaForm").validate();
	//$("#criteriaForm").removeAttr('novalidate');
	
	$(".setHelpText").mouseover(function(){
		
		var priorityValue=$(this).attr("id");
		 
		 $(".status_bar").html("Priority value "+priorityValue+" will consider.");
		 
		 $(".status_bar").show();
		 //$(".status_bar").fadeIn(50);
		});
		
	$(".setHelpText").mouseout(function(){
		$(".status_bar").html("");
		$(".status_bar").hide(); 
		//$(".status_bar").fadeOut(10000);
		});

	
	
	
	
});
