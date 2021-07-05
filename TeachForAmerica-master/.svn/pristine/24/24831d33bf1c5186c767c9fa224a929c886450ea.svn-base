package org.tfa.mtld.web.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.web.exception.TFAException;
import org.tfa.mtld.web.utils.ColumnMappingConstants;

/**
 * @author divesh.solanki
 *
 */
public class ExcelExportDataView extends AbstractExcelView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
					throws TFAException {
		String fileName = "export"+ new Date().getTime()+".xls" ;
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename="+fileName);
		httpServletResponse.setHeader("Content-Disposition", "inline;filename="+fileName);

		User user = (User) httpServletRequest.getSession().getAttribute(
				TFAConstants.SESSION_LOGGEDIN_USER_REGION);
		String regionCode = "" ; 
		
		if(user!= null && user.getRegion() != null){
			regionCode = user.getRegion().getRegionCode();
		}

		Map<String,Object> dataMap = (Map<String, Object>) model.get("exportData");
		List <CohortBean> cohorts = (List <CohortBean>) dataMap.get("cohorts");
		Map<String, List<CriteriaBean>> criteriaMap = (Map<String, List<CriteriaBean>>) dataMap.get("criteriaMap");
		
		//Create work sheet for CM and MTLD
		HSSFSheet cmSheet = workbook.createSheet(ColumnMappingConstants.CM_SHEET_NAME);
		HSSFRow cmHeaderRow = cmSheet.createRow(0);
		setCMHeader(cmHeaderRow,getCrieriaList(criteriaMap));
		HSSFSheet mtldSheet = workbook.createSheet(ColumnMappingConstants.MTLD_SHEET_NAME);
		HSSFRow mtldHeaderRow = mtldSheet.createRow(0);		
		setMTLDHeader(mtldHeaderRow);

		//Create work sheet for Criteria Values 
		HSSFSheet criteriaSheet = workbook.createSheet(ColumnMappingConstants.CRITERIA_SHEET_NAME);
		HSSFRow criteriaHeaderRow = criteriaSheet.createRow(0);
		setCriteriaHeader(criteriaHeaderRow);
		
		//Setting data for CM and MTLD
		setCMRowsData(cmSheet , cohorts, regionCode,criteriaMap);
		setMTLDRowsData(mtldSheet, cohorts);
		
		//Setting data for criteria values
		setCriteriaRowsData(criteriaSheet,criteriaMap);
	}

	private void setMTLDRowsData(HSSFSheet mtldSheet, List<CohortBean> cohorts) {
		int rowNum =1 ;
		for (Iterator iterator = cohorts.iterator(); iterator.hasNext();) {
			CohortBean cohort = (CohortBean) iterator.next();
			HSSFRow hssfRow = mtldSheet.createRow(rowNum++);
			int cellNum = 0 ;
			MTLDBean mtld= cohort.getSeededMtldBean(); 
			if( mtld != null ){
				hssfRow.createCell(cellNum++).setCellValue(mtld.getPlacementAdvisorTfaId());
				hssfRow.createCell(cellNum++).setCellValue(mtld.getFirstName());
				hssfRow.createCell(cellNum++).setCellValue(mtld.getLastName());
			}else {
				cellNum = cellNum + 3 ;
			}
			hssfRow.createCell(cellNum++).setCellValue(cohort.getId());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getCohortDetailBean() != null ? cohort.getCohortDetailBean().size():0);
			hssfRow.createCell(cellNum++).setCellValue(cohort.getSchoolRep());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getOneYearCorpPercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getTwoYearCorpPercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getEcePercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getElemPercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getMsGradePercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getHsGradePercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getSpedModifierPercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getPersonColorPercentage());			
			hssfRow.createCell(cellNum++).setCellValue(cohort.getLowIncomePercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getDistrictPartnerPercentage());
			hssfRow.createCell(cellNum++).setCellValue(cohort.getCharterPartnerPercentage());

		} 

	}

	private void setCMRowsData(HSSFSheet cmSheet, List<CohortBean> cohorts, String regionCode,Map<String, List<CriteriaBean>> criteriaMap) {
		int rowNum =1 ;
		for (Iterator iterator = cohorts.iterator(); iterator.hasNext();) {
			CohortBean cohort = (CohortBean) iterator.next();
			List<CohortDetailBean>  cohortDetails= cohort.getCohortDetailBean();
			for (Iterator iterator2 = cohortDetails.iterator(); iterator2
					.hasNext();) {
				CohortDetailBean cohortDetail = (CohortDetailBean) iterator2.next();
				CMBean corpsMember = cohortDetail.getCorpsMember();	
				if(corpsMember != null){
					HSSFRow hssfRow = cmSheet.createRow(rowNum++);
					int cellNum = 0 ;
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getTfaMasterUID());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getFirstName());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getLastName());
					hssfRow.createCell(cellNum++).setCellValue(cohort.getId());
					MTLDBean mtld= cohort.getSeededMtldBean();
					if( mtld != null ){
						hssfRow.createCell(cellNum++).setCellValue(mtld.getPlacementAdvisorTfaId());
						hssfRow.createCell(cellNum++).setCellValue(mtld.getFirstName());
						hssfRow.createCell(cellNum++).setCellValue(mtld.getLastName());
					}else {
						cellNum = cellNum + 3 ;
					}

					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getCmYear());
					//Take the region code from session Change the below code
					hssfRow.createCell(cellNum++).setCellValue(cohort.getRegionBean() != null ?cohort.getRegionBean().getRegionCode():regionCode);
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getPersonOfColor());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getIslowIncomeBackground()?"Y" : "N");
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getGradeLevel());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSubjectGroup());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSubjectModifier());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSchoolBean() != null ?corpsMember.getSchoolBean().getDistrict():"");
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSchoolBean() != null ? corpsMember.getSchoolBean().getSchoolId()+"":"");				
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getPartnerTypeCode());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getCmoAffiliation());
					//hssfRow.createCell(cellNum++).setCellValue(corpsMember.getPartnerTypeCode());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSchoolBean() != null ?corpsMember.getSchoolBean().getSchoolName():"");
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getFeederPatternHS());
					//it should be verify by Divesh
					hssfRow.createCell(cellNum++).setCellValue(cohort.getRegionBean() != null ?cohort.getRegionBean().getRegionCode():regionCode);
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getSchoolBean() != null ?corpsMember.getSchoolBean().getSchoolType():"");
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getAddress());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getCity());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getState());
					hssfRow.createCell(cellNum++).setCellValue(corpsMember.getZipCode());					
					if(cohortDetail.getFirstSeedingCorpsmember() == 'Y'){
						hssfRow.createCell(cellNum++).setCellValue("Y");
					} else {
						hssfRow.createCell(cellNum++).setCellValue("N");
					}
					Map<Integer, Double> criteriaScore = corpsMember.getCriteriaScore();
					Double totalScore = 0.0;
					
					//Below changes to make criteria values in same order as of criteria screen
					List<CriteriaBean> criteriaBeanList= getCrieriaList(criteriaMap);
					for(CriteriaBean criteriaBean:criteriaBeanList){
					
						if (criteriaScore != null) {
							for (Map.Entry<Integer, Double> entry  : corpsMember.getCriteriaScore().entrySet()) {
								if (Integer.parseInt(entry.getKey().toString()) == criteriaBean
										.getId()) {
									
									hssfRow.createCell(cellNum++).setCellValue(""+ entry.getValue());  
						    		totalScore = totalScore +entry.getValue(); 
						    		break;
									
								}
							}
						}
						
					}
					
					hssfRow.createCell(cellNum++).setCellValue(""+totalScore);
					
			
				}
			}	
			
		} 

	}
	
	private void setCriteriaRowsData(HSSFSheet criteriaSheet,
			Map<String, List<CriteriaBean>> criteriaMap) {
		List<CriteriaBean> criteriaBeanList;
		int rowNum = 1;
		for (Map.Entry<String, List<CriteriaBean>> entry : criteriaMap
				.entrySet()) {

			criteriaBeanList = entry.getValue();
			for (CriteriaBean bean : criteriaBeanList) {
				HSSFRow hssfRow = criteriaSheet.createRow(rowNum++);
				int cellNum = 0;
				hssfRow.createCell(cellNum++).setCellValue(bean.getName());
				hssfRow.createCell(cellNum++).setCellValue(
						bean.getPriorityValue());
			}
		}

	}
	
	private List<CriteriaBean> getCrieriaList(Map<String, List<CriteriaBean>> criteriaMap){
		List<CriteriaBean> beanList=new ArrayList<CriteriaBean>();
		for (Map.Entry<String, List<CriteriaBean>> entry : criteriaMap
				.entrySet()) {
		     
		      for(CriteriaBean bean:entry.getValue()){
		    	  beanList.add(bean);
		      }
			}
		return beanList;
		
	}
	
	
	private void setMTLDHeader(HSSFRow hssfRow) {
		for (int i = 0; i < ColumnMappingConstants.MTLD_HEADER_LABELS.length; i++) {
			hssfRow.createCell(i).setCellValue(ColumnMappingConstants.MTLD_HEADER_LABELS[i]);
		}

	}

	private void setCMHeader(HSSFRow hssfRow,List<CriteriaBean> criteriaList) {
		
		List<String> headerValues=new ArrayList<String>();
		 List<String> constantValues=Arrays.asList(ColumnMappingConstants.CM_HEADER_LABELS);
		 headerValues.addAll(constantValues);
		 
		for(CriteriaBean criteriaBean:criteriaList){
			headerValues.add(criteriaBean.getName());
		}
		headerValues.add(ColumnMappingConstants.TOTAL_SCORE); 
		
		for (int i = 0; i < headerValues.size(); i++) {
			hssfRow.createCell(i).setCellValue(headerValues.get(i));
		}
	
		
	}

	private void setCriteriaHeader(HSSFRow hssfRow) {
		for (int i = 0; i < ColumnMappingConstants.CRITERIA_HEADER_LABELS.length; i++) {
			hssfRow.createCell(i).setCellValue(ColumnMappingConstants.CRITERIA_HEADER_LABELS[i]);
		}
	}
	
	
	
	
}