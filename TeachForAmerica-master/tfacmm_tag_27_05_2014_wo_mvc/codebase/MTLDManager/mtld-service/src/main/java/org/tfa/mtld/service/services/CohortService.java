package org.tfa.mtld.service.services;

import java.util.List;
import java.util.Map;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.SchoolBean;

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
	 * @param criteriaBeanList
	 *            The criteriaBeanList is being selected by user.
	 * @param cohortCount
	 * @param isgroupSeededWithMTLD
	 *            The isgroupSeededWithMTLD is parameter selected by user to
	 *            inform that cohort are first seeded with MTLD Or Corps.
	 * @param isUnhierdCMIncluded
	 *            The isUnhierdCMIncluded is parameter selected by user to
	 *            inform that unhired Corps are part of the cohort formation or
	 *            not.
	 * @param user
	 *            object from session
	 * 
	 * 
	 * @return Map with contain the cohort info,MTLD info,corps member info
	 */
	public Map<String, List<?>> createGroup(
			List<CriteriaBean> criteriaBeanList, int cohortCount,
			boolean isgroupSeededWithMTLD, boolean isUnhierdCMIncluded,
			Boolean isPriorityForAllTheCriteriaIsZero, User sessionUser)
			throws Exception;

	/**
	 * Score all the cohort list which are form through algorithm
	 * 
	 * 
	 * @param cohortList
	 * @param criteriaBeanList
	 */
	public void scoreCohortList(List<Cohort> cohortList,
			List<CriteriaBean> criteriaBeanList);

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
			List<CMBean> corpList, List<CriteriaBean> criteriaBeanList,
			Map<String, List<?>> cohortMap) throws Exception;

	public void calculateCriteriaCategoryPercentage(
			List<CohortBean> cohortBeans,
			Map<String, List<CriteriaBean>> criteriaMap) throws Exception;
}
