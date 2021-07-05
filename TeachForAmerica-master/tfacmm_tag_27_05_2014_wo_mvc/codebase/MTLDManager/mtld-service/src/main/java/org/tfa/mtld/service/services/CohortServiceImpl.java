package org.tfa.mtld.service.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.scoring.ScoringCriteriaStrategy;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
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
	 * @param criteriaBeanList
	 *            The criteriaBeanList is being selected by user.
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
	@Override
	public Map<String, List<?>> createGroup(
			List<CriteriaBean> criteriaBeanList, int cohortCount,
			boolean isgroupSeededWithMTLD, boolean isUnhierdCMIncluded,
			Boolean isPriorityForAllTheCriteriaIsNotZero, User sessionUser)
			throws Exception {
		logger.debug("Inside method createGroup");

		List<CorpsMember> corps = null;

		List<CorpsMember> unhierdcorps = null;
		List<MTLD> mtlds = null;

		Map<String, List<?>> cohorts = null;
		int regionId = sessionUser.getRegion().getRegionId();

		try {
			// Get the all the MTLD ,Hired CM and Unhired CM
			if (isUnhierdCMIncluded) {

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
			mtlds = mtldCmRepository.getMTLDListByRegionId(regionId);

			logger.debug("Size of MTLD for a region" + mtlds != null ? mtlds
					.size() : mtlds);

			cohorts = cohortListFormation(sessionUser, cohortCount, mtlds,
					corps, unhierdcorps, criteriaBeanList,
					isPriorityForAllTheCriteriaIsNotZero, isgroupSeededWithMTLD);

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
	 * @param cohortCount
	 * @param mtlds
	 * @param corps
	 * @param unhierdcorps
	 * @param criteriaBeanList
	 * @param isPriorityForAllTheCriteriaIsNotZero
	 * @param isgroupSeededWithMTLD
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> cohortListFormation(User sessionUser,
			int cohortCount, List<MTLD> mtlds, List<CorpsMember> corps,
			List<CorpsMember> unhierdcorps,
			List<CriteriaBean> criteriaBeanList,
			Boolean isPriorityForAllTheCriteriaIsNotZero,
			boolean isgroupSeededWithMTLD) throws Exception {
		int regionId = sessionUser.getRegion().getRegionId();
		Integer count = 0;
		Map<String, List<?>> cohorts = new HashMap<String, List<?>>();
		List<Cohort> cohortList = new ArrayList<Cohort>();

		try {

			logger.debug("fetch the exiting cohort which are finalize for the region");
			count = populateCohortListWithExistingCohort(regionId, mtlds,
					corps, cohortList);
			cohortCount = cohortCount - count;
			// seeded the cohort with mtld or cm based on criteria
			if (isPriorityForAllTheCriteriaIsNotZero) {

				/**
				 * Each cohort starts out empty, with no corps members or MTLD
				 * in it. We need to seed each cohort with a single corps member
				 */

				if (corps != null && !corps.isEmpty() && cohortCount > 0) {
					logger.debug("Seeded the Cohort based on the flag isgroupSeededWithMTLD = "
							+ isgroupSeededWithMTLD);
					seedCohortList(criteriaBeanList, cohortList, mtlds, corps,
							isgroupSeededWithMTLD, cohortCount);

					logger.debug("Assigned corpsMember To Cohort");

					assignPooledCorpsMemberToCohort(corps, cohortList,
							cohortCount, criteriaBeanList);

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
				if (isgroupSeededWithMTLD) {
					assignMTLDToCohort(cohortList, mtlds, criteriaBeanList);
					/*
					 * Calculate the score for the cohort after final assignment
					 */
					scoreCohortList(cohortList, criteriaBeanList);
				} else {
					/*
					 * Calculate the score for the cohort after final assignment
					 */

					scoreCohortList(cohortList, criteriaBeanList);

				}

			}

			saveCohortList(cohortList, sessionUser, cohorts);

			logger.debug("Convert Entity to Bean for UI representation");
			convertEntityToBean(cohorts, mtlds, corps, unhierdcorps,
					isPriorityForAllTheCriteriaIsNotZero);

			List<Integer> corpsMemberListSize = (List<Integer>) cohorts
					.get(TFAConstants.CORPS_MEMBERS_COUNT);

			if (cohortCount < count && corpsMemberListSize != null) {
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
	private void saveCohortList(List<Cohort> cohortList, User sessionUser,
			Map<String, List<?>> cohorts) throws Exception {

		List<CohortBean> cohortBeans = new ArrayList<CohortBean>();
		List<String> cohortIDs = new ArrayList<String>();
		List<Cohort> cohortListToSave = new ArrayList<Cohort>();
		CohortBean cohortBean = null;
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
					CriteriaScoringUtils.calculateCohortDetails(cohort);
				}

				cohort.setCreatedBy(sessionUser.getLoginId());
				cohort.setCreatedDate(todayDate);
				cohort.setRegion(sessionUser.getRegion());
				try {
					// Store the cohort,cort detail in database
					cohortListToSave.add(cohort);
					//mtldCmRepository.saveCohort(cohort);

					
				} catch (Exception e) {
					logger.error("Exception at saveCohortList", e);
					throw e;
				}

				/*cohortIDs.add("" + cohort.getId());
				cohortBean = new CohortBean();
				cohortBean = convertCohortToCohortBean(cohort, cohortBean);
				cohortBeans.add(cohortBean);*/

				i++;
			}
			mtldCmRepository.saveCohort(cohortListToSave);
			for (Cohort cohort2 : cohortListToSave) {
			cohortIDs.add("" + cohort2.getId());
			cohortBean = new CohortBean();
			cohortBean = convertCohortToCohortBean(cohort2, cohortBean);
			cohortBeans.add(cohortBean);
			}
			cohorts.put(TFAConstants.COHORT_ID_LIST, cohortIDs);
			cohorts.put(TFAConstants.COHORT_LIST, cohortBeans);
			logger.debug("Storing total number of Corpsmember are present in the cohort formation for the region"
					+ corpsMemberSizeForRegion);
			List<Integer> corpsMemberListSize = new ArrayList<Integer>();
			corpsMemberListSize.add(corpsMemberSizeForRegion);
			cohorts.put(TFAConstants.CORPS_MEMBERS_COUNT, corpsMemberListSize);

		} catch (Exception e) {
			logger.error("Exception at saveCohortList", e);
			throw e;
		}

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
			List<CorpsMember> unhierdcorps,
			Boolean isPriorityForAllTheCriteriaIsZero) throws Exception {
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
	 * @param cohortCount2
	 * @param criteriaBeanList
	 */

	private void assignPooledCorpsMemberToCohort(List<CorpsMember> corps,
			List<Cohort> cohortList, int cohortCount2,
			List<CriteriaBean> criteriaBeanList) {
		int numberOfCMperCohort = (int) (Math.ceil(corps.size() / cohortCount2) + cohortCount2);

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
			scoreCohortList(cohortList, criteriaBeanList);

			Iterator<Cohort> iterator = cohortList.iterator();
			while (nextCohort == null && iterator.hasNext()) {

				tempNextCohort = iterator.next();

				if (!tempNextCohort.getIsFinalCohort()) {
					if (isBalanceGroup
							&& tempNextCohort.getCohortDetails().size() < numberOfCMperCohort) {

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

			}

			if (nextCohort == null) {
				break;
			}

			else {

				// calculate the score for all the Corpsmember which are in the
				// pool
				calculateTheScoreForPooledCorpsMember(nextCohort, corps,
						criteriaBeanList);
				CorpsMember corpsMember = null;
				if (corps != null && !corps.isEmpty() && isBalanceGroup) {
					corpsMember = corps.get(corps.size() - 1);
					corpsMember = findCoprsForNextCohort(corpsMember, corps,
							cohortList, criteriaBeanList, numberOfCMperCohort);

				}

				if (nextCohort != null
						&& nextCohort.getCohortDetails() != null
						&& nextCohort.getCohortDetails().size() >= numberOfCMperCohort) {

					isBalanceGroup = Boolean.FALSE;

				}

				if (corps != null && !corps.isEmpty() && isBalanceGroup) {

					cohortDetail = new CohortDetail();
					cohortDetail.setCorpMember(corpsMember);
					if (nextCohort != null && nextCohort.getMtld() != null
							&& nextCohort.getCohortDetails() != null
							&& nextCohort.getCohortDetails().isEmpty()) {
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
			List<CriteriaBean> criteriaBeanList, int numberOfCMperCohort) {

		// checking this corps is the most eligible corps for the
		// nextcohort

		int count = corps.size() - 2;
		int i = 0;

		while (i < cohortList.size()) {

			Cohort cohort = cohortList.get(i);

			double cmTotalScore = 0.0;
			for (CriteriaBean criteriaBean2 : criteriaBeanList) {

				if (criteriaBean2.getScoringCriteriaStrategy() != null) {
					cmTotalScore += getCriteriaScore(cohort, corpsMember,
							criteriaBean2) * criteriaBean2.getPriorityValue();

				}

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
			List<CorpsMember> corps, List<CriteriaBean> criteriaBeanList) {

		int i = 0;
		int corpsSize = corps.size();

		while (i < corpsSize) {

			CorpsMember corpsMember = corps.get(i);

			double totalCriteriaScore = 0;
			// find the score of the corps
			for (CriteriaBean criteriaBean : criteriaBeanList) {
				double criteriaScore = 0;
				if (criteriaBean.getScoringCriteriaStrategy() != null) {

					criteriaScore = getCriteriaScore(nextCohort, corpsMember,
							criteriaBean);

				}

				totalCriteriaScore = totalCriteriaScore
						+ (criteriaScore * criteriaBean.getPriorityValue());
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
		 * CHARTERNETWORKSCORINGCRITERIA scoring will called otherwise skip it
		 */

		scoringCriteriaStrategy = criteriaBean.getScoringCriteriaStrategy();

		boolean flag = TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
				.equalsIgnoreCase(criteriaBean.getClassAPI())
				|| TFAConstants.MATCHNEIGHBORHOODSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
				|| TFAConstants.FEEDERPATTERNSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim());

		if (flag && corpsMember.getSchool() != null) {
			criteriaScore = scoreCriteria(nextCohort, corpsMember,
					scoringCriteriaStrategy, criteriaBean);

		} else if (flag && corpsMember.getSchool() == null) {
			return criteriaScore;
		}

		else {
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
			List<CriteriaBean> criteriaBeanList) {
		int i = 0;
		int cohortListSize = cohortList.size();

		while (i < cohortListSize) {

			Cohort cohort = cohortList.get(i);
			if (!cohort.getIsFinalCohort()) {
				cohort.setMtld(null);

				double scoreMTLDToCohort = 0.0;

				for (MTLD mtld : mtlds) {

					for (CriteriaBean criteriaBean : criteriaBeanList) {
						if (criteriaBean.getScoringCriteriaStrategy() != null) {
							double criteriaScore = criteriaBean
									.getScoringCriteriaStrategy()
									.scoreMtldToCohort(mtld, cohort);

							scoreMTLDToCohort = scoreMTLDToCohort
									+ criteriaScore;
						}

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
			i++;
		}// while loop

	}

	/**
	 * Each cohort starts out empty, with no corps members or MTLD in it. We
	 * need to seed each cohort with a single corps member
	 * 
	 * @param criteriaBeanList
	 * 
	 * @param cohortList
	 * @param mtlds
	 * @param corps
	 * @param isgroupSeededWithMTLD
	 * @param cohortCount
	 * @param count
	 */
	private void seedCohortList(List<CriteriaBean> criteriaBeanList,
			List<Cohort> cohortList, List<MTLD> mtlds, List<CorpsMember> corps,
			boolean isgroupSeededWithMTLD, int cohortCount) {
		logger.debug("Start of the seedCohortList");

		while (cohortCount > 0) {
			Cohort tempCohort = null;
			CorpsMember corpsMember = null;
			Random random = new Random();
			CohortDetail cohortDetail = null;

			if (isgroupSeededWithMTLD && mtlds != null && !mtlds.isEmpty()) {
				tempCohort = new Cohort();
				tempCohort.setMtld(mtlds.get(random.nextInt(mtlds.size())));
				tempCohort.setIsFinalCohort(false);
				tempCohort.setCohortDetails(new ArrayList<CohortDetail>());
				cohortList.add(tempCohort);
				cohortCount--;
			} else {

				tempCohort = new Cohort();
				if (corps != null && !corps.isEmpty()) {

					if (cohortList != null && !cohortList.isEmpty()) {
						corpsMember = findCorpsmemberForCohort(random,
								criteriaBeanList, cohortList, corps);
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
				cohortCount--;

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
			List<CriteriaBean> criteriaBeanList, List<Cohort> cohortList,
			List<CorpsMember> corps) {
		CorpsMember cmToAdd = null;
	

		double cmToAddScore = 999999; // Start with a score way higher than
								// anything we'll actually get.

		for (CorpsMember cm : corps) {
			double cmTotalScore = 0;
			for (Cohort cohort : cohortList) {
				for (CriteriaBean criteriaBean : criteriaBeanList) {

					if (criteriaBean.getScoringCriteriaStrategy() != null) {

						cmTotalScore += getCriteriaScore(cohort, cm,
								criteriaBean) * criteriaBean.getPriorityValue();

					}

				}

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
	 * Populate the existing finalize cohort details
	 * 
	 * @param regionId
	 * @param corpMemberSizeForRegion
	 * @param mtlds
	 * @param corps
	 * @param cohortList
	 * @param count
	 * @throws Exception
	 */
	private Integer populateCohortListWithExistingCohort(int regionId,
			List<MTLD> mtlds, List<CorpsMember> corps, List<Cohort> cohortList)
			throws Exception {
		logger.debug("Start of the populateCohortListWithExistingCohort");

		Integer count = 0;
		try {
			// get the existing cohort for the region
			List<Cohort> existingCohortList = mtldCmRepository
					.getCohortListByRegionId(regionId);

			logger.debug("Size of existing finalize cohort for a region"
					+ existingCohortList != null ? existingCohortList.size()
					: existingCohortList);
			if (existingCohortList != null) {
				logger.debug("List Existing cohort" + existingCohortList.size());

				for (Cohort cohort : existingCohortList) {

					if (cohort != null && null != cohort.getMtld()) {
						mtlds.remove(cohort.getMtld());

					} else {

						corps.remove(cohort.getCorpsMember());
					}

					cohortList.add(cohort);
					if (count != null) {
						count = count + 1;
					}

				}
			}

			logger.debug("End of the populateCohortListWithExistingCohort");
		} catch (Exception e) {
			logger.error("Exception at convertEntityToBean", e);
			throw e;
		}
		return count;

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
					&& TFAConstants.CORPS_YEAR1 <= corp.getCorpsYear()) {
				corpsMemberBean.setCmYear(TFAConstants.CM_MTLD_ONE_YEAR);
			} else if (corp.getCorpsYear() != null
					&& TFAConstants.CORPS_YEAR2 <= corp.getCorpsYear()) {
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
			List<CriteriaBean> criteriaBeanList) {

		int i = 0;
		int cohortSize = cohortList.size();
		while (i < cohortSize) {

			Cohort cohort = cohortList.get(i);

			if (cohort != null && cohort.getIsFinalCohort() != null
					&& !cohort.getIsFinalCohort()) {
				calculateAssignedCorpsMemberScoreOFCohort(cohort,
						criteriaBeanList);

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
			List<CriteriaBean> criteriaBeanList) {

		double cohortScore = 0;
		Boolean isOverDistance = Boolean.FALSE;
		if (cohort.getCohortDetails() != null) {
			int i = 0;
			int cohortDetailSize = cohort.getCohortDetails().size();

			while (i < cohortDetailSize) {

				CohortDetail cohDetail = cohort.getCohortDetails().get(i);

				String criteriaScoreForCohortDetail = "";

				double totalCriteriaScore = 0;
				// Calculate the score of the corps which are in the cohort
				for (CriteriaBean criteriaBean : criteriaBeanList) {
					double criteriaScore = 0;

					if (criteriaBean.getScoringCriteriaStrategy() != null) {

						criteriaScore = getCriteriaScore(cohort,
								cohDetail.getCorpMember(), criteriaBean);

						if (!isOverDistance
								&& TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
										.equalsIgnoreCase(criteriaBean
												.getClassAPI()) && cohort.getCohortDetails().size() > 1
								&& criteriaScore < 1) {
							isOverDistance = Boolean.TRUE;
							cohort.setOverDistance(Boolean.TRUE);
						}

					}

					totalCriteriaScore = totalCriteriaScore
							+ (criteriaScore * criteriaBean.getPriorityValue());
					// need to change
					if (criteriaScoreForCohortDetail.isEmpty()) {
						criteriaScoreForCohortDetail = criteriaScoreForCohortDetail
								+ criteriaBean.getId() + "=" + criteriaScore;

					} else {
						criteriaScoreForCohortDetail = criteriaScoreForCohortDetail
								+ ","
								+ criteriaBean.getId()
								+ "="
								+ criteriaScore;
					}

					cohortScore = cohortScore
							+ cohDetail.getCorpMember().getTotalScore();

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

		if (CriteriaScoringUtils.cohortSizeWithoutCm(cohort, corpsMember) == 0) { // Don't
																					// score
																					// a
																					// corps
																					// member
																					// against
			// themselves
			return criteriaScore;
		}

		// When set of corpsmember in the cohort is zero and first seeded member
		// to the cohort is MTLD
		if (cohort != null && cohort.getMtld() != null
				&& cohort.getCohortDetails() != null
				&& cohort.getCohortDetails().isEmpty()) {
			criteriaScore = scoringCriteriaStrategy.scoreMtldToCorpsMember(
					cohort.getMtld(), corpsMember);
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
		for (CohortDetail tempCohortDetail : cohort.getCohortDetails()) {
			corpsMemberBean = new CMBean();
			cohortDetailBean = new CohortDetailBean();

			BeanUtils.copyProperties(cohortDetailBean, tempCohortDetail);

			corpsMemberBean = convertCorpsMemberToCMBean(corpsMemberBean,
					tempCohortDetail.getCorpMember());

			if (tempCohortDetail != null
					&& tempCohortDetail.getCorpMember() != null
					&& tempCohortDetail.getCorpMember().getCorpsYear() != null) {
				if (TFAConstants.CORPS_YEAR1 <= tempCohortDetail
						.getCorpMember().getCorpsYear()) {
					corpsMemberBean.setCmYear(TFAConstants.CM_MTLD_ONE_YEAR);
				} else if (TFAConstants.CORPS_YEAR2 <= tempCohortDetail
						.getCorpMember().getCorpsYear()) {
					corpsMemberBean.setCmYear(TFAConstants.CM_MTLD_TWO_YEAR);
				}
			}

			cohortDetailBean.setCorpsMember(corpsMemberBean);

			cohortDetailBeanList.add(cohortDetailBean);

		}
		cohortBean.setCohortDetailBean(cohortDetailBeanList);

		BeanUtils.copyProperties(cohortBean, cohort);
		createCriteriaScoreMap(cohortBean);
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
			List<CMBean> corpList, List<CriteriaBean> criteriaBeanList,
			Map<String, List<?>> cohortMap) throws Exception {
		logger.debug("Inside method calculatePercentageForCriteriaGraph");
		Map<String, Double> percentageMap = new LinkedHashMap<String, Double>();
		List<Map<String, Double>> mapList = new ArrayList<Map<String, Double>>();
		int criteriaPriorityCounter = 0;
		int i = 0, j = 0;
		int criteriaListSize = criteriaBeanList.size();
		int corpsListSize = corpList.size();

		Double overallScoresum = 0.0;
		while (i < criteriaListSize) {

			CriteriaBean criteriaBean = criteriaBeanList.get(i);
			Double criteriaScoreSum = 0.0;
			j = 0;
			while (j < corpsListSize) {
				CMBean cmBean = corpList.get(j);
				if (cmBean.getCriteriaScore() != null) {
					for (Entry entry : cmBean.getCriteriaScore().entrySet()) {
						if (Integer.parseInt(entry.getKey().toString()) == criteriaBean
								.getId() && Double.parseDouble(entry.getValue().toString()) > 0.0
								&& criteriaBean.getPriorityValue() > 0) {

								criteriaScoreSum = criteriaScoreSum
										+ ((Double.parseDouble(entry.getValue()
												.toString())));
						}
					}
				}
				j++;
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
						.put(criteriaBean.getName(), Double
								.valueOf(new DecimalFormat("0.##")
										.format((criteriaScoreSum / corpList
												.size()) * 100)));
			} else {
				percentageMap.put(criteriaBean.getName(), 0.0);
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
			List<CohortBean> cohortBeans,
			Map<String, List<CriteriaBean>> criteriaMap) {
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

					if (criteriaMap != null
							&& cohortDetailBean.getCorpsMember()
									.getCriteriaScore() != null) {

						for (Map.Entry<String, List<CriteriaBean>> entry : criteriaMap
								.entrySet()) {
							if (entry.getKey().equalsIgnoreCase(
									TFAConstants.CRITERIA_CATEGORY_BASICS)) {

								List<CriteriaBean> basicsCriteriaList = entry
										.getValue();

								for (CriteriaBean criteriaBeanObj : basicsCriteriaList) {

									if (criteriaBeanObj.getPriorityValue() > 0
											&& cohortBean.getCohortDetailBean()
													.indexOf(cohortDetailBean) == 0) {
										basicsCriteriaListSize = basicsCriteriaListSize + 1;
									}

									basicsCriteriaPer = basicsCriteriaPer
											+ cohortDetailBean
													.getCorpsMember()
													.getCriteriaScore()
													.get(criteriaBeanObj
															.getId());

								}

							}

							else if (entry.getKey().equalsIgnoreCase(
									TFAConstants.CRITERIA_CATEGORY_CONTENT)) {

								List<CriteriaBean> contentCriteriaList = entry
										.getValue();

								for (CriteriaBean criteriaBeanObj : contentCriteriaList) {

									if (criteriaBeanObj.getPriorityValue() > 0
											&& cohortBean.getCohortDetailBean()
													.indexOf(cohortDetailBean) == 0) {
										contentCriteriaListSize = contentCriteriaListSize + 1;
									}

									contentCriteriaPer = contentCriteriaPer
											+ cohortDetailBean
													.getCorpsMember()
													.getCriteriaScore()
													.get(criteriaBeanObj
															.getId());

								}

							}

							else if (entry.getKey().equalsIgnoreCase(
									TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY)) {

								List<CriteriaBean> geographyCriteriaList = entry
										.getValue();

								for (CriteriaBean criteriaBeanObj : geographyCriteriaList) {

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
										cohortBean
												.setMaxDistance(criteriaBeanObj
														.getFieldValue());
									}

									geographicCriteriaPer = geographicCriteriaPer
											+ cohortDetailBean
													.getCorpsMember()
													.getCriteriaScore()
													.get(criteriaBeanObj
															.getId());

								}

							}

							else if (entry
									.getKey()
									.equalsIgnoreCase(
											TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS)) {

								List<CriteriaBean> relationshipCriteriaList = entry
										.getValue();

								for (CriteriaBean criteriaBeanObj : relationshipCriteriaList) {

									if (criteriaBeanObj.getPriorityValue() > 0
											&& cohortBean.getCohortDetailBean()
													.indexOf(cohortDetailBean) == 0) {
										relationshipCriteriaListSize = relationshipCriteriaListSize + 1;
									}

									relationshipsCriteriaPer = relationshipsCriteriaPer
											+ cohortDetailBean
													.getCorpsMember()
													.getCriteriaScore()
													.get(criteriaBeanObj
															.getId());

								}

							}

						}

					}// null check block

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

}
