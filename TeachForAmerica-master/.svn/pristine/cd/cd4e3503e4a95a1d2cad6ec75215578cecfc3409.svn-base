package org.tfa.mtld.service.services;

import java.util.List;
import java.util.Map;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;

/**
 * 
 * @author lovely.ram
 * 
 */

public interface CohortService {

	/**
	 * Create cohort the against an region . If Region already have any existing
	 * cohort then it will show the existing as well as newly created cohort
	 * based on the criteria selected by user.
	 * 
	 * @param criteriFormBean
	 *            The criteriFormBean is being selected by user.
	 * @param user
	 *            object from session
	 * 
	 * 
	 * @return Map with contain the cohort info,MTLD info,corps member info
	 */
	public Map<String, List<?>> createGroup(CriteriaFormBean criteriaFormBean, User sessionUser)
			throws Exception;

	/**
	 * Score all the cohort list which are form through algorithm
	 * 
	 * 
	 * @param cohortList
	 * @param criteriaBeanList
	 */
	public void scoreCohortList(List<Cohort> cohortList,
			CriteriaFormBean criteriaFormBean);

	public Map<String, String> getGroupDetails(int cohortId) throws Exception;

	/**
	 * flushUnfinalizeCohort methods used clear the db table row which are used
	 * for storing unsaved cohort detail
	 * 
	 * @param regionId
	 * @throws Exception
	 */

	public void flushUnfinalizeCohort(Integer regionId) throws Exception;

	public CohortBean convertCohortToCohortBean(Cohort cohort,
			CohortBean cohortBean) throws Exception;

	public Map<String, List<?>> calculatePercentageForCriteriaGraph(
			List<CMBean> corpList, CriteriaFormBean criteriaFormBean,
			Map<String, List<?>> cohortMap) throws Exception;

	public void calculateCriteriaCategoryPercentage(
			List<CohortBean> cohortBeans,
			CriteriaFormBean criteriaFormBean) throws Exception;
	
	
	public void calculateCohortDetails(Cohort cohort) throws Exception;
	
	public void populateCriteriaAPI(CriteriaFormBean criteriaFormBean)
			throws Exception;
}
