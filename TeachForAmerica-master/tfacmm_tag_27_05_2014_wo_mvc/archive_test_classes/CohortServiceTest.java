package org.tfa.mtld.service.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.scoring.MTLDToCMRatioScoringCriteria;
import org.tfa.mtld.scoring.MinimizeTravelDistanceScoringCriteria;
import org.tfa.mtld.scoring.ScoringCriteriaStrategy;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.UserBean;
import org.tfa.mtld.service.constants.TFAConstants;

/**
 * @author divesh.solanki Modified By Lovely Ram
 * 
 */
@ContextConfiguration(locations = { "classpath:applicationContext-service.xml" })
public class CohortServiceTest {

	@Autowired
	public CohortService cohortService;

	@Autowired
	public CriteriaService criteriaService;
	
	@Autowired
	public CohortUpdateService cohortUpdateService;

	@Autowired
	public UserService userService;

	List criteriaBeanList;
	Map<String, List<CriteriaBean>> criteriaList;

	User user = null;

	@SuppressWarnings("resource")
	@Before
	public void init() throws Exception {
			if (cohortService == null || criteriaService == null
					|| userService == null) {
				ApplicationContext context = new ClassPathXmlApplicationContext(
						"applicationContext-service.xml");
				cohortService = (CohortService) context
						.getBean("cohortServiceImpl");

				criteriaService = (CriteriaService) context
						.getBean("criteriaServiceImpl");
				cohortUpdateService = (CohortUpdateService) context.getBean("cohortUpdateServiceImpl");

				userService = (UserService) context.getBean("userServiceImpl");
			}
			Assert.assertNotNull(cohortService);
			Assert.assertNotNull(criteriaService);
			Assert.assertNotNull(userService);
			Assert.assertNotNull(cohortUpdateService);
			

			criteriaList = criteriaService.getCriteriaList();
			criteriaBeanList = getCriteriaList(criteriaList);

			user = getUser("cohort", "cohort");


		

	}

	@After
	public void destroy() throws Exception {

		
			cohortService.flushUnfinalizeCohort(4); 

		
	}

	/*
	 * This test case will cover to flush the unfinalize cohort for a particular region
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testFlushUnfinalizeCohort() throws Exception {

			cohortService.flushUnfinalizeCohort(17);
	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * All the cohort contains two corps member.
	 */
	@Test
	public void testCreateGroup() throws Exception {

		@SuppressWarnings("unchecked")
		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,4, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		@SuppressWarnings("unchecked")
		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			assertEquals(2, cohortBean.getCohortDetailBean().size());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and hired and unhired corps member are
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria for all the criteria
	 * value is zero
	 */
	@Test
	public void testCreateGroupForPriorityForAllTheCriteriaIsZero()
			throws Exception {
		// Need to set test data here...............

		criteriaBeanList = getCriteriaList20(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,5, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(29, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3 but as value for
		// PriorityForAllTheCriteriaIsZero is False so it is return zero
		// there is not any finalize cohort exist
		assertEquals(0, cohortBeans.size());

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList1()
			throws Exception {
		// Need to set test data here...............

		criteriaBeanList = getCriteriaList1(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,0, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList3()
			throws Exception {

		criteriaBeanList = getCriteriaList3(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,5, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList4()
			throws Exception {

		criteriaBeanList = getCriteriaList4(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,7, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList5()
			throws Exception {

		criteriaBeanList = getCriteriaList5(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,8, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList6()
			throws Exception {

		criteriaBeanList = getCriteriaList6(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,10, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList7()
			throws Exception {

		criteriaBeanList = getCriteriaList7(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,3, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList8()
			throws Exception {

		criteriaBeanList = getCriteriaList8(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,12, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired and Unhired corps
	 * member are included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithMTLDAndBothTypeOfCorpsMemberForCriteriaList9()
			throws Exception {

		criteriaBeanList = getCriteriaList10(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,10, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired and Unhired copmember size is
		// 29
		assertEquals(29, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// MTld present in any cohort if any MTLD present for the region as well
		// as Corp member are the first seeded member for the cohort
		// and hired Corps member and Unhired corpsmember are included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				if (cohortDetailBean.getCorpsMember().getIsHired()) {
					assertEquals("Hired CorpMember", Boolean.TRUE,
							cohortDetailBean.getCorpsMember().getIsHired());
				} else {
					assertEquals("Hired CorpMember", Boolean.FALSE,
							cohortDetailBean.getCorpsMember().getIsHired());
				}

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList10()
			throws Exception {

		criteriaBeanList = getCriteriaList10(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,1, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired corps member are R
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithMTLDAndHiredCorpsMemberForCriteriaList11()
			throws Exception {

		criteriaBeanList = getCriteriaList11(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,2, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}
	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired corps member are R
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithMTLDAndHiredCorpsMemberForCriteriaList12()
			throws Exception {

		criteriaBeanList = getCriteriaList12(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,5, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired corps member are R
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList13()
			throws Exception {

		criteriaBeanList = getCriteriaList13(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,6, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired corps member are R
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList14()
			throws Exception {

		criteriaBeanList = getCriteriaList14(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,7, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired corps member are R
	 * included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList15()
			throws Exception {

		criteriaBeanList = getCriteriaList15(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList, 2,Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList16()
			throws Exception {

		criteriaBeanList = getCriteriaList16(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,5, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList18()
			throws Exception {

		criteriaBeanList = getCriteriaList18(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,6, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		assertEquals(13, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				assertEquals(Boolean.TRUE, cohortDetailBean.getCorpsMember()
						.getIsHired());

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@Test
	public void testCreateGroupWithoutMTLDAndbothTypeOfCorpsMemberForCriteriaList19()
			throws Exception {

		criteriaBeanList = getCriteriaList19(criteriaList);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList,0, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
				user);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 29
		assertEquals(29, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region Boston total number cohort required is 3
		assertEquals(3, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				if (cohortDetailBean.getCorpsMember().getIsHired()) {
					assertEquals("Hired CorpMember", Boolean.TRUE,
							cohortDetailBean.getCorpsMember().getIsHired());
				} else {
					assertEquals("Hired CorpMember", Boolean.FALSE,
							cohortDetailBean.getCorpsMember().getIsHired());
				}

			}

		}

	}

	/*
	 * Below test case is for checking groupDetails info.First will create a
	 * group with specified parameters and then compare group details with
	 * collection.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetGroupDetails() throws Exception {

			// Creating Group by calling createGroup api.
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,8, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);

			int cohortId = 0;

			if (cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();

			}
			// Fetching created group/cohort based on Id.
			CohortBean cohort = cohortUpdateService.getCohortDetails(cohortId,criteriaBeanList);

			Map<String, String> expectedMap = new HashMap<String, String>();

			expectedMap.put("SchoolDisrict",
					cohort.getSchoolRep());
			expectedMap.put("SPEDPercentage",
					cohort.getSpedModifierPercentage());
			expectedMap.put("NeighbourhoodRepresented",
					cohort.getNeighbourhoodRepresented());
			expectedMap.put("EcePercentage", cohort.getEcePercentage());
			expectedMap.put("OneYearCorpPercentage",
					cohort.getOneYearCorpPercentage());
			expectedMap.put("TwoYearCorpPercentage",
					cohort.getTwoYearCorpPercentage());
			expectedMap.put("CharterPartnerPercentage",
					cohort.getCharterPartnerPercentage());
			expectedMap.put("DistrictPartnerPercentage",
					cohort.getDistrictPartnerPercentage());
			expectedMap.put("FeederPatternHS", cohort.getFeederPatternHS());
			expectedMap.put("LowIncomePercentage",
					cohort.getLowIncomePercentage());

			Assert.assertEquals("Executed Successful", expectedMap,
					cohortService.getGroupDetails(cohortId));

	}

	/*
	 * Below test case checks the method calculates percentage for each criteria
	 * using the CM Score.
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testCalculatePercentageForCriteriaGraph() throws Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,15, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);

			List<CMBean> corps = new ArrayList<CMBean>(
					(List<CMBean>) cohortMap
							.get(TFAConstants.HIRED_CORPS_MEMBER));
			List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
					.get(TFAConstants.COHORT_LIST);

			for (CohortBean cohortBean : cohortBeans) {
				for (CohortDetailBean cohortDetailBean : cohortBean
						.getCohortDetailBean()) {
					corps.add(cohortDetailBean.getCorpsMember().clone());
				}
			}

			corps.addAll((List<CMBean>) cohortMap
					.get(TFAConstants.UNHIRED_CORPS_MEMBER));

			Map<String, List<?>> criteriaBarGraphMap = cohortService
					.calculatePercentageForCriteriaGraph(corps,
							criteriaBeanList, cohortMap);
	}

	private List getCriteriaList1(Map<String, List<CriteriaBean>> criteriaList) throws Exception {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(2);

		criteriaBeanList.get(4).setPriorityValue(2);

		criteriaBeanList.get(5).setPriorityValue(3);

		criteriaBeanList.get(6).setPriorityValue(4);

		criteriaBeanList.get(7).setPriorityValue(4);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(8);

		criteriaBeanList.get(10).setPriorityValue(8);

		criteriaBeanList.get(11).setPriorityValue(0);

		criteriaBeanList.get(12).setPriorityValue(0);

		criteriaBeanList.get(13).setPriorityValue(0);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList2(Map<String, List<CriteriaBean>> criteriaList) throws Exception {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
				
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(2);

		criteriaBeanList.get(3).setPriorityValue(2);

		criteriaBeanList.get(4).setPriorityValue(3);

		criteriaBeanList.get(5).setPriorityValue(4);

		criteriaBeanList.get(6).setPriorityValue(8);

		criteriaBeanList.get(7).setPriorityValue(2);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(8);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList3(Map<String, List<CriteriaBean>> criteriaList) throws Exception {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();

				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(2);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(4);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(8);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(0);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(0);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList4(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI().trim()).newInstance();

				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(4);

		criteriaBeanList.get(5).setPriorityValue(2);

		criteriaBeanList.get(6).setPriorityValue(8);

		criteriaBeanList.get(7).setPriorityValue(4);

		criteriaBeanList.get(8).setPriorityValue(2);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(8);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(0);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList5(Map<String, List<CriteriaBean>> criteriaList) throws Exception {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("20");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("150");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
				
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(8);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(4);

		criteriaBeanList.get(8).setPriorityValue(3);

		criteriaBeanList.get(9).setPriorityValue(8);

		criteriaBeanList.get(10).setPriorityValue(2);

		criteriaBeanList.get(11).setPriorityValue(0);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList6(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("15");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("200");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(4);

		criteriaBeanList.get(5).setPriorityValue(8);

		criteriaBeanList.get(6).setPriorityValue(4);

		criteriaBeanList.get(7).setPriorityValue(2);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList7(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList8(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList10(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList11(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList12(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList13(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList14(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList15(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList16(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList17(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList18(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList19(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList20(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(0);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(0);

		criteriaBeanList.get(2).setPriorityValue(0);

		criteriaBeanList.get(3).setPriorityValue(0);

		criteriaBeanList.get(4).setPriorityValue(0);

		criteriaBeanList.get(5).setPriorityValue(0);

		criteriaBeanList.get(6).setPriorityValue(0);

		criteriaBeanList.get(7).setPriorityValue(0);

		criteriaBeanList.get(8).setPriorityValue(0);

		criteriaBeanList.get(9).setPriorityValue(0);

		criteriaBeanList.get(10).setPriorityValue(0);

		criteriaBeanList.get(11).setPriorityValue(0);

		criteriaBeanList.get(12).setPriorityValue(0);

		criteriaBeanList.get(13).setPriorityValue(0);

		criteriaBeanList.get(14).setPriorityValue(0);

		return criteriaBeanList;
	}

	private List getCriteriaList21(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private List getCriteriaList(Map<String, List<CriteriaBean>> criteriaList) throws Exception{
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}

		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private User getUser(String userName, String password) throws Exception{

		UserBean userBean = new UserBean();
		userBean.setLoginId(userName);
		userBean.setPassword(password);
		User user = null;
		
			user = userService.userLogin(userBean);
		

		return user;
	}
}
