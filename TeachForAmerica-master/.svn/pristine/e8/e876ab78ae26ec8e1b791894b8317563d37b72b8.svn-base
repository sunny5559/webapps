package org.tfa.mtld.web.controller.ws;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.FileUploadForm;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.bean.SchoolBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.services.CorpsMemberService;
import org.tfa.mtld.service.services.MTLDService;
import org.tfa.mtld.web.exception.TFAException;
import org.tfa.mtld.web.utils.ColumnMappingConstants;

/**
 * @author divesh.solanki
 * 
 */
@Controller
public class ExcelFileImportController {

	Logger logger = Logger.getLogger(ExcelFileImportController.class);

	@Autowired
	MTLDService mtldService;

	@Autowired
	CorpsMemberService corpsMemberService;

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") FileUploadForm uploadForm,
			Model map) throws TFAException {

		List<MTLDBean> mtlds = new ArrayList<MTLDBean>();
		MultipartFile multipartFile = uploadForm.getFile();
	
		try {

			int i = 1;
			// Creates a workbook object from the uploaded excelfile
			if ("application/vnd.ms-excel".equalsIgnoreCase(multipartFile
					.getContentType())) {
				// XSSF workbook = new XSSF(multipartFile.getInputStream());
				HSSFWorkbook workbook = new HSSFWorkbook(
						multipartFile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet mtldSheet = workbook.getSheetAt(0);
				i = 1;
				while (i < mtldSheet.getLastRowNum()) {
					Row row = mtldSheet.getRow(i++);
					MTLDBean mtld = new MTLDBean();
					mtld.setFirstName(row.getCell(
							ColumnMappingConstants.MTLD_FIRST_NAME_INDEX)
							.getStringCellValue());
					mtld.setLastName(row.getCell(
							ColumnMappingConstants.MTLD_LAST_NAME_INDEX)
							.getStringCellValue());

					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_MTLD_REGION_INDEX)
							.getCellType()) {
						mtld.setRegionCode(row.getCell(
								ColumnMappingConstants.MTLD_MTLD_REGION_INDEX)
								.getStringCellValue());
					}

					if (Cell.CELL_TYPE_STRING == row
							.getCell(
									ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
							.getCellType()) {
						mtld.setPlacementAdvisorTfaId(row
								.getCell(
										ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row
							.getCell(
									ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
							.getCellType()) {
						mtld.setPlacementAdvisorTfaId(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
										.getNumericCellValue()));
					}

					mtld.setAlum("Y".equalsIgnoreCase(row.getCell(
							ColumnMappingConstants.MTLD_ALUM_INDEX)
							.getStringCellValue()));
					mtld.setCmoAffiliation(row.getCell(
							ColumnMappingConstants.MTLD_CMO_AFFILATION_INDEX)
							.getStringCellValue());
					mtld.setCorpsSubjectModifier(row.getCell(
							ColumnMappingConstants.MTLD_SUBJECT_MODIFIER_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_CORPS_YEARS_INDEX)
							.getCellType()) {
						mtld.setCorpsYears(getIntegerValue(row.getCell(
								ColumnMappingConstants.MTLD_CORPS_YEARS_INDEX)
								.getNumericCellValue()));
					}

					mtld.setGradeLevel(row.getCell(
							ColumnMappingConstants.MTLD_GRADE_LEVEL_INDEX)
							.getStringCellValue());
					mtld.setLowIncomeBackground("Y"
							.equalsIgnoreCase(row
									.getCell(
											ColumnMappingConstants.MTLD_LOW_INCOME_BACKGROUND_INDEX)
									.getStringCellValue()));
					mtld.setNeighborhood(row.getCell(
							ColumnMappingConstants.MTLD_NEIGHBORHOOD_INDEX)
							.getStringCellValue());
					mtld.setPersonColor("Y".equalsIgnoreCase(row.getCell(
							ColumnMappingConstants.MTLD_PERSON_OF_COLOR_INDEX)
							.getStringCellValue()));
					mtld.setEthnicity(row.getCell(
							ColumnMappingConstants.MTLD_ETHNICITY_INDEX)
							.getStringCellValue());
					mtld.setPlacementDistrict(row
							.getCell(
									ColumnMappingConstants.MTLD_SCHOOL_DISTRICT_CORPS_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
							.getCellType()) {
						mtld.setTenureRole(row
								.getCell(
										ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
							.getCellType()) {
						mtld.setTenureRole(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
										.getNumericCellValue()));
					}
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_MD_TLD_INDEX)
							.getCellType()) {
						mtld.setmDTLD(row.getCell(
								ColumnMappingConstants.MTLD_MD_TLD_INDEX)
								.getStringCellValue());
					}
					mtld.setAddress(row.getCell(
							ColumnMappingConstants.MTLD_ADDRESS_INDEX)
							.getStringCellValue());
					mtld.setCity(row.getCell(
							ColumnMappingConstants.MTLD_CITY_INDEX)
							.getStringCellValue());
					mtld.setState(row.getCell(
							ColumnMappingConstants.MTLD_STATE_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
							.getCellType()) {
						mtld.setZipCode(row.getCell(
								ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
							.getCellType()) {
						mtld.setZipCode(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
										.getNumericCellValue()));
					}

					mtld.setSubjectTaught(row.getCell(
							ColumnMappingConstants.MTLD_SUBJECT_TAUGHT_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_COPRS_REGION)
							.getCellType()) {
						mtld.setCorpsRegionName(row.getCell(
								ColumnMappingConstants.MTLD_COPRS_REGION)
								.getStringCellValue());
					}

					mtlds.add(mtld);
					
				}

			} else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
					.equalsIgnoreCase(multipartFile.getContentType())  || "application/octet-stream".equalsIgnoreCase(multipartFile.getContentType()) ) {
				XSSFWorkbook workbook = new XSSFWorkbook(
						multipartFile.getInputStream());
				XSSFSheet mtldSheet = workbook.getSheetAt(0);
				i = 1;
				while (i <= mtldSheet.getLastRowNum()) {
					Row row = mtldSheet.getRow(i++);
					MTLDBean mtld = new MTLDBean();
					mtld.setFirstName(row.getCell(
							ColumnMappingConstants.MTLD_FIRST_NAME_INDEX)
							.getStringCellValue());
					mtld.setLastName(row.getCell(
							ColumnMappingConstants.MTLD_LAST_NAME_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row
							.getCell(
									ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
							.getCellType()) {
						mtld.setPlacementAdvisorTfaId(row
								.getCell(
										ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row
							.getCell(
									ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
							.getCellType()) {
						mtld.setPlacementAdvisorTfaId(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_PLACEMENT_ADVISOR_TFA_UID_INDEX)
										.getNumericCellValue()));
					}
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_MTLD_REGION_INDEX)
							.getCellType()) {
						mtld.setRegionCode(row.getCell(
								ColumnMappingConstants.MTLD_MTLD_REGION_INDEX)
								.getStringCellValue());
					}
					mtld.setAlum("Y".equalsIgnoreCase(row.getCell(
							ColumnMappingConstants.MTLD_ALUM_INDEX)
							.getStringCellValue()));
					mtld.setCmoAffiliation(row.getCell(
							ColumnMappingConstants.MTLD_CMO_AFFILATION_INDEX)
							.getStringCellValue());
					mtld.setCorpsSubjectModifier(row.getCell(
							ColumnMappingConstants.MTLD_SUBJECT_MODIFIER_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_CORPS_YEARS_INDEX)
							.getCellType()) {
						mtld.setCorpsYears(getIntegerValue(row.getCell(
								ColumnMappingConstants.MTLD_CORPS_YEARS_INDEX)
								.getNumericCellValue()));
					}

					mtld.setGradeLevel(row.getCell(
							ColumnMappingConstants.MTLD_GRADE_LEVEL_INDEX)
							.getStringCellValue());
					mtld.setLowIncomeBackground("Y"
							.equalsIgnoreCase(row
									.getCell(
											ColumnMappingConstants.MTLD_LOW_INCOME_BACKGROUND_INDEX)
									.getStringCellValue())

					);
					mtld.setNeighborhood(row.getCell(
							ColumnMappingConstants.MTLD_NEIGHBORHOOD_INDEX)
							.getStringCellValue());
					mtld.setPersonColor("Y".equalsIgnoreCase(row.getCell(
							ColumnMappingConstants.MTLD_PERSON_OF_COLOR_INDEX)
							.getStringCellValue()));
					mtld.setEthnicity(row.getCell(
							ColumnMappingConstants.MTLD_ETHNICITY_INDEX)
							.getStringCellValue());
					mtld.setPlacementDistrict(row
							.getCell(
									ColumnMappingConstants.MTLD_SCHOOL_DISTRICT_CORPS_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
							.getCellType()) {
						mtld.setTenureRole(row
								.getCell(
										ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
							.getCellType()) {
						mtld.setTenureRole(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_CURRENT_TENURE_INDEX)
										.getNumericCellValue()));
					}

					mtld.setAddress(row.getCell(
							ColumnMappingConstants.MTLD_ADDRESS_INDEX)
							.getStringCellValue());
					mtld.setCity(row.getCell(
							ColumnMappingConstants.MTLD_CITY_INDEX)
							.getStringCellValue());
					mtld.setState(row.getCell(
							ColumnMappingConstants.MTLD_STATE_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
							.getCellType()) {
						mtld.setZipCode(row.getCell(
								ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
								.getStringCellValue());
					} else if (Cell.CELL_TYPE_NUMERIC == row.getCell(
							ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
							.getCellType()) {
						mtld.setZipCode(""
								+ getIntegerValue(row
										.getCell(
												ColumnMappingConstants.MTLD_ZIP_CODE_INDEX)
										.getNumericCellValue()));
					}

					mtld.setSubjectTaught(row.getCell(
							ColumnMappingConstants.MTLD_SUBJECT_TAUGHT_INDEX)
							.getStringCellValue());
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_COPRS_REGION)
							.getCellType()) {
						mtld.setCorpsRegionName(row.getCell(
								ColumnMappingConstants.MTLD_COPRS_REGION)
								.getStringCellValue());
					}
					if (Cell.CELL_TYPE_STRING == row.getCell(
							ColumnMappingConstants.MTLD_MD_TLD_INDEX)
							.getCellType()) {
						mtld.setmDTLD(row.getCell(
								ColumnMappingConstants.MTLD_MD_TLD_INDEX)
								.getStringCellValue());
					}
					mtlds.add(mtld);
					
				}
			}
		
			mtldService.save(mtlds);
			
			if (mtlds!=null &&  mtlds.size()>0) {
				map.addAttribute("success",
						TFAConstants.SEEDED_MEMBER_MTLD +TFAConstants.UPLOAD_SUCCESS_MESSAGE);
			} else {
				map.addAttribute("uploadMessage",
						multipartFile.getOriginalFilename()
								+ TFAConstants.EMPTY_UPLOAD_MESSAGE);
				}
			//map.addAttribute("mtldCount", mtlds.size());
			//map.addAttribute("fileName", multipartFile.getName());
			return "file_upload_form";
			
		} catch (Exception exception) {
			logger.error(
					"Exception in ExcelFileImportController "
							+ exception.getMessage(), exception);
			map.addAttribute("uploadError", TFAConstants.SEEDED_MEMBER_MTLD + TFAConstants.UPLOAD_ERROR);
			return "file_upload_form";
		//	throw new TFAException();

			// logger.error(TFAConstants.GENERIC_ERROR_MESSAGE, e);
			// map.addAttribute(
			// "errorMessage",
			// TFAConstants.GENERIC_ERROR_MESSAGE);
			// return "file_upload_form";
		}
		//return "file_upload_success";
	}

	private int getIntegerValue(double numericCellValue) {
		return new BigDecimal(numericCellValue).setScale(0,
				RoundingMode.HALF_UP).intValue();

	}

	@RequestMapping(value = "/uploadCorpsmembers", method = RequestMethod.POST)
	public String uploadCorpsMemberRecord(
			@ModelAttribute("corpsUploadForm") FileUploadForm uploadCorpsForm,
			HttpServletRequest request, Model map) throws TFAException {

		List<CMBean> corps = new ArrayList<CMBean>();

		int rowCount = 0;

		try {
			if (null != uploadCorpsForm) {
				MultipartFile multipartFile = uploadCorpsForm.getFile();
				User sessionUser = (User) request.getSession().getAttribute(
						TFAConstants.SESSION_LOGGEDIN_USER_REGION);
				SchoolBean schoolBean = new SchoolBean();
				String fileExtension = (String) request
						.getParameter("fileExtension");

				if (!multipartFile.isEmpty()) {
					int i = 1;
					// Creates a workbook object from the uploaded excelfile
					if ("application/vnd.ms-excel"
							.equalsIgnoreCase(multipartFile.getContentType())) {
						// XSSF workbook = new
						// XSSF(multipartFile.getInputStream());
						HSSFWorkbook workbook = new HSSFWorkbook(
								multipartFile.getInputStream());
						// Creates a worksheet object representing the first
						// sheet
						HSSFSheet corpsSheet = workbook.getSheetAt(0);
						rowCount = corpsSheet.getLastRowNum();
						i = 1;
						while (i <= rowCount) {
							Row row = corpsSheet.getRow(i);

							if (null != row
									.getCell(ColumnMappingConstants.CORPS_TFA_UID_INDEX)
									&& !("").equalsIgnoreCase(row
											.getCell(
													ColumnMappingConstants.CORPS_TFA_UID_INDEX)
											.toString())) {
								CMBean corp = new CMBean();
								corp = populateCorpsMember(corp, schoolBean,
										row);
								corps.add(corp);
							}
							i++;
						}
					} else if ("application/octet-stream"
							.equalsIgnoreCase(multipartFile.getContentType())) {

						if (".xlsx".equalsIgnoreCase(fileExtension)) {
							XSSFWorkbook workbook = new XSSFWorkbook(
									multipartFile.getInputStream());
							XSSFSheet corpsSheet = workbook.getSheetAt(0);
							rowCount = corpsSheet.getLastRowNum();
							i = 1;
							while (i <= rowCount) {

								Row row = corpsSheet.getRow(i);

								if (null != row
										.getCell(ColumnMappingConstants.CORPS_TFA_UID_INDEX)
										&& !("").equalsIgnoreCase(row
												.getCell(
														ColumnMappingConstants.CORPS_TFA_UID_INDEX)
												.toString())) {
									CMBean corp = new CMBean();
									corp = populateCorpsMember(corp,
											schoolBean, row);
									corps.add(corp);
								}
								i++;
							}

						} else if (".xls".equalsIgnoreCase(fileExtension)) {

							HSSFWorkbook workbook = new HSSFWorkbook(
									multipartFile.getInputStream());
							// Creates a worksheet object representing the first
							// sheet
							HSSFSheet corpsSheet = workbook.getSheetAt(0);

							rowCount = corpsSheet.getLastRowNum();
							i = 1;
							while (i <= rowCount) {

								Row row = corpsSheet.getRow(i);

								if (null != row
										.getCell(ColumnMappingConstants.CORPS_TFA_UID_INDEX)
										&& !("").equalsIgnoreCase(row
												.getCell(
														ColumnMappingConstants.CORPS_TFA_UID_INDEX)
												.toString())) {
									CMBean corp = new CMBean();
									corp = populateCorpsMember(corp,
											schoolBean, row);
									corps.add(corp);
								}
								i++;
							}
						}

					} else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
							.equalsIgnoreCase(multipartFile.getContentType())) {

						XSSFWorkbook workbook = new XSSFWorkbook(
								multipartFile.getInputStream());
						XSSFSheet corpsSheet = workbook.getSheetAt(0);
						rowCount = corpsSheet.getLastRowNum();
						i = 1;
						while (i <= rowCount) {

							Row row = corpsSheet.getRow(i);

							if (null != row
									.getCell(ColumnMappingConstants.CORPS_TFA_UID_INDEX)
									&& !("").equalsIgnoreCase(row
											.getCell(
													ColumnMappingConstants.CORPS_TFA_UID_INDEX)
											.toString())) {
								CMBean corp = new CMBean();
								corp = populateCorpsMember(corp, schoolBean,
										row);
								corps.add(corp);
							}
							i++;
						}

					}

					else {
						logger.debug("Error in ExcelFileImportController at uploadCorpsMemberRecord, Content type not supported "
								+ multipartFile.getContentType());
						map.addAttribute("uploadError",
								multipartFile.getOriginalFilename()
										+ TFAConstants.CM_UPLOAD_CONTENT_ERROR);

						return "file_upload_form";
					}
					corpsMemberService.saveCoprsMember(corps, sessionUser);
					logger.debug("Number of corps updated " + corps.size());
					if (rowCount >= 1) {
						map.addAttribute("success",TFAConstants.SEEDED_MEMBER_CM+ TFAConstants.UPLOAD_SUCCESS_MESSAGE);
					} else {
						map.addAttribute("uploadMessage",
								multipartFile.getOriginalFilename()+ TFAConstants.EMPTY_UPLOAD_MESSAGE);
					}
				} else {
					map.addAttribute("uploadMessage",
							multipartFile.getOriginalFilename()
									+ TFAConstants.EMPTY_UPLOAD_MESSAGE);
				}

			}
		} catch (Exception exception) {
			logger.error(
					"Exception in ExcelFileImportController at uploadCorpsMemberRecord "
							+ exception.getMessage(), exception);
			map.addAttribute("uploadError",  TFAConstants.SEEDED_MEMBER_CM + TFAConstants.UPLOAD_ERROR);

		}

		uploadCorpsForm = null;

		return "file_upload_form";

	}

	private CMBean populateCorpsMember(CMBean corp, SchoolBean schoolBean,
			Row row) {

		if (null != row.getCell(ColumnMappingConstants.CORPS_SCHOOL_ID_INDEX)){
				if (Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_SCHOOL_ID_INDEX)
						.getCellType()) {
			schoolBean.setSchoolTfaUid(row.getCell(
					ColumnMappingConstants.CORPS_SCHOOL_ID_INDEX)
					.getStringCellValue());
			corp.setSchoolBean(schoolBean);
						}
				else if(Cell.CELL_TYPE_NUMERIC == row
						.getCell(ColumnMappingConstants.CORPS_SCHOOL_ID_INDEX)
						.getCellType()){
					schoolBean.setSchoolTfaUid(""+row.getCell(
							ColumnMappingConstants.CORPS_SCHOOL_ID_INDEX)
							.getNumericCellValue());
					corp.setSchoolBean(schoolBean);
				}

		}

		if (Cell.CELL_TYPE_STRING == row.getCell(
				ColumnMappingConstants.CORPS_TFA_UID_INDEX).getCellType()) {
			corp.setTfaMasterUID(row.getCell(
					ColumnMappingConstants.CORPS_TFA_UID_INDEX)
					.getStringCellValue());
		} else if (Cell.CELL_TYPE_NUMERIC == row.getCell(
				ColumnMappingConstants.CORPS_TFA_UID_INDEX).getCellType()) {
			corp.setTfaMasterUID(""
					+ getIntegerValue(row.getCell(
							ColumnMappingConstants.CORPS_TFA_UID_INDEX)
							.getNumericCellValue()));
		}

		if (null != row.getCell(ColumnMappingConstants.CORPS_FIRST_NAME_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_FIRST_NAME_INDEX)
						.getCellType()) {
			corp.setFirstName(row.getCell(
					ColumnMappingConstants.CORPS_FIRST_NAME_INDEX)
					.getStringCellValue());
		}
		if (null != row.getCell(ColumnMappingConstants.CORPS_LAST_NAME_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_LAST_NAME_INDEX)
						.getCellType()) {
			corp.setLastName(row.getCell(
					ColumnMappingConstants.CORPS_LAST_NAME_INDEX)
					.getStringCellValue());
		}
		if (null != row
				.getCell(ColumnMappingConstants.CORPS_HIRED_STATUS_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_HIRED_STATUS_INDEX)
						.getCellType()) {
			corp.setHiredStatus(row.getCell(
					ColumnMappingConstants.CORPS_HIRED_STATUS_INDEX)
					.getStringCellValue());
		}
		if (null != row.getCell(ColumnMappingConstants.CORPS_GRADE_LEVEL_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_GRADE_LEVEL_INDEX)
						.getCellType()) {
			corp.setGradeLevel(row.getCell(
					ColumnMappingConstants.CORPS_GRADE_LEVEL_INDEX)
					.getStringCellValue());
		}
		if (null != row
				.getCell(ColumnMappingConstants.CORPS_SUBJECT_GROUP_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_SUBJECT_GROUP_INDEX)
						.getCellType()) {
			corp.setSubjectGroup(row.getCell(
					ColumnMappingConstants.CORPS_SUBJECT_GROUP_INDEX)
					.getStringCellValue());
		}
		if (null != row
				.getCell(ColumnMappingConstants.CORPS_SUBJECT_MODIFIER_INDEX)
				&& Cell.CELL_TYPE_STRING == row.getCell(
						ColumnMappingConstants.CORPS_SUBJECT_MODIFIER_INDEX)
						.getCellType()) {
			corp.setSubjectModifier(row.getCell(
					ColumnMappingConstants.CORPS_SUBJECT_MODIFIER_INDEX)
					.getStringCellValue());
		}

		return corp;

	}
	
	
	
	
	private MTLDBean populateMTLDBean(MTLDBean mtldBean,Row row){
		
		return mtldBean;
		
	}
	
	
	
	}
