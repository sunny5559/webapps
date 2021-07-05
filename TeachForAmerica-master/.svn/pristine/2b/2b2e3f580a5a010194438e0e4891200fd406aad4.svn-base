package org.tfa.mtld.service.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.scoring.MTLDToCMRatioScoringCriteria;
import org.tfa.mtld.scoring.MinimizeTravelDistanceScoringCriteria;
import org.tfa.mtld.scoring.ScoringCriteriaStrategy;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.CriteriaCategoryBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.bean.SchoolBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

@Service
public class CohortServiceImpl implements CohortService {

	Logger logger = Logger.getLogger(CohortServiceImpl.class);

	@Autowired
	MtldCmRepository mtldCmRepository;
	
	
	/**
	 * Create cohort the against an region . If Region already have any existing
	 * cohort then it will show the existing, as well as newly created cohort
	 * based on the criteria selected by user.
	 * 
	 * @param criteriFormBean
	 *            The criteriaBeanList is being selected by user.
	 * @param user
	 *            object from session
	 * 
	 * 
	 * @return Map with contain the cohort info,MTLD info,corps member info
	 */
	@Override
	public Map<String, List<?>> createGroup(CriteriaFormBean criteriaFormBean,
			User sessionUser) throws Exception {
		logger.debug("Inside method createGroup");

		List<CorpsMember> corps = null;

		List<CorpsMember> unhierdcorps = null;
		List<MTLD> mtlds = null;

		Map<String, List<?>> cohorts = null;
		int regionId = sessionUser.getRegion().getRegionId();

		try {
			// Get the all the MTLD ,Hired CM and Unhired CM
			if (criteriaFormBean.getIncludeUnhiredCM()) {

				/*
				 * Will return list of CM including hired and unhired, and only
				 * unassigned, list will not include any assigned cm which are
				 * there in finalize cohort(s).
				 */
				corps = mtldCmRepository.getCorpMemberListByRegionId(regionId);
				logger.debug("Size of Corpsmember for both type" + corps != null ? corps
						.size() : corps);
			} else {
				/*
				 * Will return list of CM including only hired, and only
				 * unassigned. List will not include any assigned cm which are
				 * there in finalize cohort(s).
				 */
				corps = mtldCmRepository
						.getHiredCorpMemberListByRegionId(regionId);
				logger.debug("Size of Corpsmember for hired type" + corps != null ? corps
						.size() : corps);
				/*
				 * Will return list of CM including unhired, and only unassigned
				 * this will not include any assigned cm which are there in
				 * finalize cohort(s).
				 */
				unhierdcorps = mtldCmRepository
						.getUnhiredCorpMemberListByRegionId(regionId);
				logger.debug("Size of Corpsmember for unhired type"
						+ unhierdcorps != null ? unhierdcorps.size()
						: unhierdcorps);
			}

			logger.debug("List of MTLD Member");
			// return all the MTLD for login user's Region
			mtlds = mtldCmRepository.getUnassignedMTLDListByRegionId(regionId);

			logger.debug("Size of MTLD for a region" + mtlds != null ? mtlds
					.size() : mtlds);

			cohorts = cohortListFormation(sessionUser, mtlds, corps,
					unhierdcorps, criteriaFormBean);

			logger.debug("End of the method createGroup");
		} catch (Exception e) {
			logger.error("Exception at createGroup", e);
			throw e;
		}
		return cohorts;
	}

	/**
	 * 
	 * @param sessionUser
	 * @param mtlds
	 * @param corps
	 * @param unhierdcorps
	 * @param criteriaFormBean
	 * @throws Exception
	 */
	private Map<String, List<?>> cohortListFormation(User sessionUser,
			List<MTLD> mtlds, List<CorpsMember> corps,
			List<CorpsMember> unhierdcorps, CriteriaFormBean criteriaFormBean)
			throws Exception {
		int regionId = sessionUser.getRegion().getRegionId();
		Integer finalizeCohortCount = 0;
		int numberOfCorpsInCohort = 0;
		Map<String, List<?>> cohorts = new HashMap<String, List<?>>();
		List<Cohort> cohortList = new ArrayList<Cohort>();

		try {

			logger.debug("fetch the exiting cohort which are finalize for the region");
			List<Cohort> existingCohortList = mtldCmRepository
					.getCohortListByRegionId(regionId);
			if (existingCohortList != null) {
				finalizeCohortCount = existingCohortList.size();
			}

			int unfinalizeCohortCount = criteriaFormBean.getCohortCount()
					- finalizeCohortCount;
			// seeded the cohort with mtld or cm based on criteria
			if (criteriaFormBean.getIsPriorityForAllTheCriteriaIsNotZero()) {

				/**
				 * Each cohort starts out empty, with no corps members or MTLD
				 * in it. We need to seed each cohort with a single corps member
				 */

				if (corps != null && !corps.isEmpty()
						&& unfinalizeCohortCount > 0) {
					
					int corpsSize = corps.size();
					logger.debug("Seeded the Cohort based on the flag isgroupSeededWithMTLD = "
							+ criteriaFormBean.getIncludeMTLD());
					seedCohortList(criteriaFormBean, unfinalizeCohortCount,
							cohortList, mtlds, corps);

					logger.debug("Assigned corpsMember To Cohort");

					assignPooledCorpsMemberToCohort(corps, cohortList,
							unfinalizeCohortCount,corpsSize, criteriaFormBean);

				}

				/**
				 * • For each MTLD, calculate the score against each cohort
				 * (using the scoreMtldToCohort method to determine each score).
				 * • Assign MTLDs to cohorts so that we get the highest matches
				 * possible across all cohorts. • We display the completed
				 * MTLD/cohort assigments to the user. The user has an
				 * opportunity to make manual adjustments to the assignments,
				 * and then save the results.
				 */
				if (criteriaFormBean.getIncludeMTLD()) {
					assignMTLDToCohort(cohortList, mtlds, criteriaFormBean);
					/*
					 * Calculate the score for the cohort after final assignment
					 */
					scoreCohortList(cohortList, criteriaFormBean);
				} else {
					/*
					 * Calculate the score for the cohort after final assignment
					 */

					scoreCohortList(cohortList, criteriaFormBean);

				}

				cohortList = saveCohortList(cohortList, sessionUser, cohorts);

			}
			if (existingCohortList != null) {
				cohortList.addAll(existingCohortList);
			}
			List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
			List<String> cohortIDs = new ArrayList<String>();
			CohortBean cohortBean = null;
			int cohortSize = cohortList.size();
			int i = 0;
			while (i < cohortSize) {
				Cohort cohort = cohortList.get(i);
				if (null != cohort.getCohortDetails()) {
					numberOfCorpsInCohort += cohort.getCohortDetails().size();
				}
				cohortIDs.add("" + cohort.getId());
				cohortBean = new CohortBean();
				cohortBean = convertCohortToCohortBean(cohort, cohortBean);
				cohortBeans.add(cohortBean);
				i++;
			}
			cohorts.put(TFAConstants.COHORT_ID_LIST, cohortIDs);
			cohorts.put(TFAConstants.COHORT_LIST, cohortBeans);
			logger.debug("Convert Entity to Bean for UI representation");
			convertEntityToBean(cohorts, mtlds, corps, unhierdcorps);

			// Store the number of corps member participate in cohort in first
			// index
			List<Integer> corpsMemberListSize = new ArrayList<Integer>();
			corpsMemberListSize.add(numberOfCorpsInCohort);
			cohorts.put(TFAConstants.CORPS_MEMBERS_COUNT, corpsMemberListSize);
			// store the number zero in second index if all the cohort count
			// passed by the UI is already finalize
			if (unfinalizeCohortCount < finalizeCohortCount
					&& corpsMemberListSize != null) {
				corpsMemberListSize.add(0);
			}
			cohorts.put(TFAConstants.CORPS_MEMBERS_COUNT, corpsMemberListSize);

		} catch (Exception e) {
			logger.error("Exception at cohortListFormation", e);
			throw e;
		}
		return cohorts;
	}

	/*
 * 
 */
	private List<Cohort> saveCohortList(List<Cohort> cohortList,
			User sessionUser, Map<String, List<?>> cohorts) throws Exception {

		List<Cohort> cohortListToSave = new ArrayList<Cohort>();
		int corpsMemberSizeForRegion = 0;
		try {
			Collections.reverse(cohortList);
			int i = 0;
			int cohortListSize = cohortList.size();
			while (i < cohortListSize) {
				Date todayDate = new Date();

				Cohort cohort = cohortList.get(i);
				if (cohort.getMtld() == null && cohort.getCorpsMember() == null) {
					cohortList.remove(i);
					cohortListSize--;
					continue;
				}
				if (cohort != null && cohort.getCohortDetails() != null) {
					corpsMemberSizeForRegion = corpsMemberSizeForRegion
							+ cohort.getCohortDetails().size();
				}

				if (cohort.getIsFinalCohort() != null
						&& !cohort.getIsFinalCohort()) {
					calculateCohortDetails(cohort);
				}

				cohort.setCreatedBy(sessionUser.getLoginId());
				cohort.setCreatedDate(todayDate);
				cohort.setRegion(sessionUser.getRegion());

				cohortListToSave.add(cohort);

				i++;
			}
			mtldCmRepository.saveCohortList(cohortListToSave);
		} catch (Exception e) {
			logger.error("Exception at saveCohortList", e);
			throw e;
		}

		return cohortListToSave;

	}

	/**
	 * 'Convert Entity to Bean for UI representation
	 * 
	 * @param cohortList
	 * @param sessionUser
	 * @param cohorts
	 * @param mtlds
	 * @param unhierdcorps
	 * @param unhierdcorps2
	 */
	private void convertEntityToBean(Map<String, List<?>> cohorts,
			List<MTLD> mtlds, List<CorpsMember> corps,
			List<CorpsMember> unhierdcorps) throws Exception {
		logger.debug("Start of  of Converting the entity to Bean");

		MTLDBean mtldBean = null;
		CMBean corpsMemberBean = null;
		List<CMBean> corpsMemberList = new ArrayList<CMBean>();

		List<CMBean> unhierdCM = new ArrayList<CMBean>();
		List<MTLDBean> mtldBeans = new ArrayList<MTLDBean>();

		try {

			if (corps != null) {
				for (CorpsMember corp : corps) {
					corpsMemberBean = new CMBean();
					if (corp.getSchool() != null) {
						corpsMemberBean = convertCorpsMemberToCMBean(
								corpsMemberBean, corp);
						corpsMemberList.add(corpsMemberBean);
					} else {
						if (unhierdcorps == null) {
							unhierdcorps = new ArrayList<CorpsMember>();
						}
						unhierdcorps.add(corp);
						continue;
					}

				}
			}
			cohorts.put(TFAConstants.HIRED_CORPS_MEMBER, corpsMemberList);

			// convert unhired corp to CMBen
			if (unhierdcorps != null) {
				for (CorpsMember unHiredCorp : unhierdcorps) {
					corpsMemberBean = new CMBean();
					corpsMemberBean = convertCorpsMemberToCMBean(
							corpsMemberBean, unHiredCorp);
					unhierdCM.add(corpsMemberBean);
				}
			}
			cohorts.put(TFAConstants.UNHIRED_CORPS_MEMBER, unhierdCM);
			if (mtlds != null) {
				for (MTLD mtld : mtlds) {
					mtldBean = new MTLDBean();
					BeanUtils.copyProperties(mtldBean, mtld);
					mtldBean.setCorpsRegionName(mtld.getCorpsRegion() != null ? mtld
							.getCorpsRegion().getRegionName() : "");
					mtldBean.setCorpsSchoolDistrict(mtld.getCorpsSchool() != null ? mtld
							.getCorpsSchool().getDistrict() : "");
					mtldBeans.add(mtldBean);
				}
			}
			cohorts.put(TFAConstants.MTLD_LIST, mtldBeans);

		} catch (Exception e) {
			logger.error("Exception at convertEntityToBean", e);
			throw e;
		}
		logger.debug("End of  of Converting the entity to Bean");

	}

	/**
	 * Assigned pooled corpsMember To Cohort
	 * 
	 * @param corps
	 * @param cohortList
	 * @param unfinalizeCohortCount
	 * @param corpsSize
	 * @param criteriaFormBean
	 */

	private void assignPooledCorpsMemberToCohort(List<CorpsMember> corps,
			List<Cohort> cohortList, int unfinalizeCohortCount,int corpsSize,
			CriteriaFormBean criteriaFormBean) {
		
		int numberOfCMperCohort = (int) (Math.ceil(corpsSize / unfinalizeCohortCount)+1);

		int numberOfCorpsFitForCohort = (int) Math
				.floor((corps.size() * 10 / 100));
		Boolean isBalanceGroup = Boolean.TRUE;
		logger.debug("Start of the assignPooledCorpsMemberToCohort");
		while (corps != null && !corps.isEmpty()) {
			// scope changes
			Cohort tempNextCohort = null;
			CohortDetail cohortDetail = null;
			Cohort nextCohort = null;

			// calculate cohort score for finding the next cohort
			scoreCohortList(cohortList, criteriaFormBean);

			Iterator<Cohort> iterator = cohortList.iterator();
			while (nextCohort == null && iterator.hasNext()) {

				tempNextCohort = iterator.next();
				if (isBalanceGroup && null != tempNextCohort.getMtld()) {

					nextCohort = tempNextCohort;
					iterator.remove();
				} else {
					if (tempNextCohort.getCohortDetails() != null
							&& tempNextCohort.getCohortDetails().size() < numberOfCMperCohort) {
						isBalanceGroup = Boolean.TRUE;
						nextCohort = tempNextCohort;
						iterator.remove();
					}
				}
			}

			if (nextCohort == null) {
				break;
			}

			else {

				// calculate the score for all the Corpsmember which are in the
				// pool
				calculateTheScoreForPooledCorpsMember(nextCohort, corps,
						criteriaFormBean);
				CorpsMember corpsMember = null;
				if (corps != null && !corps.isEmpty()) {
					corpsMember = corps.get(corps.size() - 1);
					corpsMember = findCoprsForNextCohort(corpsMember, corps,
							cohortList, criteriaFormBean, numberOfCMperCohort);

				}

				if (corps != null && !corps.isEmpty()) {

					cohortDetail = new CohortDetail();
					cohortDetail.setCorpMember(corpsMember);
					if (isBalanceGroup && nextCohort.getMtld() != null) {
						cohortDetail.setFirstSeedingCorpsmember('Y');
						nextCohort.setCorpsMember(corpsMember);
						nextCohort.setMtld(null);
					} else {
						cohortDetail.setFirstSeedingCorpsmember('N');
					}
					if (corps != null && !corps.isEmpty()
							&& corps.size() < numberOfCorpsFitForCohort) {
						cohortDetail.setNotFitForCohort(true);
					}

					nextCohort.addCohortDetail(cohortDetail);
					corps.remove(corpsMember);
				}

				if (nextCohort.getCohortDetails() != null
						&& nextCohort.getCohortDetails().size() >= numberOfCMperCohort) {

					isBalanceGroup = Boolean.FALSE;

				}

				cohortList.add(nextCohort);

			}

		}

		logger.debug("Start of the assignPooledCorpsMemberToCohort");

	}

	/**
	 * 
	 * @param corpsMember
	 * @param corps
	 * @param cohortList
	 * @param criteriaBeanList
	 * @param numberOfCMperCohort
	 * @return
	 */
	private CorpsMember findCoprsForNextCohort(CorpsMember corpsMember,
			List<CorpsMember> corps, List<Cohort> cohortList,
			CriteriaFormBean criteriaFormBean, int numberOfCMperCohort) {

		// checking this corps is the most eligible corps for the
		// nextcohort

		int count = corps.size() - 2;
		int i = 0;
		int criteriaCategoryListSize = criteriaFormBean
				.getCriteriaCategoryBeanList().size();

		while (i < cohortList.size()) {
			int j = 0;
			Cohort cohort = cohortList.get(i);

			double cmTotalScore = 0.0;
			while (j < criteriaCategoryListSize) {
				int k = 0;
				int criteriaBeanSize = criteriaFormBean
						.getCriteriaCategoryBeanList().get(j)
						.getCriteriaBeans().size();
				while (k < criteriaBeanSize) {
					CriteriaBean criteriaBean = criteriaFormBean
							.getCriteriaCategoryBeanList().get(j)
							.getCriteriaBeans().get(k);

					if (criteriaBean.getScoringCriteriaStrategy() != null) {
						cmTotalScore += getCriteriaScore(cohort, corpsMember,
								criteriaBean) * criteriaBean.getPriorityValue();

					}
					k++;
				}
				j++;
			}
			if (cmTotalScore > corpsMember.getTotalScore()
					&& cohort.getCohortDetails().size() < numberOfCMperCohort) {

				count--;

				if (count < 0) {
					break;
				}

				corpsMember = corps.get(count);

			}

			i++;

		}

		return corpsMember;

	}

	/**
	 * 
	 * @param nextCohort
	 * @param corps
	 * @param criteriaBeanList
	 */
	private void calculateTheScoreForPooledCorpsMember(Cohort nextCohort,
			List<CorpsMember> corps, CriteriaFormBean criteriaFormBean) {

		int i = 0;
		int corpsSize = corps.size();
		int criteriaCategoryListSize = criteriaFormBean
				.getCriteriaCategoryBeanList().size();

		while (i < corpsSize) {
			int j = 0;
			CorpsMember corpsMember = corps.get(i);

			double totalCriteriaScore = 0;
			// find the score of the corps
			while (j < criteriaCategoryListSize) {
				int k = 0;
				int criteriaBeanSize = criteriaFormBean
						.getCriteriaCategoryBeanList().get(j)
						.getCriteriaBeans().size();
				while (k < criteriaBeanSize) {
					CriteriaBean criteriaBean = criteriaFormBean
							.getCriteriaCategoryBeanList().get(j)
							.getCriteriaBeans().get(k);
					double criteriaScore = 0;
					if (criteriaBean.getScoringCriteriaStrategy() != null) {

						criteriaScore = getCriteriaScore(nextCohort,
								corpsMember, criteriaBean);

					}

					totalCriteriaScore = totalCriteriaScore
							+ (criteriaScore * criteriaBean.getPriorityValue());
					k++;
				}
				j++;
			}

			corpsMember.setTotalScore(totalCriteriaScore);

			i++;

		}
		Collections.sort(corps, new CorpsMember());

	}

	private double getCriteriaScore(

	Cohort nextCohort, CorpsMember corpsMember, CriteriaBean criteriaBean) {
		double criteriaScore = 0;
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;
		/*
		 * if corpmember is hired then only
		 * MinimizeTravelDistanceScoringCriteria
		 * MATCHNEIGHBORHOODSCORINGCRITERIA FEEDERPATTERNSCORINGCRITERIA
		 * CHARTERNETWORKSCORINGCRITERIA MTLDDISTRICTSCORINGCRITERIA scoring
		 * will called otherwise skip it
		 */

		scoringCriteriaStrategy = criteriaBean.getScoringCriteriaStrategy();

		boolean flag = TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
				.equalsIgnoreCase(criteriaBean.getClassAPI())
				|| TFAConstants.MATCHNEIGHBORHOODSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
				|| TFAConstants.FEEDERPATTERNSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
				|| TFAConstants.MTLDDISTRICTSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim());

		if (flag && corpsMember.getSchool() != null) {
			criteriaScore = scoreCriteria(nextCohort, corpsMember,
					scoringCriteriaStrategy, criteriaBean);

		} else if (!flag) {
			criteriaScore = scoreCriteria(nextCohort, corpsMember,
					scoringCriteriaStrategy, criteriaBean);
		}

		return criteriaScore;
	}

	/**
	 * 
	 * 
	 * • For each MTLD, calculate the score against each cohort (using the
	 * scoreMtldToCohort method to determine each score). • Assign * MTLDs to
	 * cohorts so that we get the highest matches possible across all cohorts. •
	 * We display the completed MTLD/cohort assigments to the user. The user has
	 * an opportunity to make manual adjustments to the assignments, and then
	 * save the results.
	 * 
	 * 
	 * @param cohortList
	 * @param mtlds
	 * @param criteriaBeanList
	 */
	private void assignMTLDToCohort(List<Cohort> cohortList, List<MTLD> mtlds,
			CriteriaFormBean criteriaFormBean) {

		for (Cohort cohort : cohortList) {

			if (!cohort.getIsFinalCohort()) {
				cohort.setMtld(null);

				double scoreMTLDToCohort = 0.0;

				for (MTLD mtld : mtlds) {
					int criteriaCategoryListSize = criteriaFormBean
							.getCriteriaCategoryBeanList().size();

					int j = 0;
					;
					while (j < criteriaCategoryListSize) {

						int criteriaBeanSize = criteriaFormBean
								.getCriteriaCategoryBeanList().get(j)
								.getCriteriaBeans().size();
						int k = 0;
						while (k < criteriaBeanSize) {
							CriteriaBean criteriaBean = criteriaFormBean
									.getCriteriaCategoryBeanList().get(j)
									.getCriteriaBeans().get(k);
							if (criteriaBean.getScoringCriteriaStrategy() != null) {
								double criteriaScore = criteriaBean
										.getScoringCriteriaStrategy()
										.scoreMtldToCohort(mtld, cohort);

								scoreMTLDToCohort = scoreMTLDToCohort
										+ criteriaScore;
							}

							k++;
						}
						j++;
					}
					mtld.setScoreMTLDToCohort(scoreMTLDToCohort);
				}

				Collections.sort(mtlds, new MTLD());
				if (mtlds != null && !mtlds.isEmpty()) {
					cohort.setMtld(mtlds.get(mtlds.size() - 1));
					cohort.setCorpsMember(null);
					mtlds.remove(mtlds.get(mtlds.size() - 1));

				}

			}

		}

	}

	/**
	 * Each cohort starts out empty, with no corps members or MTLD in it. We
	 * need to seed each cohort with a single corps member
	 * 
	 * @param unfinalizeCohortCount
	 * 
	 * @param criteriaBeanList
	 * 
	 * @param cohortList
	 * @param mtlds
	 * @param corps
	 */
	private void seedCohortList(CriteriaFormBean criteriaFormBean,
			int unfinalizeCohortCount, List<Cohort> cohortList,
			List<MTLD> mtlds, List<CorpsMember> corps) {
		logger.debug("Start of the seedCohortList");
		int count = 0;
		while (unfinalizeCohortCount > 0) {
			Cohort tempCohort = null;
			CorpsMember corpsMember = null;
			Random random = new Random();
			CohortDetail cohortDetail = null;

			if (criteriaFormBean.getIncludeMTLD() && mtlds != null
					&& !mtlds.isEmpty() && count < mtlds.size()) {
				tempCohort = new Cohort();
				tempCohort.setMtld(mtlds.get(count));
				tempCohort.setIsFinalCohort(false);
				tempCohort.setCohortDetails(new ArrayList<CohortDetail>());
				count++;
				cohortList.add(tempCohort);
				unfinalizeCohortCount--;
			} else {

				tempCohort = new Cohort();
				if (corps != null && !corps.isEmpty()) {

					if (cohortList != null && !cohortList.isEmpty()) {
						corpsMember = findCorpsmemberForCohort(random,
								criteriaFormBean, cohortList, corps);
					}

					else {
						corpsMember = corps.get(random.nextInt(corps.size()));
					}

					tempCohort.setCorpsMember(corpsMember);
					cohortDetail = new CohortDetail();
					cohortDetail.setCorpMember(corpsMember);
					cohortDetail.setFirstSeedingCorpsmember('Y');
					corps.remove(corpsMember);
					tempCohort.addCohortDetail(cohortDetail);
					tempCohort.setIsFinalCohort(false);
					cohortList.add(tempCohort);
				}
				unfinalizeCohortCount--;

			}
		}// while loop end

		logger.debug("End of the seedCohortList");

	}

	/**
	 * 
	 * @param random
	 * @param criteriaBeanList
	 * @param cohortList
	 * @param corps
	 * @param corpsMember
	 * @return
	 */
	private CorpsMember findCorpsmemberForCohort(Random random,
			CriteriaFormBean criteriaFormBean, List<Cohort> cohortList,
			List<CorpsMember> corps) {
		CorpsMember cmToAdd = null;

		double cmToAddScore = 999999; // Start with a score way higher than
		// anything we'll actually get.

		for (CorpsMember cm : corps) {
			double cmTotalScore = 0;
			int cohortListSize = cohortList.size();
			int i = 0;
			while (i < cohortListSize) {
				Cohort cohort = cohortList.get(i);
				int j = 0;
				int criteriaCategoryListSize = criteriaFormBean
						.getCriteriaCategoryBeanList().size();
				while (j < criteriaCategoryListSize) {
					int k = 0;
					int criteriaBeanSize = criteriaFormBean
							.getCriteriaCategoryBeanList().get(j)
							.getCriteriaBeans().size();
					while (k < criteriaBeanSize) {
						CriteriaBean criteriaBean = criteriaFormBean
								.getCriteriaCategoryBeanList().get(j)
								.getCriteriaBeans().get(k);

						if (criteriaBean.getScoringCriteriaStrategy() != null) {

							cmTotalScore += getCriteriaScore(cohort, cm,
									criteriaBean)
									* criteriaBean.getPriorityValue();

						}
						k++;
					}
					j++;
				}
				i++;
			}

			if (cmTotalScore < cmToAddScore
					|| (cmTotalScore == cmToAddScore && random.nextBoolean())

			) {
				cmToAdd = cm;
				cmToAddScore = cmTotalScore;
			}

		}

		return cmToAdd;
	}

	/**
	 * 
	 * @param corpsMemberBean
	 * @param corp
	 * @return
	 * @throws Exception
	 */
	private CMBean convertCorpsMemberToCMBean(CMBean corpsMemberBean,
			CorpsMember corp) throws Exception {

		SchoolBean schoolBean = new SchoolBean();
		if (corp != null && corpsMemberBean != null) {
			BeanUtils.copyProperties(corpsMemberBean, corp);
			if (corp.getSchool() != null) {
				BeanUtils.copyProperties(schoolBean, corp.getSchool());
				corpsMemberBean.setSchoolBean(schoolBean);
				corpsMemberBean.setIsHired(Boolean.TRUE);
			}
			if (corp.getCorpsYear() != null
					&& CriteriaScoringUtils.CURRENTCORPYEAR == corp
							.getCorpsYear()) {
				corpsMemberBean.setCmYear(TFAConstants.CM_MTLD_ONE_YEAR);
			} else {
				corpsMemberBean.setCmYear(TFAConstants.CM_MTLD_TWO_YEAR);
			}
		}

		return corpsMemberBean;
	}

	/**
	 * Calculate the score for the cohort after final assignment
	 * 
	 * @param cohortList
	 * @param criteriaBeanList
	 */
	@Override
	public void scoreCohortList(List<Cohort> cohortList,
			CriteriaFormBean criteriaFormBean) {

		int i = 0;
		int cohortSize = cohortList.size();
		while (i < cohortSize) {

			Cohort cohort = cohortList.get(i);

			if (cohort != null && cohort.getIsFinalCohort() != null
					&& !cohort.getIsFinalCohort()) {
				calculateAssignedCorpsMemberScoreOFCohort(cohort,
						criteriaFormBean);

			}

			i++;
		}
		Collections.sort(cohortList, new Cohort());
	}

	/**
	 * 
	 * @param cohort
	 * @param corps
	 * @param criteriaBeanList
	 */

	private void calculateAssignedCorpsMemberScoreOFCohort(Cohort cohort,
			CriteriaFormBean criteriaFormBean) {

		double cohortScore = 0;
		Boolean isOverDistance = Boolean.FALSE;
		int criteriaCategoryListSize = criteriaFormBean
				.getCriteriaCategoryBeanList().size();
		if (cohort.getCohortDetails() != null) {
			int i = 0;
			int cohortDetailSize = cohort.getCohortDetails().size();

			while (i < cohortDetailSize) {
				int j = 0;
				CohortDetail cohDetail = cohort.getCohortDetails().get(i);

				String criteriaScoreForCohortDetail = "";

				double totalCriteriaScore = 0;
				// Calculate the score of the corps which are in the cohort
				while (j < criteriaCategoryListSize) {

					int criteriaBeanSize = criteriaFormBean
							.getCriteriaCategoryBeanList().get(j)
							.getCriteriaBeans().size();
					int k = 0;
					while (k < criteriaBeanSize) {

						CriteriaBean criteriaBean = criteriaFormBean
								.getCriteriaCategoryBeanList().get(j)
								.getCriteriaBeans().get(k);
						double criteriaScore = 0;

						if (criteriaBean.getScoringCriteriaStrategy() != null) {

							criteriaScore = getCriteriaScore(cohort,
									cohDetail.getCorpMember(), criteriaBean);

							if (!isOverDistance
									&& TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
											.equalsIgnoreCase(criteriaBean
													.getClassAPI())
									&& cohort.getCohortDetails().size() > 1
									&& criteriaScore < 1) {
								isOverDistance = Boolean.TRUE;
								cohort.setOverDistance(Boolean.TRUE);
							}

						}

						totalCriteriaScore = totalCriteriaScore
								+ (criteriaScore * criteriaBean
										.getPriorityValue());
						// need to change
						if (criteriaScoreForCohortDetail.isEmpty()) {
							criteriaScoreForCohortDetail = criteriaScoreForCohortDetail
									+ criteriaBean.getId()
									+ "="
									+ criteriaScore;

						} else {
							criteriaScoreForCohortDetail = criteriaScoreForCohortDetail
									+ ","
									+ criteriaBean.getId()
									+ "="
									+ criteriaScore;
						}

						cohortScore = cohortScore
								+ cohDetail.getCorpMember().getTotalScore();

						k++;
					}
					j++;
				}

				cohDetail.getCorpMember().setTotalScore(totalCriteriaScore);
				cohDetail.setCriteriaScore(criteriaScoreForCohortDetail);

				i++;

			}
		}

		cohort.setCohortScore(cohortScore);

	}

	private double scoreCriteria(Cohort cohort, CorpsMember corpsMember,
			ScoringCriteriaStrategy scoringCriteriaStrategy,
			CriteriaBean criteriaBean) {

		double criteriaScore = 0;

		// When set of corpsmember in the cohort is zero and first seeded member
		// to the cohort is MTLD
		if (cohort != null && cohort.getMtld() != null
				&& cohort.getCohortDetails() != null
				&& cohort.getCohortDetails().isEmpty()) {
			criteriaScore = scoringCriteriaStrategy.scoreMtldToCorpsMember(
					cohort.getMtld(), corpsMember);
		}

		else if (CriteriaScoringUtils.cohortSizeWithoutCm(cohort, corpsMember) == 0) { // Don't
			// score
			// a
			// corps
			// member
			// against
			// themselves
			return criteriaScore;
		}

		// When set of corpsmember in the cohort is zero or greater than zero

		else {
			criteriaScore = scoringCriteriaStrategy.scoreCorpsMemberToCohort(
					corpsMember, cohort);
		}

		return criteriaScore;
	}

	public CohortBean convertCohortToCohortBean(Cohort cohort,
			CohortBean cohortBean) throws Exception {

		MTLDBean mtldBean = null;
		CMBean corpsMemberBean = null;
		CohortDetailBean cohortDetailBean = null;

		// Copy attribute from cohort to cohortBean
		List<CohortDetailBean> cohortDetailBeanList = new ArrayList<CohortDetailBean>();
		if (cohort != null && cohort.getMtld() != null) {
			mtldBean = new MTLDBean();
			BeanUtils.copyProperties(mtldBean, cohort.getMtld());
			mtldBean.setCorpsRegionName(cohort.getMtld().getCorpsRegion() != null ? cohort
					.getMtld().getCorpsRegion().getRegionName()
					: "");

			mtldBean.setCorpsSchoolDistrict(cohort.getMtld().getCorpsSchool() != null ? cohort
					.getMtld().getCorpsSchool().getDistrict()
					: "");
			cohortBean.setSeededMtldBean(mtldBean);
		} else {
			if (cohort.getCorpsMember() != null) {

				corpsMemberBean = new CMBean();

				corpsMemberBean = convertCorpsMemberToCMBean(corpsMemberBean,
						cohort.getCorpsMember());

				cohortBean.setSeededCMBean(corpsMemberBean);
			}
		}
		if (null != cohort.getCohortDetails()
				&& !cohort.getCohortDetails().isEmpty()) {
			for (CohortDetail tempCohortDetail : cohort.getCohortDetails()) {
				corpsMemberBean = new CMBean();
				cohortDetailBean = new CohortDetailBean();

				BeanUtils.copyProperties(cohortDetailBean, tempCohortDetail);

				corpsMemberBean = convertCorpsMemberToCMBean(corpsMemberBean,
						tempCohortDetail.getCorpMember());

				if (tempCohortDetail != null
						&& tempCohortDetail.getCorpMember() != null
						&& tempCohortDetail.getCorpMember().getCorpsYear() != null) {

					if (tempCohortDetail.getCorpMember().getCorpsYear() != null
							&& CriteriaScoringUtils.CURRENTCORPYEAR.intValue() == tempCohortDetail
									.getCorpMember().getCorpsYear().intValue()) {
						corpsMemberBean
								.setCmYear(TFAConstants.CM_MTLD_ONE_YEAR);
					} else {
						corpsMemberBean
								.setCmYear(TFAConstants.CM_MTLD_TWO_YEAR);
					}
				}

				cohortDetailBean.setCorpsMember(corpsMemberBean);

				cohortDetailBeanList.add(cohortDetailBean);

			}
			cohortBean.setCohortDetailBean(cohortDetailBeanList);
		} else {
			cohortBean.setCohortDetailBean(new ArrayList<CohortDetailBean>());
		}

		BeanUtils.copyProperties(cohortBean, cohort);
		if (!cohortBean.getIsFinalCohort()) {
			createCriteriaScoreMap(cohortBean);
		}
		return cohortBean;

	}

	private void createCriteriaScoreMap(CohortBean cohortBean) {
		if (!cohortBean.getIsFinalCohort() && cohortBean != null
				&& cohortBean.getCohortDetailBean() != null) {

			for (CohortDetailBean cohortDetail : cohortBean
					.getCohortDetailBean()) {

				Map<Integer, Double> scorecriteriaMap = new HashMap<Integer, Double>();
				String[] criteriaScoreArray = cohortDetail.getCriteriaScore()
						.split(",");
				if (criteriaScoreArray.length > 0) {
					for (String scoreString : criteriaScoreArray) {
						String[] criteriaScore = scoreString.split("=");
						scorecriteriaMap.put(
								Integer.parseInt(criteriaScore[0]),
								Double.parseDouble(criteriaScore[1]));
					}
				}
				cohortDetail.getCorpsMember()
						.setCriteriaScore(scorecriteriaMap);

			}
		}

	}

	@Override
	public Map<String, String> getGroupDetails(int cohortId) throws Exception {
		Cohort cohort = mtldCmRepository.getCohort(cohortId);

		Map<String, String> groupDetails = new HashMap<String, String>();
		if (cohort != null) {

			groupDetails.put(TFAConstants.GROUP_SCHOOL_DISTRICT,
					cohort.getSchoolDistrictRepresented());
			groupDetails.put(TFAConstants.GROUP_SPED_PERCENTAGE,
					cohort.getSpedModifierPercentage());
			groupDetails.put(TFAConstants.GROUP_NEIGHBOURHOOD_REPRESENTED,
					cohort.getNeighbourhoodRepresented());
			groupDetails.put(TFAConstants.GROUP_ECE_PERCETAGE,
					cohort.getEcePercentage());
			groupDetails.put(TFAConstants.GROUP_ONE_YEAR_CORP_PERCENTAGE,
					cohort.getOneYearCorpPercentage());
			groupDetails.put(TFAConstants.GROUP_TWO_YEAR_CORP_PERCENTAGE,
					cohort.getTwoYearCorpPercentage());
			groupDetails.put(TFAConstants.GROUP_CHARTER_PARTNER_PERCENTAGE,
					cohort.getCharterPartnerPercentage());
			groupDetails.put(TFAConstants.GROUP_DISTRICT_PARTNER_PERCENTAGE,
					cohort.getDistrictPartnerPercentage());
			groupDetails.put(TFAConstants.GROUP_LOW_INCOME_PERCENTAGE,
					cohort.getLowIncomePercentage());
			groupDetails.put(TFAConstants.GROUP_FEEDER_PATTERN_HS,
					cohort.getFeederPatternHS());
		}
		return groupDetails;

	}

	@Override
	public void flushUnfinalizeCohort(Integer regionId) throws Exception {
		mtldCmRepository.flushUnfinalizeCohort(regionId);

	}

	/**
	 * This method calculates percentage of each selected criteria in order to
	 * show them on bar-chart.
	 * 
	 * @param criteriaBeanList
	 *            This list contains selected criteria's details.
	 * @param cohortMap
	 *            This contains all the cohort formed as a part of
	 *            criteria-matching algorithm.
	 * 
	 * @return cohortMap
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List<?>> calculatePercentageForCriteriaGraph(
			List<CMBean> corpList, CriteriaFormBean criteriaFormBean,
			Map<String, List<?>> cohortMap) throws Exception {
		logger.debug("Inside method calculatePercentageForCriteriaGraph");
		Map<String, Double> percentageMap = new LinkedHashMap<String, Double>();
		List<Map<String, Double>> mapList = new ArrayList<Map<String, Double>>();
		int criteriaPriorityCounter = 0;
		int i = 0, j = 0, k = 0;
		int criteriaCategoryListSize = criteriaFormBean
				.getCriteriaCategoryBeanList().size();

		int corpsListSize = corpList.size();

		Double overallScoresum = 0.0;
		while (i < criteriaCategoryListSize) {

			int criteriaBeanSize = criteriaFormBean
					.getCriteriaCategoryBeanList().get(i).getCriteriaBeans()
					.size();
			j = 0;
			while (j < criteriaBeanSize) {
				CriteriaBean criteriaBean = criteriaFormBean
						.getCriteriaCategoryBeanList().get(i)
						.getCriteriaBeans().get(j);
				Double criteriaScoreSum = 0.0;
				k = 0;
				while (k < corpsListSize) {
					CMBean cmBean = corpList.get(k);
					if (cmBean.getCriteriaScore() != null) {
						for (Entry entry : cmBean.getCriteriaScore().entrySet()) {
							if (Integer.parseInt(entry.getKey().toString()) == criteriaBean
									.getId()
									&& Double.parseDouble(entry.getValue()
											.toString()) > 0.0
									&& criteriaBean.getPriorityValue() > 0) {

								criteriaScoreSum = criteriaScoreSum
										+ ((Double.parseDouble(entry.getValue()
												.toString())));
							}
						}
					}
					k++;
				}
				if (criteriaBean.getPriorityValue() > 0) {
					criteriaPriorityCounter++;
				}
				// added by Lovely Ram
				if (!corpList.isEmpty()) {
					overallScoresum = overallScoresum
							+ Double.valueOf(new DecimalFormat("0.##")
									.format((criteriaScoreSum / corpList.size()) * 100));
					percentageMap
							.put(criteriaBean.getName(),
									Double.valueOf(new DecimalFormat("0.##")
											.format((criteriaScoreSum / corpList
													.size()) * 100)));
				} else {
					percentageMap.put(criteriaBean.getName(), 0.0);
				}
				j++;
			}
			i++;
		}
		if (criteriaPriorityCounter > 0) {
			percentageMap.put(TFAConstants.OVERALL_PROGRESS_KEY, Double
					.valueOf(new DecimalFormat("0.##").format(overallScoresum
							/ criteriaPriorityCounter)));
		} else {
			percentageMap.put(TFAConstants.OVERALL_PROGRESS_KEY, 0.0);
		}
		mapList.add(percentageMap);
		cohortMap.put(TFAConstants.CRITERIA_MAP_SCORE_KEY, mapList);
		logger.debug("End of the method calculatePercentageForCriteriaGraph");
		return cohortMap;
	}

	/**
	 * 
	 * @param cohortBeans
	 * @param criteriaMap
	 */
	public void calculateCriteriaCategoryPercentage(
			List<CohortBean> cohortBeans, CriteriaFormBean criteriaFormBean) {
		int j = 0;
		int cohortListSize = cohortBeans.size();
		while (j < cohortListSize) {
			CohortBean cohortBean = cohortBeans.get(j);
			double basicsCriteriaPer = 0;
			double contentCriteriaPer = 0;
			double geographicCriteriaPer = 0;
			double relationshipsCriteriaPer = 0;

			int basicsCriteriaListSize = 0;
			int contentCriteriaListSize = 0;
			int geographyCriteriaListSize = 0;
			int relationshipCriteriaListSize = 0;

			if (cohortBean.getCohortDetailBean().isEmpty()) {
				cohortBean.setBasicsCriteriaPer("0.0");
				cohortBean.setContentCriteriaPer("0.0");
				cohortBean.setGeographicCriteriaPer("0.0");
				cohortBean.setRelationshipsCriteriaPer("0.0");
			}

			if (!cohortBean.getIsFinalCohort()) {
				int i = 0;

				while (i < cohortBean.getCohortDetailBean().size()) {

					CohortDetailBean cohortDetailBean = cohortBean
							.getCohortDetailBean().get(i);

					for (CriteriaCategoryBean criteriaCategoryBean : criteriaFormBean
							.getCriteriaCategoryBeanList()) {
						if (TFAConstants.CRITERIA_CATEGORY_BASICS
								.equalsIgnoreCase(criteriaCategoryBean
										.getCategoryName())) {

							for (CriteriaBean criteriaBeanObj : criteriaCategoryBean
									.getCriteriaBeans()) {

								if (criteriaBeanObj.getPriorityValue() > 0
										&& cohortBean.getCohortDetailBean()
												.indexOf(cohortDetailBean) == 0) {
									basicsCriteriaListSize = basicsCriteriaListSize + 1;
								}

								basicsCriteriaPer = basicsCriteriaPer
										+ cohortDetailBean.getCorpsMember()
												.getCriteriaScore()
												.get(criteriaBeanObj.getId());

							}

						}

						else if (TFAConstants.CRITERIA_CATEGORY_CONTENT
								.equalsIgnoreCase(criteriaCategoryBean
										.getCategoryName())) {

							for (CriteriaBean criteriaBeanObj : criteriaCategoryBean
									.getCriteriaBeans()) {

								if (criteriaBeanObj.getPriorityValue() > 0
										&& cohortBean.getCohortDetailBean()
												.indexOf(cohortDetailBean) == 0) {
									contentCriteriaListSize = contentCriteriaListSize + 1;
								}

								contentCriteriaPer = contentCriteriaPer
										+ cohortDetailBean.getCorpsMember()
												.getCriteriaScore()
												.get(criteriaBeanObj.getId());

							}

						}

						else if (TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY
								.equalsIgnoreCase(criteriaCategoryBean
										.getCategoryName())) {

							for (CriteriaBean criteriaBeanObj : criteriaCategoryBean
									.getCriteriaBeans()) {

								if (criteriaBeanObj.getPriorityValue() > 0
										&& cohortBean.getCohortDetailBean()
												.indexOf(cohortDetailBean) == 0) {
									geographyCriteriaListSize = geographyCriteriaListSize + 1;
								}

								if (criteriaBeanObj.getPriorityValue() > 0
										&& criteriaBeanObj
												.getClassAPI()
												.equalsIgnoreCase(
														TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA)) {
									cohortBean.setMaxDistance(criteriaBeanObj
											.getFieldValue());
								}

								geographicCriteriaPer = geographicCriteriaPer
										+ cohortDetailBean.getCorpsMember()
												.getCriteriaScore()
												.get(criteriaBeanObj.getId());

							}

						}

						else if (TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS
								.equalsIgnoreCase(criteriaCategoryBean
										.getCategoryName())) {

							for (CriteriaBean criteriaBeanObj : criteriaCategoryBean
									.getCriteriaBeans()) {

								if (criteriaBeanObj.getPriorityValue() > 0
										&& cohortBean.getCohortDetailBean()
												.indexOf(cohortDetailBean) == 0) {
									relationshipCriteriaListSize = relationshipCriteriaListSize + 1;
								}

								relationshipsCriteriaPer = relationshipsCriteriaPer
										+ cohortDetailBean.getCorpsMember()
												.getCriteriaScore()
												.get(criteriaBeanObj.getId());

							}

						}

					}

					i++;

				}

			}

			if (cohortBean.getCohortDetailBean() != null
					&& cohortBean.getCohortDetailBean().size() > 0
					&& !cohortBean.getIsFinalCohort()) {

				int size = cohortBean.getCohortDetailBean().size();

				if (basicsCriteriaListSize > 0) {
					cohortBean
							.setBasicsCriteriaPer(Double
									.valueOf(
											new DecimalFormat("0.##")
													.format((basicsCriteriaPer / (size * basicsCriteriaListSize)) * 100))
									.toString());
				} else {
					cohortBean.setBasicsCriteriaPer("0.0");
				}
				if (contentCriteriaListSize > 0) {
					cohortBean
							.setContentCriteriaPer(Double
									.valueOf(
											new DecimalFormat("0.##")
													.format((contentCriteriaPer / (size * contentCriteriaListSize)) * 100))
									.toString());

				} else {
					cohortBean.setContentCriteriaPer("0.0");
				}
				if (geographyCriteriaListSize > 0) {
					cohortBean
							.setGeographicCriteriaPer(Double
									.valueOf(
											new DecimalFormat("0.##")
													.format((geographicCriteriaPer / (size * geographyCriteriaListSize)) * 100))
									.toString());
				} else {
					cohortBean.setGeographicCriteriaPer("0.0");
				}
				if (relationshipCriteriaListSize > 0) {
					cohortBean
							.setRelationshipsCriteriaPer(Double
									.valueOf(
											new DecimalFormat("0.##")
													.format((relationshipsCriteriaPer / (size * relationshipCriteriaListSize)) * 100))
									.toString());

				} else {
					cohortBean.setRelationshipsCriteriaPer("0.0");
				}
			}
			j++;
		}// cohort ended
	}

	public void calculateCohortDetails(Cohort cohort) throws Exception {
		if (cohort != null && cohort.getCohortDetails() != null) {
			if (!cohort.getCohortDetails().isEmpty()) {
				cohort.setSchoolDistrictRepresented(String
						.valueOf(calculateSchoolDistricts(cohort)));

				cohort.setSpedModifierPercentage(String
						.valueOf(calculateSPEDPercentage(cohort,
								TFAConstants.SUBJECT_MODIFIER)));
				cohort.setElemPercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_ELEM)));
				cohort.setMsGradePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_MS)));
				cohort.setHsGradePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_HIGH)));
				cohort.setEcePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_PREK)));

				cohort.setOneYearCorpPercentage(String
						.valueOf(calculateYearPercentage(cohort,
								CriteriaScoringUtils.CURRENTCORPYEAR,
								TFAConstants.FIRST_YEAR_CM)));
				cohort.setTwoYearCorpPercentage(String
						.valueOf(calculateYearPercentage(cohort,
								CriteriaScoringUtils.CURRENTCORPYEAR,
								TFAConstants.SECOND_YEAR_CM)));

				cohort.setCharterPartnerPercentage(String
						.valueOf(calculatePlacementPartnerPercentage(cohort,
								TFAConstants.PLACEMENT_PARTNER_CHARTER)));
				cohort.setDistrictPartnerPercentage(String
						.valueOf(calculatePlacementPartnerPercentage(cohort,
								TFAConstants.PLACEMENT_PARTNER_DISTRICT)));

				cohort.setLowIncomePercentage(String
						.valueOf(calcualtelowIncomeBackgroundPercentage(cohort)));

				cohort.setNeighbourhoodRepresented(String
						.valueOf(calcualteNeighborhoodRepresented(cohort)));

				cohort.setFeederPatternHS(calcualteFeederPatternHS(cohort));

				calculateSchoolsRepresented(cohort);
			} else {
				cohort.setSchoolDistrictRepresented("0");
				cohort.setSpedModifierPercentage("0.0");
				cohort.setElemPercentage("0.0");
				cohort.setMsGradePercentage("0.0");
				cohort.setHsGradePercentage("0.0");
				cohort.setEcePercentage("0.0");

				cohort.setOneYearCorpPercentage("0.0");
				cohort.setTwoYearCorpPercentage("0.0");

				cohort.setCharterPartnerPercentage("0.0");
				cohort.setDistrictPartnerPercentage("0.0");

				cohort.setLowIncomePercentage("0.0");

				cohort.setNeighbourhoodRepresented("");

				cohort.setFeederPatternHS("");
				cohort.setSchoolRep("0.0");
			}

		}

	}

	/**
	 * This method calculates no of distinct school in the passed cohort.
	 * 
	 * @param cohort
	 *            Cohort for which schoolRepresented needs to be calculated.
	 * 
	 * @return cohort
	 */
	public Cohort calculateSchoolsRepresented(Cohort cohort) throws Exception {

		School school = null;
		Map<School, Integer> schoolMap = new HashMap<School, Integer>();

		int i = 0;
		int cohortDetailSize = cohort.getCohortDetails().size();

		while (i < cohortDetailSize) {
			if (cohort.getCohortDetails().get(i).getCorpMember().getSchool() != null) {
				school = cohort.getCohortDetails().get(i).getCorpMember()
						.getSchool();
				if (!schoolMap.containsKey(school)) {
					schoolMap.put(school, 1);
				}
			}
			i++;
		}
		cohort.setSchoolRep(Integer.toString(schoolMap.size()));

		return cohort;
	}

	private int calculateSchoolDistricts(Cohort cohort) {

		Set<String> schoolDistricts = new HashSet<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			// Added By Lovely Ram
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getDistrict() != null
					&& cohortDetail.getCorpMember().getSchool().getDistrict()
							.length() > 0) {
				schoolDistricts.add(cohortDetail.getCorpMember().getSchool()
						.getDistrict());
			}
		}
		return schoolDistricts.size();

	}

	private Double calculatePercentage(Double count, int totalCorps) {
		Double percentage = 0.0;
		if (count > 0.0) {

			percentage = Double.valueOf(new DecimalFormat("0.##")
					.format((count / totalCorps) * 100));
		}

		return percentage;
	}

	private Double calculateSPEDPercentage(Cohort cohort, String modifierValue) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSubjectModifier() != null
					&& cohortDetail.getCorpMember().getSubjectModifier()
							.equalsIgnoreCase(modifierValue)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	private Double calculateGradeLevelPercentage(Cohort cohort,
			String gradeValue) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getGradeLevel() != null
					&& cohortDetail.getCorpMember().getGradeLevel()
							.equalsIgnoreCase(gradeValue)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	/*
	 * Due to value changes in data base.For first year cm corps year should be
	 * equals or grater then 2013 else it's second year cm.
	 */

	private Double calculateYearPercentage(Cohort cohort, Integer yearValue,
			String yearCalcualtion) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {

			if (yearCalcualtion.equalsIgnoreCase(TFAConstants.FIRST_YEAR_CM)
					&& cohortDetail.getCorpMember() != null
					&& null != cohortDetail.getCorpMember().getCorpsYear()
					&& cohortDetail.getCorpMember().getCorpsYear().intValue() == yearValue
							.intValue()) {

				count++;
			}
			if (yearCalcualtion.equalsIgnoreCase(TFAConstants.SECOND_YEAR_CM)
					&& cohortDetail.getCorpMember() != null
					&& null != cohortDetail.getCorpMember().getCorpsYear()
					&& cohortDetail.getCorpMember().getCorpsYear().intValue() < yearValue
							.intValue()) {
				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	private Double calculatePlacementPartnerPercentage(Cohort cohort,
			String partnerType) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getSchoolType() != null
					&& cohortDetail.getCorpMember().getSchool().getSchoolType()
							.equalsIgnoreCase(partnerType)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());
	}

	private Double calcualtelowIncomeBackgroundPercentage(Cohort cohort) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getIslowIncomeBackground() != null
					&& cohortDetail.getCorpMember().getIslowIncomeBackground()) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());
	}

	private String calcualteNeighborhoodRepresented(Cohort cohort) {

		List<String> neighborhoods = new ArrayList<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {

			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getNeighborhood() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getNeighborhood().length() > 0) {
				neighborhoods.add(cohortDetail.getCorpMember().getSchool()
						.getNeighborhood());
			}

		}

		int max = 0;
		int curr = 0;
		String maxNeighborhood = null;
		Set<String> unique = new HashSet<String>(neighborhoods);

		for (String key : unique) {
			curr = Collections.frequency(neighborhoods, key);

			if (max < curr) {
				max = curr;
				maxNeighborhood = key;
			}
		}

		return maxNeighborhood;

	}

	private String calcualteFeederPatternHS(Cohort cohort) {

		List<String> feederPatterns = new ArrayList<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getFeederPatternHS() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getFeederPatternHS().length() > 0) {
				feederPatterns.add(cohortDetail.getCorpMember().getSchool()
						.getFeederPatternHS());
			}

		}

		int max = 0;
		int curr = 0;
		String maxFeederPattern = null;
		Set<String> unique = new HashSet<String>(feederPatterns);

		for (String key : unique) {
			curr = Collections.frequency(feederPatterns, key);

			if (max < curr) {
				max = curr;
				maxFeederPattern = key;
			}
		}

		return maxFeederPattern;

	}

	public void populateCriteriaAPI(CriteriaFormBean criteriaFormBean)
			throws Exception {

		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (CriteriaCategoryBean criteriaCategoryBean : criteriaFormBean
				.getCriteriaCategoryBeanList()) {
			for (CriteriaBean criteriaBeanObj : criteriaCategoryBean
					.getCriteriaBeans()) {
				if (TFAConstants.CRITERIA_CATEGORY_BASICS
						.equalsIgnoreCase(criteriaCategoryBean
								.getCategoryName())) {

					scoringCriteriaStrategy = null;

					if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
							.equalsIgnoreCase(criteriaBeanObj.getClassAPI()
									.trim())) {
						if (criteriaBeanObj.getPriorityValue() != 0
								&& criteriaBeanObj.getFieldValue() != null
								&& !("0").equalsIgnoreCase(criteriaBeanObj
										.getFieldValue())) {
							scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
									Integer.parseInt(criteriaBeanObj
											.getFieldValue()));
						}

					} else if (criteriaBeanObj.getPriorityValue() != 0) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBeanObj.getClassAPI()
														.trim()).newInstance();
					}
					criteriaBeanObj
							.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				}

				else if (TFAConstants.CRITERIA_CATEGORY_CONTENT
						.equalsIgnoreCase(criteriaCategoryBean
								.getCategoryName())) {

					scoringCriteriaStrategy = null;

					if (criteriaBeanObj.getPriorityValue() != 0) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBeanObj.getClassAPI()
														.trim()).newInstance();
					}

					criteriaBeanObj
							.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				}

				else if (TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY
						.equalsIgnoreCase(criteriaCategoryBean
								.getCategoryName())) {

					scoringCriteriaStrategy = null;

					if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
							.equalsIgnoreCase(criteriaBeanObj.getClassAPI()
									.trim())) {

						if (criteriaBeanObj.getPriorityValue() != 0
								&& criteriaBeanObj.getFieldValue() != null
								&& !("0").equalsIgnoreCase(criteriaBeanObj
										.getFieldValue())) {
							scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
									Double.parseDouble(criteriaBeanObj
											.getFieldValue()));
						}
					} else if (criteriaBeanObj.getPriorityValue() != 0) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBeanObj.getClassAPI()
														.trim()).newInstance();
					}
					criteriaBeanObj
							.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				}

				else if (TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS
						.equalsIgnoreCase(criteriaCategoryBean
								.getCategoryName())) {

					scoringCriteriaStrategy = null;

					if (criteriaBeanObj.getPriorityValue() != 0) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBeanObj.getClassAPI()
														.trim()).newInstance();
					}

					criteriaBeanObj
							.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				}
			}

		}

	}

}
