package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;
import org.tfa.mtld.service.exception.TFAMTLDPerCohortException;

@Service
public class CohortUpdateServiceImpl implements CohortUpdateService {
	@Autowired
	MtldCmRepository mtldCmRepository;
	@Autowired
	CohortService cohortService;
	Logger logger = Logger.getLogger(CohortUpdateServiceImpl.class);

	/**
	 * Add passed CM to the specified cohort.
	 * 
	 @param criteriFormBean
	 *            The criteriFormBean is being selected by user.
	 * @param isUnhired
	 *            Flag indicates whether CM is hired or unhired.
	 * @param corpsMemberId
	 *            The corps member which needs to be added in cohort.
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * 
	 * @return None
	 */
	@Override
	public CohortBean addCMToCohort(boolean isUnhired,
			int corpsMemberId, int cohortId, CriteriaFormBean criteriaFormBean)
			throws TFAInvalidCohortException,Exception {
		logger.debug("Inside method addCMToCohort");
		CohortBean cohortBean = new CohortBean();
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		CohortDetail cohortDetail = new CohortDetail();
		CorpsMember corpMember = mtldCmRepository
				.getCorpsMemberById(corpsMemberId);
		if (cohort.getCorpsMember() == null && cohort.getMtld() == null) {
			cohort.setCorpsMember(corpMember);
		}
		cohortDetail.setCorpMember(corpMember);
		List<CohortDetail> existingCohortDetail = cohort.getCohortDetails() != null ? cohort
				.getCohortDetails() : new ArrayList<CohortDetail>();
		existingCohortDetail.add(cohortDetail);
		cohort.setCohortDetails(existingCohortDetail);
		List<Cohort> cohortList = new ArrayList<Cohort>();
		cohortList.add(cohort);
		cohortService.scoreCohortList(cohortList, criteriaFormBean);
		cohortService.calculateCohortDetails(cohort);
		mtldCmRepository.saveCohort(cohort);
		cohortService.convertCohortToCohortBean(cohort, cohortBean);
	
		logger.debug("Inside method addCMToCohort");
		return cohortBean;
	}

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
	@Override
	public CohortBean removeCMFromCohort(int corpsMemberId, int cohortId,
			CriteriaFormBean criteriaFormBean)
			throws TFAInvalidCohortException, Exception {
		logger.debug("Inside method removeCMFromCohort");
		CohortBean cohortBean = new CohortBean();
		CohortDetail cohortDetail = null;
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		for (CohortDetail detail : cohort.getCohortDetails()) {
			if (detail.getCorpMember().getId() == corpsMemberId) {
				cohortDetail = detail;
				break;
			}
		}
		cohort.getCohortDetails().remove(cohortDetail);
		if (cohort.getCorpsMember() != null
				&& cohort.getCorpsMember().getId() == corpsMemberId) {
			cohort.setCorpsMember(null);
			if (cohort.getCohortDetails().size() > 0) {
				cohort.setCorpsMember(cohort.getCohortDetails().iterator()
						.next().getCorpMember());
			}
		}
		List<Cohort> cohortList = new ArrayList<Cohort>();
		cohortList.add(cohort);
		cohortService.scoreCohortList(cohortList, criteriaFormBean);
		cohortService.calculateCohortDetails(cohort);
		if (cohortDetail != null) {
			mtldCmRepository.deleteCohortDetail(cohortDetail);
		}
		mtldCmRepository.saveCohort(cohort);
		cohortService.convertCohortToCohortBean(cohort, cohortBean);
		
		logger.debug("Inside method removeCMFromCohort");
		return cohortBean;
	}

	/**
	 * Saves the updated cohort .
	 * 
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * 
	 */
	@Override
	public CohortBean saveUpdatedCohort(int cohortId,
			CriteriaFormBean criteriaFormBean, String loginId)
			throws TFAInvalidCohortException, Exception {
		logger.debug("Inside method saveUpdatedCohort");
		CohortBean updatedCohortBean = new CohortBean();
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
	
		CohortBean cohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(cohort, cohortBean);
		List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
		cohortBeans.add(cohortBean);
		cohortService.calculateCriteriaCategoryPercentage(cohortBeans,
				criteriaFormBean);

		cohort.setBasicsCriteriaPer(cohortBeans.get(0).getBasicsCriteriaPer());
		cohort.setContentCriteriaPer(cohortBeans.get(0).getContentCriteriaPer());
		cohort.setGeographicCriteriaPer(cohortBeans.get(0)
				.getGeographicCriteriaPer());
		cohort.setRelationshipsCriteriaPer(cohortBeans.get(0)
				.getRelationshipsCriteriaPer());
		cohort.setIsFinalCohort(true);
		cohort.setUpdatedBy(loginId);
		cohort.setUpdatedDate(new Date());
		mtldCmRepository.saveCohort(cohort);
		cohortService
				.convertCohortToCohortBean(cohort, updatedCohortBean);
		logger.debug("Inside method saveUpdatedCohort");
		return updatedCohortBean;
	}

	/**
	 * unlocks the target cohort .
	 * 
	 * @param cohortId
	 *            Cohort in which corp member need to be added.
	 * @param loginId
	 *            loginId of logged-in user.
	 */
	@Override
	public CohortBean unlockCohort(int cohortId, String loginId)
			throws TFAInvalidCohortException, Exception {
		logger.debug("Inside method unlockCohort");
		CohortBean updatedCohortBean = new CohortBean();
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		cohort.setIsFinalCohort(false);
		cohort.setUpdatedBy(loginId);
		cohort.setUpdatedDate(new Date());
		mtldCmRepository.saveCohort(cohort);
		cohortService
				.convertCohortToCohortBean(cohort, updatedCohortBean);
		logger.debug("Inside method unlockCohort");
		return updatedCohortBean;
	}

	/**
	 * fetch the details of passed cohort Id.
	 * 
	 * @param cohortId
	 *            Cohort which needs to be fetched.
	 * @return CohortBean containing details of cohort.
	 */
	@Override
	public CohortBean getCohortDetails(int cohortId,
			CriteriaFormBean criteriaFormBean)
			throws TFAInvalidCohortException, Exception {
		logger.info("Inside method getCohortDetails");
		CohortBean cohortBean = new CohortBean();
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		List<Cohort> cohortList = new ArrayList<Cohort>();
		cohortList.add(cohort);
		cohortService.populateCriteriaAPI(criteriaFormBean);
		cohortService.scoreCohortList(cohortList, criteriaFormBean);
		cohortService.calculateCohortDetails(cohort);
		cohortBean = cohortService.convertCohortToCohortBean(cohort,
				cohortBean);
	
		logger.info("Inside method getCohortDetails");
		return cohortBean;
	}

	/**
	 * Remove passed seeded member from the specified cohort and assigns a new
	 * seeded member.
	 * 
	 * @param seededMemberType
	 *            Seeded Member Type(CM/MTLD).
	 * @param seededMemberId
	 *            Id of the seeded Member.
	 * @param cohortId
	 *            Cohort from which seeded member needs to be removed.
	 *	@param criteriFormBean
	 *            The criteriFormBean is being selected by user.
	 * @return CohortBean Object.
	 */
	@Override
	public CohortBean removeSeededMember(String seededMemberType,
			int seededMemberId, int cohortId,
			CriteriaFormBean criteriaFormBean)
			throws TFAInvalidCohortException, Exception {
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		CohortBean cohortBean = new CohortBean();
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		if (seededMemberType.equalsIgnoreCase(TFAConstants.SEEDED_MEMBER_CM)) {
			cohort.setCorpsMember(null);
		} else {
			cohort.setMtld(null);
		}
		List<CohortDetail> cohortDetailList = cohort.getCohortDetails();
		CorpsMember corpsMember = null;
		for (CohortDetail cohortDetail : cohortDetailList) {
			corpsMember = cohortDetail.getCorpMember();
			break;
		}
		if (corpsMember != null) {
			cohort.setCorpsMember(corpsMember);
		}
		List<Cohort> cohortList = new ArrayList<Cohort>();
		cohortList.add(cohort);
		cohortService.scoreCohortList(cohortList, criteriaFormBean);
		cohortService.calculateCohortDetails(cohort);
		mtldCmRepository.saveCohort(cohort);
		cohortService.convertCohortToCohortBean(cohort, cohortBean);
		
		return cohortBean;
	}

	/**
	 * Add passed seeded member to the specified cohort .
	 * 
	 * @param seededMemberId
	 *            Id of the seeded Member.
	 * @param cohortId
	 *            Cohort from which seeded member needs to be removed.
	 *	@param criteriFormBean
	 *            The criteriFormBean is being selected by user.
	 * @return CohortBean Object.
	 */
	public CohortBean addSeededMember(int seededMemberId, int cohortId,
			CriteriaFormBean criteriaFormBean)
			throws TFAInvalidCohortException, TFAMTLDPerCohortException,
			Exception {
		CohortBean cohortBean = new CohortBean();
		Cohort cohort = mtldCmRepository.getCohort(cohortId);
		if (cohort == null) {
			throw new TFAInvalidCohortException(
					TFAConstants.INVALID_COHORT_EXCEPTION);
		}
		if (cohort.getMtld() != null) {
			throw new TFAMTLDPerCohortException(
					TFAConstants.MTLD_PER_COHORT_EXCEPTION);
		}
		MTLD mtld = mtldCmRepository.getMTLDById(seededMemberId);
		cohort.setMtld(mtld);
		cohort.setCorpsMember(null);
		List<Cohort> cohortList = new ArrayList<Cohort>();
		cohortList.add(cohort);
		cohortService.scoreCohortList(cohortList, criteriaFormBean);
		cohortService.calculateCohortDetails(cohort);
		mtldCmRepository.saveCohort(cohort);
		cohortService.convertCohortToCohortBean(cohort, cohortBean);
		
		return cohortBean;
	}
}
