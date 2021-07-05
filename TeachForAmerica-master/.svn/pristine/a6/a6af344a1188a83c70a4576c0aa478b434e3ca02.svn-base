package org.tfa.mtld.service.services;

import java.util.List;
import java.util.Map;

import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;
import org.tfa.mtld.service.exception.TFAMTLDPerCohortException;

public interface CohortUpdateService {
	/**
	 * Add passed CM to the specified cohort.
	 * 
	 * @param criteriaMap
	 *            Map contains the criteria priority and field details.
	 * @param isUnhired
	 *            Flag indicates whether CM is hired or unhired.
	 * @param corpsMemberId
	 *            The corps member which needs to be added in cohort.
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * 
	 * @return None
	 */
	public CohortBean addCMToCohort(Map<String, List<CriteriaBean>> criteriaMap,
			boolean isUnhired, int corpsMemberId, int cohortId,
			List<CriteriaBean> criteriaBeanList)
			throws /*TFACorpsPerMTLDException,*/TFAInvalidCohortException, Exception;

	/**
	 * Remove passed CM from the specified cohort.
	 * 
	 * @param corpsMemberId
	 *            The corps member which needs to be added in cohort.
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * 
	 * @return Flag which indicated whether removed CM was hired or unhired.
	 */
	public CohortBean removeCMFromCohort(int corpsMemberId, int cohortId,
			List<CriteriaBean> criteriaBeanList) throws TFAInvalidCohortException,Exception;

	/**
	 * Saves the updated cohort .
	 * 
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * @param loginId
	 *            loginId of logged-in user.
	 * @param criteriaList
	 *            list of CriteriaBean.
	 * 
	 */
	public CohortBean saveUpdatedCohort(int cohortId,Map<String,List<CriteriaBean>> criteriaMap, String loginId)
			throws TFAInvalidCohortException, Exception;

	/**
	 * unlocks the target cohort .
	 * 
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * @param loginId
	 *            loginId of logged-in user.
	 */
	public CohortBean unlockCohort(int cohortId, String loginId) throws TFAInvalidCohortException,Exception;

	/**
	 * fetch the details of passed cohort Id.
	 * 
	 * @param cohortId
	 *            Cohort which needs to be fetched.
	 *            
	 * @return CohortBean containing details of cohort.
	 */
	public CohortBean getCohortDetails(int cohortId,List<CriteriaBean> criteriaBeanList) throws TFAInvalidCohortException,Exception;

	/**
	 * Remove passed seeded member from the specified cohort and assigns a new seeded member.
	 * 
	 * @param seededMemberType
	 *            Seeded Member Type(CM/MTLD).
	 * @param seededMemberId
	 *            Id of the seeded Member.
	 * @param cohortId
	 *            Cohort from which seeded member needs to be removed.
	 * @param criteriaBeanList
	 * 			  List of all the criteria's details.   
	 * @return  CohortBean Object.
	 */
	public CohortBean removeSeededMember(String seededMemberType,int seededMemberId, int cohortId,List<CriteriaBean> criteriaBeanList) throws TFAInvalidCohortException,Exception;
	
	/**
	 * Add passed seeded member to the specified cohort .
	 * 
	 * @param seededMemberId
	 *            Id of the seeded Member.
	 * @param cohortId
	 *            Cohort from which seeded member needs to be removed.
	 * @param criteriaBeanList
	 * 			  List of all the criteria's details.   
	 * @return  CohortBean Object.
	 */
	public CohortBean addSeededMember(int seededMemberId,int cohortId,List<CriteriaBean> criteriaBeanList) throws TFAInvalidCohortException,TFAMTLDPerCohortException,Exception;
}
