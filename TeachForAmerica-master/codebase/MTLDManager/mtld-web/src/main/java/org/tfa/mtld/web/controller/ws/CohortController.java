package org.tfa.mtld.web.controller.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.bean.SchoolBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;
import org.tfa.mtld.service.exception.TFAMTLDPerCohortException;
import org.tfa.mtld.service.services.CohortService;
import org.tfa.mtld.service.services.CohortUpdateService;
import org.tfa.mtld.service.services.MTLDService;
import org.tfa.mtld.service.services.SchoolService;
import org.tfa.mtld.web.exception.TFAException;
import org.tfa.mtld.web.utils.JsonResponse;
import org.tfa.mtld.web.utils.JsonResponse.JsonResponseStatus;

import com.google.gson.Gson;

@Controller
@Scope("session")
public class CohortController {

	@Autowired
	public CohortService cohortService;

	@Autowired
	public MTLDService mtldService;

	@Autowired
	public SchoolService schoolService;

	@Autowired
	public CohortUpdateService cohortUpdateService;

	Logger logger = Logger.getLogger(CohortController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/makeMyMatches", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String makeMyMatches(
			@ModelAttribute("criteriaFormBean") CriteriaFormBean criteriaFormBean,
			HttpServletRequest request, Model model, HttpSession session)
			throws TFAException {
		logger.info("cohorts controller");

		long startTime = System.currentTimeMillis();
		logger.info("In Cohort controller in time "
				+ System.currentTimeMillis());

		if (request.getMethod().equalsIgnoreCase("GET")) {
			return "redirect:selectCriteria";
		}

		try {

			User user = (User) session
					.getAttribute(TFAConstants.SESSION_LOGGEDIN_USER_REGION);

			cohortService.flushUnfinalizeCohort(user.getRegion().getRegionId());
			
			Gson gson = new Gson();

			String criteriaString = gson.toJson(criteriaFormBean);

			session.setAttribute(
					TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING,
					criteriaString);
			
			cohortService.populateCriteriaAPI(criteriaFormBean);
			// added by Lovely ram
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaFormBean, user);

			List<CMBean> corps = new ArrayList<CMBean>();
			List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
					.get(TFAConstants.COHORT_LIST);
			// Added code for Criteria category percentage
			if (criteriaFormBean.getIsPriorityForAllTheCriteriaIsNotZero()) {
				cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
						criteriaFormBean);
			}
			for (CohortBean cohortBean : cohortBeans) {
				if (!cohortBean.getIsFinalCohort()
						&& cohortBean.getCohortDetailBean() != null) {
					for (CohortDetailBean cohortDetailBean : cohortBean
							.getCohortDetailBean()) {
						corps.add(cohortDetailBean.getCorpsMember().clone());
					}
				}
			}
			cohortMap = cohortService.calculatePercentageForCriteriaGraph(
					corps, criteriaFormBean, cohortMap);

			model.addAttribute("cohortsdata", cohortMap);
			List<String> cohortIDs = (List<String>) cohortMap
					.get(TFAConstants.COHORT_ID_LIST);
			String cohortCSVID = org.apache.commons.lang.StringUtils.join(
					cohortIDs, ", ");

			model.addAttribute("cohortCSVID", cohortCSVID);
		} catch (Exception exception) {
			logger.error("Exception in CohortController.makeMyMatches() "
					+ exception.getMessage(), exception);
			throw new TFAException();

		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Cohort controller executin time taken " + timeDiff);
		return "cohorts";
	}

	@RequestMapping(value = "/addCorpMember/{listType}/{corpsMemberId}/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse addCorpMember(Model model,
			@PathVariable String listType, @PathVariable int corpsMemberId,
			@PathVariable int cohortId, HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {
			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			cohortService.populateCriteriaAPI(criteriaFormBean);
			boolean isUnhired = listType
					.equalsIgnoreCase(TFAConstants.CM_UNHIRED) ? true : false;

			CohortBean cohortBean = cohortUpdateService.addCMToCohort(
					isUnhired, corpsMemberId, cohortId, criteriaFormBean);

			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			cohortBeans.add(cohortBean);
			cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
					criteriaFormBean);

			resp.setData(cohortBean);
			resp.setStatus(JsonResponseStatus.SUCCESS.toString());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	@RequestMapping(value = "/removeCorpMember/{corpsMemberId}/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse removeCorpMember(@PathVariable int corpsMemberId,
			@PathVariable int cohortId, Model model, HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {

			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			cohortService.populateCriteriaAPI(criteriaFormBean);
			CohortBean cohortBean = cohortUpdateService.removeCMFromCohort(
					corpsMemberId, cohortId, criteriaFormBean);

			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			cohortBeans.add(cohortBean);
			cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
					criteriaFormBean);

			resp.setData(cohortBean);
			resp.setStatus(JsonResponseStatus.SUCCESS.toString());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	@RequestMapping(value = "/saveUpdatedCohort/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse saveUpdatedCohort(@PathVariable int cohortId,
			HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {

			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			cohortService.populateCriteriaAPI(criteriaFormBean);
			User user = (User) session
					.getAttribute(TFAConstants.SESSION_LOGGEDIN_USER_REGION);

			cohortUpdateService.saveUpdatedCohort(cohortId, criteriaFormBean,
					user.getLoginId());
			resp.setStatus(JsonResponseStatus.SUCCESS.toString());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	@RequestMapping(value = "/getGroupDetails/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getGroupDetails(@PathVariable int cohortId,
			HttpSession session) throws TFAException {

		JsonResponse resp = new JsonResponse();
		try {

			Map<String, String> groupDetails = cohortService
					.getGroupDetails(cohortId);
			if (groupDetails != null && groupDetails.size() > 0) {
				resp.setData(groupDetails);
				resp.setStatus(JsonResponseStatus.SUCCESS.toString());
			} else {
				resp.setStatus(JsonResponseStatus.FAIL.toString());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
			throw new TFAException(e.getMessage());

		}
		return resp;
	}

	@RequestMapping(value = "/unlockCohort/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse unlockCohort(@PathVariable int cohortId,
			HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {
			User user = (User) session
					.getAttribute(TFAConstants.SESSION_LOGGEDIN_USER_REGION);
			cohortUpdateService.unlockCohort(cohortId, user.getLoginId());
			resp.setStatus(JsonResponseStatus.SUCCESS.toString());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	private List<SchoolBean> getSchoolDetails(CohortBean cohortBean,
			HttpSession session) throws TFAException {
		List<SchoolBean> schoolDetails = new ArrayList<SchoolBean>();
		SchoolBean schoolBean = null;
		Integer cmCount = new Integer(1);
		HashMap<Integer, Integer> calculateCMCount = new HashMap<Integer, Integer>();
		try {

			List<CohortDetailBean> sCohortDetailBeans = cohortBean
					.getCohortDetailBean();
			for (CohortDetailBean cohortDetailBean : sCohortDetailBeans) {
				schoolBean = new SchoolBean();
				if (cohortDetailBean != null
						&& cohortDetailBean.getCorpsMember() != null
						&& cohortDetailBean.getCorpsMember().getSchoolBean() != null
						&& cohortDetailBean.getCorpsMember().getSchoolBean()
								.getAddress() != null) {

					SchoolBean schoolBeanObj = cohortDetailBean
							.getCorpsMember().getSchoolBean();
					schoolBean.setLatitude(schoolBeanObj.getLatitude());
					schoolBean.setLongitude(schoolBeanObj.getLongitude());
					schoolBean.setAddress(schoolBeanObj.getAddress());
					schoolBean.setSchoolName(schoolBeanObj.getSchoolName());
					schoolBean.setSchoolId(schoolBeanObj.getSchoolId());

					if (calculateCMCount.containsKey(cohortDetailBean
							.getCorpsMember().getSchoolBean().getSchoolId())) {
						cmCount = calculateCMCount
								.get(cohortDetailBean.getCorpsMember()
										.getSchoolBean().getSchoolId()) + 1;
						calculateCMCount.put(cohortDetailBean.getCorpsMember()
								.getSchoolBean().getSchoolId(), cmCount);
						schoolBean.setCmCount(cmCount);
					} else {
						calculateCMCount.put(cohortDetailBean.getCorpsMember()
								.getSchoolBean().getSchoolId(), 1);
						cmCount = calculateCMCount
								.get(cohortDetailBean.getCorpsMember()
										.getSchoolBean().getSchoolId());
						schoolBean.setCmCount(cmCount);
					}

					if (null != cohortBean.getSeededMtldBean()) {
						MTLDBean mtld = new MTLDBean();
						mtld.setFirstName(cohortBean.getSeededMtldBean()
								.getFirstName());
						mtld.setLastName(cohortBean.getSeededMtldBean()
								.getLastName());
						schoolBean.setMtldBean(mtld);

					}

					schoolDetails.add(schoolBean);
				}

			}
		} catch (Exception exception) {
			logger.error(exception.getMessage(), exception);
			throw new TFAException();
		}
		return schoolDetails;
	}

	@RequestMapping(value = "/getCohortDetails/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getCohortDetails(@PathVariable int cohortId,
			HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {

			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			
			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			CohortBean cohortBean = cohortUpdateService.getCohortDetails(
					cohortId, criteriaFormBean);
			cohortBeans.add(cohortBean);
			cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
					criteriaFormBean);
			List<SchoolBean> schoolDetails = getSchoolDetails(cohortBean,
					session);
			//updateSchoolLatLon(session); Not need this code as Timer job will update lat log 
			cohortBean.setSchoolBeans(schoolDetails);
			resp.setStatus(JsonResponseStatus.SUCCESS.toString());
			resp.setData(cohortBean);

		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	@RequestMapping(value = "/removeSeededMember/{seededMemberType}/{seededMemberId}/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse removeSeededMember(
			@PathVariable String seededMemberType,
			@PathVariable int seededMemberId, @PathVariable int cohortId,
			HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {

			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			cohortService.populateCriteriaAPI(criteriaFormBean);
			CohortBean cohortBean = cohortUpdateService.removeSeededMember(
					seededMemberType, seededMemberId, cohortId,
					criteriaFormBean);
			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			cohortBeans.add(cohortBean);
			cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
					criteriaFormBean);
			resp.setData(cohortBean);
			resp.setStatus(JsonResponse.JsonResponseStatus.SUCCESS.toString());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	@RequestMapping(value = "/addSeededMember/{seededMemberId}/{cohortId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse addSeededMember(@PathVariable int seededMemberId,
			@PathVariable int cohortId, HttpSession session) {
		JsonResponse resp = new JsonResponse();
		try {

			Gson criteriaGson = new Gson();
			CriteriaFormBean criteriaFormBean = criteriaGson.fromJson(
					session.getAttribute(
							TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
							.toString(), CriteriaFormBean.class);
			cohortService.populateCriteriaAPI(criteriaFormBean);
			CohortBean cohortBean = cohortUpdateService.addSeededMember(
					seededMemberId, cohortId, criteriaFormBean);
			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			cohortBeans.add(cohortBean);
			cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
					criteriaFormBean);
			resp.setData(cohortBean);
			resp.setStatus(JsonResponse.JsonResponseStatus.SUCCESS.toString());
		} catch (TFAMTLDPerCohortException tmpce) {
			logger.error(tmpce.getMessage(), tmpce);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tmpce.getMessage());
		} catch (TFAInvalidCohortException tice) {
			logger.error(tice.getMessage(), tice);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(tice.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setStatus(JsonResponseStatus.FAIL.toString());
			resp.setMessage(TFAConstants.GENERIC_ERROR_MESSAGE);
		}
		return resp;
	}

	private void updateSchoolLatLon(HttpSession session) throws Exception{
		User user = (User) session
				.getAttribute(TFAConstants.SESSION_LOGGEDIN_USER_REGION);
		schoolService.updateSchoolNullLatLong(user.getRegion().getRegionId());
		}
		
	}
	

