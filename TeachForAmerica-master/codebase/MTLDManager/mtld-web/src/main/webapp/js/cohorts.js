var loadingImage = ctx+'/images/ajax-loader-large.gif';
$(document).ready(function(){

	
	$('#criteria_screen').removeClass('selected');
	$('#mtld_info_screen').removeClass('selected');
	$('#matching_screen').addClass('selected');
	 //Start: Toggle MTLD and CM Left pane list	 
	 $('#cmmtldpicklist').change(function(){
		 $(".errorLabel").text("");
	        if(($("#cmmtldpicklist :selected").val())=='mtld')
	        {
	        	$('.cohorts_item').show();
	        	$('#mtldlist').show();
	        	$('#cmlist').hide();
	        	$('#unhiredlist').hide();
	        	$('#filterDiv').hide();
	        }
	 		else if(($("#cmmtldpicklist :selected").val())=='cm')
	 		{
	 			$('.cohorts_item').show();
	 			$('#cmlist').show();
	 			$('#mtldlist').hide();
	 			$('#unhiredlist').hide();
	 			$('#filterDiv').hide();
	 		}
	 		else
	 		{
	 			$('.cohorts_item').show();
	 			$('#unhiredlist').show();
	 			$('#mtldlist').hide();
	 			$('#cmlist').hide();
	 			$('#filterDiv').show();
	 		}
		 
	 });
	 //End: Toggle MTLD and CM Left pane list
	 
	//Start: filter unhired left pane list
		$('#filter').change(function(){
			$(".errorLabel").text("");
			$('.cohorts_item').show();
			var myList=0;
			var selectedValue= $('#filter :selected').text().split(" ").join("");
			var cmYearClass=null;
			
			if(selectedValue=='Corpsyear-1CM')
			{
				selectedValue='Corpsyear';
				cmYearClass='1 Year CM';
			}
			if(selectedValue=='Corpsyear-2CM')
			{
				selectedValue='Corpsyear';
				cmYearClass='2 Year CM';
			}
			myList = $('#unhiredlist').children('ul').children('div').children('div.mCSB_container');
			var listItems = myList.children('li');
			listItems.each(function(idx, li){
				if(!(($(li).find('td:nth-child(2)').find('.'+selectedValue).val())==cmYearClass))
				{
					$(li).hide();	
				}
				});
		});
	 //End: filter unhired left pane list
	 
	//Start: copy CM to the cohort
		$(document).on('click', 'a.copy_cohorts', function (e) {
			$(".errorLabel").text("");
			var listType = $("#cmmtldpicklist :selected").val();
	        var id= $(this).attr('id').split('_');
	        var cohort = $('#group_detail').children().attr('class');
	        if(typeof cohort === "undefined")
	        {
	        	$(".errorLabel").text("No target cohort found");
	        	$(window).scrollTop(0);
	        }
	        else
	        {
		        var cohortId = cohort.split('_'); 
		        var url=ctx+'/addCorpMember'+'/'+listType+'/'+id[2]+'/'+cohortId[2];
		        var selectedValue = $('#cmmtldpicklist option:selected').text().split(' ');
				if($('.isFinalCohort_'+cohortId[2]).val()=="true")
				{
					$(".errorLabel").text("Target cohort is already Locked. Please unlock to continue.");
					$(window).scrollTop(0);
				}
				else
				{
					$.blockUI({ 
				        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
			        $.getJSON(url, function(resp) {
	
			        	if(resp.status=="fail")
			        	{
			        		$(".errorLabel").text(resp.message);
			        		$(window).scrollTop(0);
			        		$.unblockUI();
			        	}
			        	else
			        	{
			        		var cohort= resp.data;
			        		listId = 'li_cm_'+id[2];
			        		//$("#"+listId).clone().appendTo("#group_detail ul.cohorts_list .mCSB_container");
							//$(this).closest(".cohorts_item").clone().appendTo("#group_detail ul.cohorts_list .mCSB_container");
							//$(this).closest("li.cohorts_item").remove();
			        		$('.cohorts_list1 #'+listId).remove();
							$('#groupCMCount').text(parseInt($('#groupCMCount').text(),10)+1);
					 	    $('#cmmtldpicklist option:selected').text(parseInt(selectedValue[0])-1 + " " + selectedValue[1]);
					 	    createCohort(cohort);
							createCohortDetailsBlock(cohort);
							$.unblockUI(); 
			        	}
			        });
				}
	        }
		 });
	//End: copy CM to the cohort
		
	//Start: Remove cm from cohort
		 $(document).on('click', 'a.delete_cohorts', function (e) {
			 $(".errorLabel").text("");
			 var id= $(this).attr('id').split('_');
			 var isHired=id[3];
		     var cohortId = $('#group_detail').children().attr('class').split('_');
		     var url=ctx+'/removeCorpMember'+'/'+id[2]+'/'+cohortId[2];
		     var selectedValue ;
			 if($('.isFinalCohort_'+cohortId[2]).val()=="true")
			{
				$(".errorLabel").text("Target cohort is already Locked. Please unlock to continue.");
				$(window).scrollTop(0);
			}
			else
			{
				$.blockUI({ 
			        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
		        $.getJSON(url, function(resp) {
		        	if(resp.status=="fail")
		        	{
		        		$(".errorLabel").text(resp.message);
		        		$(window).scrollTop(0);
		        		$.unblockUI();
		        	}
		        	else
		        	{
		        		listId = 'li_cm_'+id[2];
		        		var cohort= resp.data;
		        		var listType;
		        		if(isHired=="true")
		        		{
		        			listType="cmlist";
		        			selectedValue = $('#cmmtldpicklist option').eq(1).text().split(' ');
		        			$('#cmmtldpicklist option').eq(1).text(parseInt(selectedValue[0])+1 + " " + selectedValue[1]);
		        		}
		        		else
		        		{
		        			listType="unhiredlist";
		        			selectedValue = $('#cmmtldpicklist option').eq(2).text().split(' ');
		        			$('#cmmtldpicklist option').eq(2).text(parseInt(selectedValue[0])+1 + " " + selectedValue[1]);
		        		}
		        		$('#redDot_'+id[2]).hide();
		        		$("#"+listId).clone().appendTo("#cohorts_div #"+listType+" ul.cohorts_list .mCustomScrollBox .mCSB_container");		 	       
						$("#groupCMCount").text(parseInt($("#groupCMCount").text(),10)-1);
			 	       	createCohort(cohort);
						createCohortDetailsBlock(cohort);
						$.unblockUI();
		        	}
		        }); 
			}
	    });
	//End:  Remove cm from cohort
		 
	//Start: Save cohort
		 $(document).on('click','.save_btn',function(){
			 $(".errorLabel").text("");
			 var idString= $(this).attr('id').split('_');
			 url=ctx+'/saveUpdatedCohort/'+idString[2];
			 $.blockUI({ 
			        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
			 $.getJSON(url, function(resp) {

		        	if(resp.status=="fail")
		        	{
		        		$(".errorLabel").text(resp.message);
		        		$(window).scrollTop(0);
		        		$.unblockUI();
		        	}
		        	else
		        	{
		        		$('#cohort_save_'+idString[2]).hide();
		        		$('#cohort_unlock_'+idString[2]).show();
		        		$('.isFinalCohort_'+idString[2]).val(true);
		        		$.unblockUI();
		        	}
		        }); 
		 });
		 
	//End:  Save Cohort
	
	//Start: Unlock cohort
		 $(document).on('click','.unlock_btn',function(){
			 $(".errorLabel").text("");
			 var idString= $(this).attr('id').split('_');
			 url=ctx+'/unlockCohort/'+idString[2];
			 $.blockUI({ 
			        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
			 $.getJSON(url, function(resp) {

		        	if(resp.status=="fail")
		        	{
		        		$(".errorLabel").text(resp.message);
		        		$(window).scrollTop(0);
		        		$.unblockUI();
		        	}
		        	else
		        	{
		        		$('#cohort_save_'+idString[2]).show();
		        		$('#cohort_unlock_'+idString[2]).hide();
		        		$('.isFinalCohort_'+idString[2]).val(false);
		        		$.unblockUI();
		        	}
		        }); 
		 });
		 
	//End:  Unlock Cohort
	//Start: Show Cohort Details
		 $(document).on('click','.showCohortDetails',function(){
			 $(".errorLabel").text("");
			 var elementId= $(this).attr('id').split('_');
			 var corpMTLDId= elementId[2];
			 var cohortId=  elementId[3];
			 var url = ctx+'/getCohortDetails/'+cohortId;
			 $.blockUI({ 
			        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
			 $.getJSON(url, function(resp) {
				 	if(resp.status=="fail")
				 	{
				 		$(".errorLabel").text(resp.message);
				 		$(window).scrollTop(0);
				 		$.unblockUI();
				 	}
				 	else
				 	{
				 		$('#'+$('.each_group_selected').attr('id')).removeClass('each_group_selected');
				 		$('#each_group_'+corpMTLDId+'_'+cohortId).addClass('each_group_selected');
				 		var cohortObject = resp.data;
				 		createCohortDetailsBlock(cohortObject);
				 	// Code Added by Arun
				 		if(cohortObject != null && cohortObject != 'undefined'){
				 			createGoogleMap(cohortObject);
				 		}
				 		// Arun Ends
				 		$.unblockUI();
				 	}
			 	}); 
		 });
		 
		//End: Show Cohort Details
		 
		 /*Popup Code.Start  */
		 
		 $(document).on('click', '.open_mtld', function (e) {
			 	$(".errorLabel").text("");
			 	var fieldId=$(this).attr("id");
			  
			 	fieldId=fieldId.substr(fieldId.indexOf("_")+1 ,fieldId.length);
			 //	fieldId=fieldId.substr(0,fieldId.indexOf("_"));
			  
			 $("body").css("overflow", "hidden");
			 $(".body_overlay").show();
			 $(".popup_mtld").show();
			 
			
			 $(".user_img_big").html($("#mtld_first_name_char_"+fieldId).val()+$("#mtld_last_name_char_"+fieldId).val());
			 $("#mtld_cmo_affiliation_"+fieldId).val().length==0 || $("#mtld_cmo_affiliation_"+fieldId).val()=="null"?$(".mtld_cmo_affiliation").html(NO_RECORD_FOUND)+$(".mtld_cmo_affiliation").css("color","gray"):$(".mtld_cmo_affiliation").css("color","black")+$(".mtld_cmo_affiliation").html($("#mtld_cmo_affiliation_"+fieldId).val());
				$("#mtld_name_"+fieldId).val().length==0 || $("#mtld_name_"+fieldId).val()=="null"?$(".mtld_name").html(NO_RECORD_FOUND)+$(".mtld_name").css("color","gray"):$(".mtld_name").css("color","black")+$(".mtld_name").html($("#mtld_name_"+fieldId).val());
				$("#mtld_tenureRole_"+fieldId).val().length==0 || $("#mtld_tenureRole_"+fieldId).val()=="null"?$(".mtld_tenureRole").html(NO_RECORD_FOUND)+$(".mtld_tenureRole").css("color","gray"):$(".mtld_tenureRole").css("color","black")+$(".mtld_tenureRole").html($("#mtld_tenureRole_"+fieldId).val());
				$("#mtld_alum_"+fieldId).val().length==0 || $("#mtld_alum_"+fieldId).val()=="null"?$(".mtld_alum").html(NO_RECORD_FOUND)+$(".mtld_alum").css("color","gray"):$(".mtld_alum").css("color","black")+$(".mtld_alum").html($("#mtld_alum_"+fieldId).val()=="true"?"Y":$("#mtld_alum_"+fieldId).val()=="false"?"N":"");
				$("#mtld_subject_taught_"+fieldId).val().length==0 || $("#mtld_subject_taught_"+fieldId).val()=="null"?$(".mtld_subject_taught").html(NO_RECORD_FOUND)+$(".mtld_subject_taught").css("color","gray"):$(".mtld_subject_taught").css("color","black")+$(".mtld_subject_taught").html($("#mtld_subject_taught_"+fieldId).val());
				$("#mtld_corps_region_"+fieldId).val().length==0 || $("#mtld_corps_region_"+fieldId).val()=="null"?$(".mtld_corps_region").html(NO_RECORD_FOUND)+$(".mtld_corps_region").css("color","gray"):$(".mtld_corps_region").css("color","black")+$(".mtld_corps_region").html($("#mtld_corps_region_"+fieldId).val());
				$("#mtld_corps_district_"+fieldId).val().length==0 || $("#mtld_corps_district_"+fieldId).val()=="null"?$(".mtld_corps_district").html(NO_RECORD_FOUND)+$(".mtld_corps_district").css("color","gray"):$(".mtld_corps_district").css("color","black")+$(".mtld_corps_district").html($("#mtld_corps_district_"+fieldId).val());
				$("#mtld_person_color_"+fieldId).val().length==0 || $("#mtld_person_color_"+fieldId).val()=="null"?$(".mtld_person_color").html(NO_RECORD_FOUND)+$(".mtld_person_color").css("color","gray"):$(".mtld_person_color").css("color","black")+$(".mtld_person_color").html($("#mtld_person_color_"+fieldId).val()=="true"?"Y":$("#mtld_person_color_"+fieldId).val()=="false"?"N":"");
				$("#mtld_low_income_background_"+fieldId).val().length==0 || $("#mtld_low_income_background_"+fieldId).val()=="null"?$(".mtld_low_income_background").html(NO_RECORD_FOUND)+$(".mtld_low_income_background").css("color","gray"):$(".mtld_low_income_background").css("color","black")+$(".mtld_low_income_background").html($("#mtld_low_income_background_"+fieldId).val()=="true"?"Y":$("#mtld_low_income_background_"+fieldId).val()=="false"?"N":"");
				$("#mtld_neighborhood_"+fieldId).val().length==0 || $("#mtld_neighborhood_"+fieldId).val()=="null"?$(".mtld_neighborhood").html(NO_RECORD_FOUND)+$(".mtld_neighborhood").css("color","gray"):$(".mtld_neighborhood").css("color","black")+$(".mtld_neighborhood").html($("#mtld_neighborhood_"+fieldId).val());
				
			 
			 var docuemnt_height = $(document).width();
			 var window_width = $(window).width();
			 var popup_width= $(".popup_mtld").width();
			 var popup_position_left = (window_width - popup_width) / 2;
			 $(".popup_mtld").css("left" , popup_position_left );
			 
			 $(".body_overlay").css("height", docuemnt_height);
		});
		
		$(document).on('click', '.popup_mtld_close', function (e) {
			$(".errorLabel").text("");
	         $("body").css("overflow", "auto");
			 $(".body_overlay").hide();
			 $(".popup_mtld").hide();
	    });
		
		
		

		  $(document).on('click', '.open_corp', function (e) {
		 
			  $(".errorLabel").text("");
			  var fieldId=$(this).attr("id");
			  
			  fieldId=fieldId.substr(fieldId.indexOf("_")+1 ,fieldId.length);
			
			  
			 // fieldId=fieldId.substr(0,fieldId.indexOf("_"));
			  
			 $("body").css("overflow", "hidden");
			 $(".body_overlay").show();
			 $(".popup_corp").show();
			 
			
			  $(".user_img_big").html($("#cm_first_name_char_"+fieldId).val()+$("#cm_last_name_char_"+fieldId).val());
			  $("#cm_name_"+fieldId).val().length==0 || $("#cm_name_"+fieldId).val()=="null"?$(".cm_name").html(NO_RECORD_FOUND)+$(".cm_name").css("color","gray"):$(".cm_name").css("color","black")+$(".cm_name").html($("#cm_name_"+fieldId).val());
			  $("#cm_corps_year_"+fieldId).val().length==0 || $("#cm_corps_year_"+fieldId).val()=="null"?$(".cm_corps_year").html(NO_RECORD_FOUND)+$(".cm_corps_year").css("color","gray"):$(".cm_corps_year").css("color","black")+$(".cm_corps_year").html($("#cm_corps_year_"+fieldId).val());
			  $("#cm_grade_level_"+fieldId).val().length==0 || $("#cm_grade_level_"+fieldId).val()=="null"?$(".cm_grade_level").html(NO_RECORD_FOUND)+$(".cm_grade_level").css("color","gray"):$(".cm_grade_level").css("color","black")+$(".cm_grade_level").html($("#cm_grade_level_"+fieldId).val());
			  $("#cm_primary_subject_"+fieldId).val().length==0 || $("#cm_primary_subject_"+fieldId).val()=="null"?$(".cm_primary_subject").html(NO_RECORD_FOUND)+$(".cm_primary_subject").css("color","gray"):$(".cm_primary_subject").css("color","black")+$(".cm_primary_subject").html($("#cm_primary_subject_"+fieldId).val());
			  $("#cm_subject_modifier_"+fieldId).val().length==0 || $("#cm_subject_modifier_"+fieldId).val()=="null"?$(".cm_subject_modifier").html(NO_RECORD_FOUND)+$(".cm_subject_modifier").css("color","gray"):$(".cm_subject_modifier").css("color","black")+$(".cm_subject_modifier").html($("#cm_subject_modifier_"+fieldId).val());
			  $("#cm_school_name_"+fieldId).val().length==0 || $("#cm_school_name_"+fieldId).val()=="null"?$(".cm_school_name").html(NO_RECORD_FOUND)+$(".cm_school_name").css("color","gray"):$(".cm_school_name").css("color","black")+$(".cm_school_name").html($("#cm_school_name_"+fieldId).val());
			  $("#cm_school_neighborhood_"+fieldId).val().length==0 || $("#cm_school_neighborhood_"+fieldId).val()=="null"?$(".cm_school_neighborhood").html(NO_RECORD_FOUND)+$(".cm_school_neighborhood").css("color","gray"):$(".cm_school_neighborhood").css("color","black")+$(".cm_school_neighborhood").html($("#cm_school_neighborhood_"+fieldId).val());
			  
			  
			 
			 var docuemnt_height = $(document).width();
			 var window_width = $(window).width();
			 var popup_width= $(".popup_corp").width();
			 var popup_position_left = (window_width - popup_width) / 2;
			 $(".popup_corp").css("left" , popup_position_left );
			 
			 $(".body_overlay").css("height", docuemnt_height);
		});
		
		$(document).on('click', '.popup_corp_close', function (e) {
			$(".errorLabel").text("");
	         $("body").css("overflow", "auto");
			 $(".body_overlay").hide();
			 $(".popup_corp").hide();
	    });
		
		

		$(document).on('click', '.group_popup_icon', function (e) {
		
			$(".errorLabel").text("");
			 $("body").css("overflow", "hidden");
			 $(".body_overlay").show();
			 $(".popup_group").show();
			
			var idString= $(this).attr('id');
			 url=ctx+'/getGroupDetails/'+idString;
			 $.getJSON(url, function(resp) {

		        	if(resp.status=="fail")
		        	{
		        		$(".errorLabel").text(resp.message);
		        		$(window).scrollTop(0);
		        	}
		        	else
		        		
		        	{
		        		
		        		 $(".school_disrict").html(resp.data.SchoolDistrict!=null && resp.data.SchoolDistrict!="null"?resp.data.SchoolDistrict:"");
		        		 $(".neighbourhood_represented").html(resp.data.NeighbourhoodRepresented!=null && resp.data.NeighbourhoodRepresented!="null"?resp.data.NeighbourhoodRepresented:"");
		        		 $(".sped_percentage").html(resp.data.SPEDPercentage+"%");
		        		 $(".ece_percentage").html(resp.data.EcePercentage+"%");
		        		 $(".one_year_corp_percentage").html(resp.data.OneYearCorpPercentage+"%");
		        		 $(".two_year_corp_percentage").html(resp.data.TwoYearCorpPercentage+"%");
		        		 $(".charter_partner_percentage").html(resp.data.CharterPartnerPercentage+"%");
		        		 $(".district_partner_percentage").html(resp.data.DistrictPartnerPercentage+"%");
		        		 $(".low_income_percentage").html(resp.data.LowIncomePercentage+"%");
		        		 $(".feeder_pattern_hs").html(resp.data.FeederPatternHS!=null && resp.data.FeederPatternHS!="null"?resp.data.FeederPatternHS:"");
		 
		        	}
		        });
			
			
			 
			 
			 var docuemnt_height = $(document).width();
			 var window_width = $(window).width();
			 var popup_width= $(".popup_group").width();
			 var popup_position_left = (window_width - popup_width) / 2;
			 $(".popup_group").css("left" , popup_position_left );
			 
			 $(".body_overlay").css("height", docuemnt_height);
		});
		
		
		$(document).on('click', '.popup_group_close', function (e) {
			$(".errorLabel").text("");
	         $("body").css("overflow", "auto");
			 $(".body_overlay").hide();
			 $(".popup_group").hide();
	    });
		
		
		
		
		/*Popup Code.End  */
		
		//Start:open progress bar popup
			$(document).on('click','.progress_popup_icon',function(){
				$(".errorLabel").text("");
				$('#criteria_progress_popup_div').empty();
				$("#progress_criteria .progress_bar_table").clone().appendTo("#criteria_progress_popup_div");
				$('#criteria_progress_popup_div .progress_bar_table').each(function(){
				    $(this).find('td.criteria_progress_row').each(function(){
				    	var id=$(this).attr('id').split('_');
				    	
				    	$(this).find('#criteria_progress_row_text_'+id[3]).text($(this).attr("alt"));
				    });
				});
				$('#criteria_progress_popup_div .progress_bar_table').find('.overall_progress_row').css('width','62%');
				$('#criteria_progress_popup_div .progress_bar_table').find('.overall_progress_row_value').css('width','38%');
				$("body").css("overflow", "hidden");
				$(".body_overlay").show();
				$(".popup_progress").show();
				 
				 
				var docuemnt_height = $(document).width();
				var window_width = $(window).width();
				var popup_width= $(".popup_progress").width();
				var popup_position_left = (window_width - popup_width) / 2;
				$(".popup_progress").css("left" , popup_position_left );
				 
				$(".body_overlay").css("height", docuemnt_height);

			});
		//End:open progress bar popup
		//Start:close progress bar popup
			$(document).on('click', '.popup_progress_close', function (e) {
				$(".errorLabel").text("");
		         $("body").css("overflow", "auto");
				 $(".body_overlay").hide();
				 $(".popup_progress").hide();
		    });
		//End:close progress bar popup
			
	//Start :Remove Seeded Member
			$(document).on('click','.delete_seeded',function(){
				$(".errorLabel").text("");
				var id=$(this).attr('id').split('_');
				var cohortId = $('#group_detail').children().attr('class').split('_');
				url=ctx+'/removeSeededMember/'+id[2]+'/'+id[3]+'/'+cohortId[2];
				
				var dropDownValue = $('#cmmtldpicklist option').eq(0).text().split(' ');
				if($('.isFinalCohort_'+cohortId[2]).val()=="true")
				{
					$(".errorLabel").text("Target cohort is already Locked. Please unlock to continue.");
					$(window).scrollTop(0);
				}
				else
				{
					$.blockUI({ 
					        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
					 $.getJSON(url, function(resp) {
						 if(resp.status=="fail")
						 {
							 $(".errorLabel").text(resp.message);
							 $(window).scrollTop(0);
							 $.unblockUI(); 
						 }
						 else
						 {
							 var cohort=resp.data;
							 listId = 'li_seededMember_MTLD_'+id[3];
							 $("#"+listId).clone().appendTo("#cohorts_div #mtldlist ul.cohorts_list .mCustomScrollBox .mCSB_container");
							 $("#group_detail .cohorts_list #"+listId).remove();
							 $('#cmmtldpicklist option').eq(0).text(parseInt(dropDownValue[0])+1 + " " + dropDownValue[1]);
							 createCohort(cohort);
							 createCohortDetailsBlock(cohort);
							 $.unblockUI(); 
						 }
					 });
				}
				 
					/*$(".scrollbar_box").mCustomScrollbar({
						scrollButtons:{
							enable:true
						}
						
					});*/
			});
	//End :Remove Seeded Member
			
	//Start :Add Seeded Member
			$(document).on('click','.copy_seeded',function(){
				$(".errorLabel").text("");
				var id=$(this).attr('id').split('_');
				var cohort = $('#group_detail').children().attr('class');
				if(typeof cohort === "undefined")
			    {
			       	$(".errorLabel").text("No target cohort found");
			       	$(window).scrollTop(0);
			    }
				else
				{
					var cohortId = cohort.split('_');
					url=ctx+'/addSeededMember/'+id[2]+'/'+cohortId[2];
					var dropDownValue = $('#cmmtldpicklist option').eq(0).text().split(' ');
					if($('.isFinalCohort_'+cohortId[2]).val()=="true")
					{
						$(".errorLabel").text("Target cohort is already Locked. Please unlock to continue.");
						$(window).scrollTop(0);
					}
					else
					{
						$.blockUI({ 
						        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });
						 $.getJSON(url, function(resp) {
							 if(resp.status=="fail")
							 {
								 $(".errorLabel").text(resp.message);
								 $(window).scrollTop(0);
								 $.unblockUI(); 
							 }
							 else
							 {
								 var cohort=resp.data;
								 listId = 'li_seededMember_MTLD_'+id[2];
								 $('.cohorts_list1 #'+listId).remove();
								 $('#cmmtldpicklist option').eq(0).text(parseInt(dropDownValue[0])-1 + " " + dropDownValue[1]);
								 createCohort(cohort);
								 createCohortDetailsBlock(cohort);
								 $.unblockUI(); 
							 }
						 });
					}
				}
			});
	//End :Add Seeded Member
	
			bindHelpTextEvent();
			
			/*$(".scrollbar_box").mCustomScrollbar({
				scrollButtons:{
					enable:true
				}
				
			});*/
			
			var cohortID = $('#defaultCohortId').val();
			if(cohortID != null && cohortID != 'undefined'){
				getGoogleMapDetails(cohortID);	
			}
});

function getGoogleMapDetails(cohortID){
	 
	 var cohortId=  cohortID;
	 var url = ctx+'/getCohortDetails/'+cohortId;
	/* $.blockUI({ 
	        message: '<h1><img src='+loadingImage+' class="ajax-loader-large" /></h1>'         });*/
	 $.getJSON(url, function(resp) {
		 	if(resp.status=="fail")
		 	{
		 		$(".errorLabel").text(resp.message);
		 		//$.unblockUI();
		 	}
		 	else
		 	{  
		 		var cohortObject = resp.data;
		 		if(cohortObject != null && cohortObject != 'undefined'){
		 		createGoogleMap(cohortObject);
		 		}
		 		//$.unblockUI();
		 	}
	 	}); 
}

//Arun Starts for google Map
///Function to create google map div dynamically
function createGoogleMap(cohortObject) {
	if(cohortObject != null && cohortObject != 'undefined'){
			initialize(cohortObject);
	}
		}

		var markers = new Array();
		var map;
		var infowindow = new Array();

		function initialize(cohortObject) {
			var midMap;
			var centerPin;
			if(cohortObject != null && cohortObject.schoolBeans.length != 'undefined'){
				if(cohortObject.schoolBeans.length > 0){
					midMap = cohortObject.schoolBeans.length / 2;
					centerPin = Math.floor(midMap);
				}else{
					centerPin =0 ;
				}
			}
			var mapCenter;
			if(cohortObject != null && cohortObject.schoolBeans.length > 0 && cohortObject.schoolBeans[centerPin].latitude !=null 
					&& cohortObject.schoolBeans[centerPin].longitude!=null
					&& cohortObject.schoolBeans[centerPin].address !=null
					&& cohortObject.schoolBeans[centerPin].schoolName != null){
			  mapCenter= new google.maps.LatLng(
						cohortObject.schoolBeans[centerPin].latitude,
						cohortObject.schoolBeans[centerPin].longitude);
		    }else{ 
			 mapCenter= new google.maps.LatLng(40.7141667,-74.0063889);
			}
			// End If 
			var mapProp = {
				center : mapCenter,
				zoom : 8,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};

			map = new google.maps.Map(document
					.getElementById("googleMap"), mapProp);
			drowMapPins(cohortObject);

		}

		function drowMapPins(cohortObject) {
			if(cohortObject != null && cohortObject.schoolBeans.length != 'undefined'){
			for ( var i = 0; i < cohortObject.schoolBeans.length; i++) {
				// information window to disply on google map
				if (cohortObject.schoolBeans[i].mtldBean != null && cohortObject.schoolBeans[i].schoolName !=null && cohortObject.schoolBeans[i].address!= null
						&& cohortObject.schoolBeans[i].mtldBean.firstName !=null) { 
					infowindow
							.push(new google.maps.InfoWindow(
									{
										content :'<B> School Name : </B>'
												+ cohortObject.schoolBeans[i].schoolName
												+ '<br>'
												+ '<B>School Address: </B>'
												+ cohortObject.schoolBeans[i].address
												+ '<br>'
												+ '<B>MTLD : </B>'
												+ cohortObject.schoolBeans[i].mtldBean.firstName +' '+cohortObject.schoolBeans[i].mtldBean.lastName
												+ '<br>'
												+ '<B>No of CMs :</B> '
												+ cohortObject.schoolBeans[i].cmCount
									}));
				} else { 

					var cmCount = cohortObject.schoolBeans[i].cmCount;
					infowindow
							.push(new google.maps.InfoWindow(
									{
										content : '<B>School Name :</B>'
												+ cohortObject.schoolBeans[i].schoolName
												+ '<br>'
												+ '<B>School Address: </B>'
												+ cohortObject.schoolBeans[i].address
												
												+ '<br>'
												+ '<B> No of CMs : </B>'
												+ cmCount
									}));
				    }
			}//for loop ends for information window.
			}//End if

			if(cohortObject != null && cohortObject.schoolBeans.length != 'undefined'){
			for ( var i = 0; i < cohortObject.schoolBeans.length; i++) {
				// code to show pin points
				if(cohortObject != null && cohortObject.schoolBeans[i].latitude != null 
						&& cohortObject.schoolBeans[i].latitude != 0
						&& cohortObject.schoolBeans[i].longitude != null
						&& cohortObject.schoolBeans[i].longitude != 0 
						){ 
				markers.push(new google.maps.Marker({
					position : new google.maps.LatLng(
							cohortObject.schoolBeans[i].latitude,
							cohortObject.schoolBeans[i].longitude),
					title : 'Teach For America',
					map : map,
				}));
				}else{
					markers.push(new google.maps.Marker({
						position : new google.maps.LatLng(
								40.7141667,
								-74.0063889),
						title : 'Teach For America',
						map : map,
					}));
				}
			}// For loop ends for pin generation
		}//End IF

			for ( var y = 0; y < markers.length; y++) {
				addContantToPins(markers[y], infowindow[y]);
			}
			function addContantToPins(markers, infowindow) {
				google.maps.event.addListener(markers, 'click',
						function() {
							infowindow.open(map, markers);
						});
			}
		}
//End: Show Google Map
//Arun Ends for google Map

function bindHelpTextEvent(){
	
	
	$(".setHelpText").mouseover(function(){
		
		
		var name=$(this).attr("name");
		
		if(name=="expandCohort"){
			 $(".status_bar").html("Click to view this Cohort in expanded view");
			 
			 $(".status_bar").show();
			
		}if(name=="Corp Member" || name=="MTLD"  || name=="Cohort/Group" ){
		 
		 $(".status_bar").html("Click to view this "+name+ " details");
		 
		 $(".status_bar").show();
		 //$(".status_bar").fadeIn(50);
		}
		});
		
	$(".setHelpText").mouseout(function(){
		$(".status_bar").html("");
		$(".status_bar").hide(); 
		//$(".status_bar").fadeOut(10000);
		});
	
	
}

//Function to create cohortDetails div dynamically
function createCohortDetailsBlock(cohortObject) {
	var id;
	var name;
	var fNameCharacter;
	var lNameCharacter;
	var cmMTLDHtml;
	var groupSize;
	var isFinalCohort=false;
	var tenureRole='';
	var schoolsRep;
	var overDistanceImage='';
	var seededMtldHtml='';

	if(cohortObject.isFinalCohort==true)
	{
		isFinalCohort=true;
	}
	if(cohortObject.maxDistance!=null && cohortObject.overDistance==true)
	{
		overDistanceImage='<strong>'+cohortObject.maxDistance+'</strong> Miles apart (max)<br><img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" >';
	}
	else if(cohortObject.maxDistance!=null)
	{
		overDistanceImage='<strong>'+cohortObject.maxDistance+'</strong> Miles apart (max)<br>';
	}
	if(cohortObject.schoolRep=='1')
	{
		schoolRep = '<strong class="schools_represented_'+cohortObject.id+'">'+cohortObject.schoolRep+'</strong> School Represented <br>';
	}
	else
	{
		schoolRep = '<strong class="schools_represented_'+cohortObject.id+'">'+cohortObject.schoolRep+'</strong> Schools Represented <br>';
	}
	if(cohortObject.seededCMBean!=null)
	{
		id=cohortObject.seededCMBean.id;
		name=cohortObject.seededCMBean.firstName+' '+cohortObject.seededCMBean.lastName;
		fNameCharacter=(cohortObject.seededCMBean.firstName.substring(0, 1)).toUpperCase();
		lNameCharacter=(cohortObject.seededCMBean.lastName.substring(0, 1)).toUpperCase();
		var schoolBeanHiddenFields='';
		if(cohortObject.seededCMBean.schoolBean!=null) {
			schoolBeanHiddenFields=	 '<input type="hidden" id="cm_school_name_'+id+'" value="'+cohortObject.seededCMBean.schoolBean.schoolName+'"/>' +
	         '<input type="hidden" id="cm_school_neighborhood_'+id+'" value="'+cohortObject.seededCMBean.schoolBean.neighborhood+'"/>';
		}
		else
		{
			schoolBeanHiddenFields=	        '<input type="hidden" id="cm_school_name_'+id+'" value=""/>' +
	         '<input type="hidden" id="cm_school_neighborhood_'+id+'" value=""/>';
		}
		
		tenureRole= cohortObject.seededCMBean.cmYear;
		cmMTLDHtml='<tr><td width="75%" align="left" valign="bottom">' +
		'<a href="javascript:void(0);" class=""  id="cm_'+id+'">Group Info</a>' +
		'<input type="hidden" id="cm_first_name_char_'+id+'"  value="'+fNameCharacter+'" />'+
		'<input type="hidden" id="cm_last_name_char_'+id+'"   value="'+lNameCharacter+'" />'+
		'<input type="hidden" id="cm_name_'+id+'" value="'+name+'" />' +
	    '<input type="hidden" id="cm_corps_year_'+id+'" value="'+cohortObject.seededCMBean.cmYear+'" />' +
	     '<input type="hidden" id="cm_grade_level_'+id+'" value="'+cohortObject.seededCMBean.gradeLevel+'" />' +
	      '<input type="hidden" id="cm_primary_subject_'+id+'" value="'+cohortObject.seededCMBean.subjectGroup+'" />' +
	       '<input type="hidden" id="cm_subject_modifier_'+id+'" value="'+cohortObject.seededCMBean.subjectModifier+'"/>' +schoolBeanHiddenFields+
	       '</td>' +
	         '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id='+cohortObject.id+' " class="group_popup_icon setHelpText" name="Cohort/Group"><img src="images/popup_icon.png" width="20" height="18"></a></td>' +
		      '</tr>' +
			   	'<tr>' +
		          '<td colspan="2" align="left" valign="top">' +schoolRep
		           +overDistanceImage+
		          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">' +
			          ' <tr>' +
			      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
			     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
			    	'%</span></td>' +
			        '</tr>' +
			        '<tr>' +
			        '<td align="left" valign="middle">Content</td>' +
			        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
			        '</tr>' +
			        '<tr>' +
			        '<td align="left" valign="middle">Geography</td>' +
			        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
			        '</tr>' +
			        '<tr>' +
			        '<td align="left" valign="middle">Relationships</td>' +
			        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
					  '</tr>' +
			  '</table>' +
		      '</td></tr>';
		
	}
	else if(cohortObject.seededMtldBean!=null)
	{
		id=cohortObject.seededMtldBean.id;
		if(cohortObject.seededMtldBean.tenureRole!=null)
		{
			if(cohortObject.seededMtldBean.tenureRole>1)
			{
				tenureRole= cohortObject.seededMtldBean.tenureRole.toString()+' Years MTLD';
			}
			else
			{
				tenureRole= cohortObject.seededMtldBean.tenureRole.toString()+' Year MTLD';
			}
		}
		name=cohortObject.seededMtldBean.firstName+' '+cohortObject.seededMtldBean.lastName;
		fNameCharacter=(cohortObject.seededMtldBean.firstName.substring(0, 1)).toUpperCase();
		lNameCharacter=(cohortObject.seededMtldBean.lastName.substring(0, 1)).toUpperCase();
		cmMTLDHtml='<tr><td width="75%" align="left" valign="bottom">' +
		'<a href="javascript:void(0);" class=""  id="mtld_'+id+'">Group Info</a>' + 
		'<input type="hidden" id="mtld_first_name_char_'+id+'"  value="'+fNameCharacter+'" />'+
		'<input type="hidden" id="mtld_last_name_char_'+id+'"   value="'+lNameCharacter+'" />'+
		'<input type="hidden" id="mtld_name_'+id+'"  value="'+name+'" />'+
	    '<input type="hidden" id="mtld_tenureRole_'+id+'"  value="'+cohortObject.seededMtldBean.tenureRole+'" />'+
	     '<input type="hidden" id="mtld_alum_'+id+'"  value="'+cohortObject.seededMtldBean.alum+'" />' +
	     '<input type="hidden" id="mtld_subject_taught_'+id+'"  value="'+cohortObject.seededMtldBean.subjectTaught+'" />' +
	     '<input type="hidden" id="mtld_corps_region_'+id+'"  value="'+cohortObject.seededMtldBean.corpsRegionName+'" />' +
	     '<input type="hidden" id="mtld_corps_district_'+id+'"  value="'+cohortObject.seededMtldBean.corpsSchoolDistrict+'" />' +
	     '<input type="hidden" id="mtld_person_color_'+id+'"  value="'+cohortObject.seededMtldBean.personColor+'" />' +
	     '<input type="hidden" id="mtld_low_income_background_'+id+'"  value="'+cohortObject.seededMtldBean.lowIncomeBackground+'" />' +
	     '<input type="hidden" id="mtld_neighborhood_'+id+'"  value="'+cohortObject.seededMtldBean.neighborhood+'" /><td>'+
	     '<input type="hidden" id="mtld_cmo_affiliation_'+id+'"  value="'+cohortObject.seededMtldBean.cmoAffiliation+'" />'+
	     '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id='+cohortObject.id+' " class="group_popup_icon setHelpText" name="Cohort/Group"><img src="images/popup_icon.png" width="20" height="18"></a></td></tr>' +
	          '<tr><td colspan="2" align="left" valign="top">' +schoolRep+overDistanceImage+
	          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">' +
		          '<tr>' +
		      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
		     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
		    	'%</span></td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Content</td>' +
		        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Geography</td>' +
		        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Relationships</td>' +
		        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
				  '</tr>' +
		  '</table>' +
	      '</td></tr>';
		
		seededMtldHtml= '<li class="cohorts_item" id="li_seededMember_MTLD_'+id+'">'+
		'<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
			'<tr>'+
				'<td><a href="javascript:void();" class="open_mtld open_status setHelpText" name="MTLD" id="MTLD_'+id+'">'+name+'</a> '+tenureRole+'</td>'+
				'<td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" class="icon_btn copy_seeded" id="move_MTLD_'+id+'"><img src="images/icon_drag.png" width="20" height="17"></a></td>'+
				'<td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_seeded" id="delete_seededMember_MTLD_'+id+'"><img src="images/icon_delete.png" width="18" height="18"></a></td>'+
		  '</tr>'+
		'</table>'+
		'</li>'
	}
	else
	{
		tenureRole="";
		cmMTLDHtml='<tr><td width="75%" align="left" valign="bottom">' +
		'<a href="javascript:void(0);" class="" id="mtld_none">Group Info</a>' + 
		'<input type="hidden" id="mtld_first_name_char_none"  value="'+fNameCharacter+'" />'+
		'<input type="hidden" id="mtld_last_name_char_none"   value="'+lNameCharacter+'" />'+
	     '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id='+cohortObject.id+' " class="group_popup_icon"></a></td></tr>' +
	          '<tr><td colspan="2" align="left" valign="top">' +schoolRep+
	          overDistanceImage+
	          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">' +
		          ' <tr>' +
		      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
		     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
		    	'%</span></td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Content</td>' +
		        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Geography</td>' +
		        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
		        '</tr>' +
		        '<tr>' +
		        '<td align="left" valign="middle">Relationships</td>' +
		        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
				  '</tr> ' +
		  '</table>' +
	      '</td></tr>';
		
	}
	if(cohortObject.cohortDetailBean.length==1)
	{
		groupSize= cohortObject.cohortDetailBean.length+' Corp Member';
	}
	else
	{
		groupSize= cohortObject.cohortDetailBean.length+' Corps Members';
	}
	var html='<div id="group_detail_heading" class="heading_'+id+'_'+cohortObject.id+'">' +
	'<input type="hidden" class="isFinalCohort_'+cohortObject.id+'" value="'+isFinalCohort+'"/>' +
	'<table width="100%" border="0" cellspacing="0" cellpadding="0" id="cohorts_detail_'+id+'_'+cohortObject.id+'">' 
	+ cmMTLDHtml + 
 	'<tr></tr>' +
  '</table>' +
  '</div>' +
	'<div class="group_detail_counting">' +
    '<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
	 '<tr>' +
	 '<td width="90%"><span class="cmcount_'+cohortObject.id+'">'+groupSize+'</span>'+
	 '</td>' +
	    '<td width="10%">&nbsp;</td>' +
	  '</tr>' +
	'</table>' +
    '</div>' +
    '<ul class="cohorts_list scrollbar_box">'+ seededMtldHtml;
	for(var i=0; i<cohortObject.cohortDetailBean.length;i++)
	{
		var isNotFitForCohortImage='';
		var corpMemberId= cohortObject.cohortDetailBean[i].corpsMember.id;
		var corpMemberName = cohortObject.cohortDetailBean[i].corpsMember.firstName+' '+cohortObject.cohortDetailBean[i].corpsMember.lastName;
		var fNameCharacter= (cohortObject.cohortDetailBean[i].corpsMember.firstName.substring(0, 1)).toUpperCase();
		var lNameCharacter= (cohortObject.cohortDetailBean[i].corpsMember.lastName.substring(0, 1)).toUpperCase();
		var schoolBeanHiddenFields='';
		var cmYear='';
		var gradeLevel='';
		var subjectGroup='';
		if(cohortObject.cohortDetailBean[i].corpsMember.cmYear!=null)
		{
			cmYear=cohortObject.cohortDetailBean[i].corpsMember.cmYear;
			if(cohortObject.cohortDetailBean[i].corpsMember.gradeLevel!=null || cohortObject.cohortDetailBean[i].corpsMember.subjectGroup !=null)
			{
				cmYear=cohortObject.cohortDetailBean[i].corpsMember.cmYear+', ';
			}
		}
		if(cohortObject.cohortDetailBean[i].corpsMember.gradeLevel!=null)
		{
			gradeLevel=cohortObject.cohortDetailBean[i].corpsMember.gradeLevel+' School';
			if(cohortObject.cohortDetailBean[i].corpsMember.subjectGroup !=null)
			{
				gradeLevel=cohortObject.cohortDetailBean[i].corpsMember.gradeLevel+' School,  ';
			}
		}
		if(cohortObject.cohortDetailBean[i].corpsMember.subjectGroup !=null)
		{
			subjectGroup = cohortObject.cohortDetailBean[i].corpsMember.subjectGroup;
		}
		if(cohortObject.cohortDetailBean[i].notFitForCohort == true)
		{
			isNotFitForCohortImage='<span id="redDot_'+corpMemberId+'"><img src="images/dot_red.png" title="check for fit" alt="check for fit" width="14" height="14"></span>';
		}
		if(cohortObject.cohortDetailBean[i].corpsMember.schoolBean!=null) {
			schoolBeanHiddenFields=	 '<input type="hidden" id="cm_school_name_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.schoolBean.schoolName+'"/>' +
	         '<input type="hidden" id="cm_school_neighborhood_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.schoolBean.neighborhood+'"/>';
		}
		else
		{
			schoolBeanHiddenFields=	        '<input type="hidden" id="cm_school_name_'+corpMemberId+'" value=""/>' +
	         '<input type="hidden" id="cm_school_neighborhood_'+corpMemberId+'" value=""/>';
		}
		var cmHTML= '<li class="cohorts_item" id="li_cm_'+corpMemberId+'"><table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			  '<tr>' +
		'<td><a class="open_corp setHelpText" name="Corp Member" id="cm_'+corpMemberId+'" href="javascript:void(0);">'+corpMemberName+'</a>'+cmYear+gradeLevel+subjectGroup + 
		'<input type="hidden" id="cm_first_name_char_'+corpMemberId+'"  value="'+fNameCharacter+'" />'+
		'<input type="hidden" id="cm_last_name_char_'+corpMemberId+'"   value="'+lNameCharacter+'" />'+
		'<input type="hidden" id="cm_name_'+corpMemberId+'" value="'+corpMemberName+'" />' + 
		 '<input type="hidden" id="cm_corps_year_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.cmYear+'" />' +
	     '<input type="hidden" id="cm_grade_level_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.gradeLevel+'" />' +
	      '<input type="hidden" id="cm_primary_subject_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.subjectGroup+'" />' +
	       '<input type="hidden" id="cm_subject_modifier_'+corpMemberId+'" value="'+cohortObject.cohortDetailBean[i].corpsMember.subjectModifier+'" />' +schoolBeanHiddenFields+isNotFitForCohortImage+
		'</td>' +
		 '<td>' +
		    '<input type="hidden" id="cmyear_'+corpMemberId+'" class="cmyear Corpsyear" value="'+cohortObject.cohortDetailBean[i].corpsMember.cmYear+'"/>' +
	    '</td> ' +
		'<td width="8%" align="left" valign="middle" class="copy_cohorts_td"><a href="javascript:void(0);" class="icon_btn copy_cohorts" id="move_cm_'+corpMemberId+'"><img src="images/icon_drag.png" width="20" height="17"></a></td>' +
		'<td width="8%" align="right" valign="middle" class="delete_cohorts_td"><a href="javascript:void(0);" class="icon_btn delete_cohorts" id="delete_cm_'+corpMemberId+'_'+cohortObject.cohortDetailBean[i].corpsMember.isHired+'"><img src="images/icon_delete.png" width="18" height="18"></a></td>' +
	  '</tr>' +
	'</table>' +
	'</li>';
		html=html+cmHTML;
	}
	html = html+'</ul><div class="clear"></div></div>';
	$('#group_detail').empty();
	$('#group_detail').append(html);
	
	$("#group_detail .scrollbar_box").mCustomScrollbar({
		scrollButtons:{
			enable:true
		}
		
	});
	bindHelpTextEvent();
}

//Function to create cohortDetails div dynamically
function createCohort(cohortObject){
	var seededMemberId;
	var cmMtldHtml;
	var tenureRole='';
	var fNameCharacter;
	var lNameCharacter;
	var name;
	var schoolRep;
	var groupSize;
	var overDistanceImage='';
	if(cohortObject.schoolRep=='1')
	{
		schoolRep = '<strong class="schools_represented_'+cohortObject.id+'">'+cohortObject.schoolRep+'</strong> School Represented <br>';
	}
	else
	{
		schoolRep = '<strong class="schools_represented_'+cohortObject.id+'">'+cohortObject.schoolRep+'</strong> Schools Represented <br>';
	}
	if(cohortObject.maxDistance!=null && cohortObject.overDistance==true)
	{
		overDistanceImage='<strong>'+cohortObject.maxDistance+'</strong> Miles apart (max)<br><img src="images/dot_yellow.png" title="Over distance limit" alt="Over distance limit" >';
	}
	else if(cohortObject.maxDistance!=null)
	{
		overDistanceImage='<strong>'+cohortObject.maxDistance+'</strong> Miles apart (max)<br>';
	}
	if(cohortObject.cohortDetailBean.length==1)
	{
		groupSize= cohortObject.cohortDetailBean.length+' Corp Member';
	}
	else
	{
		groupSize= cohortObject.cohortDetailBean.length+' Corps Members';
	}
	if(cohortObject.seededCMBean!=null)
	{
		if(cohortObject.seededCMBean.cmYear!=null)
		{	tenureRole= cohortObject.seededCMBean.cmYear; }
		name=cohortObject.seededCMBean.firstName+' '+cohortObject.seededCMBean.lastName;
		fNameCharacter=(cohortObject.seededCMBean.firstName.substring(0, 1)).toUpperCase();
		lNameCharacter=(cohortObject.seededCMBean.lastName.substring(0, 1)).toUpperCase();
		cmMtldHtml='<div class="each_group each_group_selected" id="each_group_'+cohortObject.seededCMBean.id+'_'+cohortObject.id+'">'+
		'<table width="100%" border="0" cellspacing="0" cellpadding="0" id="cohort_table_'+cohortObject.seededCMBean.id+'_'+cohortObject.id+'">'+
		'<tr>'+
	          '<td width="20%" rowspan="2" align="left" valign="top"><div class="user_img">'+fNameCharacter+lNameCharacter+'</div></td>'+
	          '<td width="75%" align="left" valign="bottom"><a href="javascript:void(0);" class="showCohortDetails" id="cohort_name_'+cohortObject.seededCMBean.id+'_'+cohortObject.id+'">'+name+'</a></td>'+
	          '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id="'+cohortObject.id+'" class="group_popup_icon"><img src="images/popup_icon.png" width="20" height="18"></a></td>'+
	   '</tr>'+
	  '<tr>'+
          '<td colspan="2" align="left" valign="top">'+tenureRole+'<br>'+ schoolRep + overDistanceImage +
          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">'+
          ' <tr>'+
      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
    	'%</span></td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Content</td>' +
        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Geography</td>' +
        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Relationships</td>' +
        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
	  '</tr> '+
	  '</table></td>'+
	  '</tr>'+
	  '</table>'+
	  '<div class="group_detail_counting">'+
	    '<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
		  '<tr>'+
		  '<td width="90%"><span class="cmcount_'+cohortObject.id+'">'+groupSize+'</span>'+
		    '</td>'+
		    '<td width="10%"><input name="" type="button" class="save_btn" value="save" id="cohort_save_'+cohortObject.id+'"></td>'+
		    '<td width="10%"><input name="" type="button" value="Unlock" class="unlock_btn" id="cohort_unlock_'+cohortObject.id+'" style="display:none;"></td>'+
		    '</tr>'+
		'</table>'+
		'</div>'+
	  	'<div class="clear"></div>'+
		'</div>';
	}
	else if(cohortObject.seededMtldBean!=null)
	{
		id=cohortObject.seededMtldBean.id;
		if(cohortObject.seededMtldBean.tenureRole!=null)
		{
			if(cohortObject.seededMtldBean.tenureRole>1)
			{
				tenureRole= cohortObject.seededMtldBean.tenureRole.toString()+' Years MTLD';
			}
			else
			{
				tenureRole= cohortObject.seededMtldBean.tenureRole.toString()+' Year MTLD';
			}
		}
		name=cohortObject.seededMtldBean.firstName+' '+cohortObject.seededMtldBean.lastName;
		fNameCharacter=(cohortObject.seededMtldBean.firstName.substring(0, 1)).toUpperCase();
		lNameCharacter=(cohortObject.seededMtldBean.lastName.substring(0, 1)).toUpperCase();
		cmMtldHtml='<div class="each_group each_group_selected" id="each_group_'+cohortObject.seededMtldBean.id+'_'+cohortObject.id+'">'+
		'<table width="100%" border="0" cellspacing="0" cellpadding="0" id="cohort_table_'+cohortObject.seededMtldBean.id+'_'+cohortObject.id+'">'+
		'<tr>'+
	          '<td width="20%" rowspan="2" align="left" valign="top"><div class="user_img">'+fNameCharacter+lNameCharacter+'</div></td>'+
	          '<td width="75%" align="left" valign="bottom"><a href="javascript:void(0);" class="showCohortDetails" id="cohort_name_'+cohortObject.seededMtldBean.id+'_'+cohortObject.id+'">'+name+'</a></td>'+
	          '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id="'+cohortObject.id+'" class="group_popup_icon"><img src="images/popup_icon.png" width="20" height="18"></a></td>'+
	   '</tr>'+
	  '<tr>'+
          '<td colspan="2" align="left" valign="top">'+tenureRole+'<br>'+ schoolRep +
          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">'+
          ' <tr>'+
      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
    	'%</span></td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Content</td>' +
        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Geography</td>' +
        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Relationships</td>' +
        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
	  '</tr> '+
	  '</table></td>'+
	  '</tr>'+
	  '</table>'+
	  '<div class="group_detail_counting">'+
	    '<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
		  '<tr>'+
		  '<td width="90%"><span class="cmcount_'+cohortObject.id+'">'+groupSize+'</span>'+
		    '</td>'+
		    '<td width="10%"><input name="" type="button" class="save_btn" value="save" id="cohort_save_'+cohortObject.id+'"></td>'+
		    '<td width="10%"><input name="" type="button" value="Unlock" class="unlock_btn" id="cohort_unlock_'+cohortObject.id+'" style="display:none;"></td>'+
		    '</tr>'+
		'</table>'+
		'</div>'+
	  	'<div class="clear"></div>'+
		'</div>';
	}
	else
	{
		schoolRep = '<strong class="schools_represented_'+cohortObject.id+'">0</strong> School Represented <br>';
		
		cmMtldHtml='<div class="each_group each_group_selected" id="each_group_none_'+cohortObject.id+'">'+
		'<table width="100%" border="0" cellspacing="0" cellpadding="0" id="cohort_table_none_'+cohortObject.id+'">'+
		'<tr>'+
	          '<td width="20%" rowspan="2" align="left" valign="top"><div class="user_img">EC</div></td>'+
	          '<td width="75%" align="left" valign="bottom"><a href="javascript:void(0);" class="showCohortDetails" id="cohort_name_none_'+cohortObject.id+'">Empty Cohort</a></td>'+
	          '<td width="9%" align="right" valign="bottom"><a href="javascript:void(0);" id="'+cohortObject.id+'" style="display:none; class="group_popup_icon"><img src="images/popup_icon.png" width="20" height="18"></a></td>'+
	   '</tr>'+
	  '<tr>'+
          '<td colspan="2" align="left" valign="top"><br>'+ schoolRep +
          '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="score">'+
          ' <tr>'+
      	'<td width="73%" align="left" valign="middle"><span title="Basics Score">Basics</span></td>' +
     	'<td width="27%" align="left" valign="middle"><span title="ContentScore">'+cohortObject.basicsCriteriaPer + 
    	'%</span></td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Content</td>' +
        '<td align="left" valign="middle">'+cohortObject.contentCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Geography</td>' +
        '<td align="left" valign="middle">'+cohortObject.geographicCriteriaPer+'%</td>' +
        '</tr>' +
        '<tr>' +
        '<td align="left" valign="middle">Relationships</td>' +
        '<td align="left" valign="middle">'+cohortObject.relationshipsCriteriaPer+'%</td>' +
	  '</tr> '+
	  '</table></td>'+
	  '</tr>'+
	  '</table>'+
	  '<div class="group_detail_counting">'+
	    '<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
		  '<tr>'+
		  '<td width="90%"><span class="cmcount_'+cohortObject.id+'">'+groupSize+'</span>'+
		    '</td>'+
		    '<td width="10%"><input name="" type="button" class="save_btn" value="save" id="cohort_save_'+cohortObject.id+'" style="display:none;"></td>'+
		    '<td width="10%"><input name="" type="button" value="Unlock" class="unlock_btn" id="cohort_unlock_'+cohortObject.id+'" style="display:none;"></td>'+
		    '</tr>'+
		'</table>'+
		'</div>'+
	  	'<div class="clear"></div>'+
		'</div>';
	}
	$('#cohortGroup_'+cohortObject.id).empty();
	$('#cohortGroup_'+cohortObject.id).append(cmMtldHtml);
}
