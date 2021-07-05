package org.tfa.mtld.service.services;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.Region;
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
import org.tfa.mtld.service.constants.TFAConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CohortServiceMockTest {
	@Autowired
	public CohortService cohortService;

	@Autowired
	MtldCmRepository mtldCmRepository;
	Map<String, List<?>> expectedCohortMap;
	List<CriteriaBean> criteriaBeanList;

	User sessionUser;

	Cohort expectedCohortDeail;

	List<MTLD> expectedMtlds;

	List<CorpsMember> expectedCorps;

	List<CorpsMember> expectedUnhierdcorps;
	List<CorpsMember> expectedHierdcorps;

	@Configuration
	static class CohortServiceMockTestContextConfiguration {

		@Bean
		public MtldCmRepository mtldCmRepository() {
			return Mockito.mock(MtldCmRepository.class);
		}

		@Bean
		public CohortServiceImpl cohortService() {
			return new CohortServiceImpl();
		}
	}

	public List<CriteriaBean> createCriteriaListForGraph() {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		CriteriaBean criteriaBean = new CriteriaBean();
		criteriaBean.setClassAPI(TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA);
		criteriaBean.setDescription(null);
		criteriaBean.setFieldPlaceholder("#CMs/MTLD");
		criteriaBean.setFieldType("Text Field");
		criteriaBean.setFieldValidation("number_two_digit");
		criteriaBean.setFiledRequired(true);
		criteriaBean.setId(1);
		criteriaBean.setName(TFAConstants.RESTRICT_MTLD_CM_RATIO);
		criteriaBean.setPriorityValue(4);
		criteriaBeanList.add(criteriaBean);
		CriteriaBean criteriaBean2 = new CriteriaBean();

		criteriaBean2
				.setClassAPI(TFAConstants.BALANCECORPSYEARSSCORINGCRITERIA);
		criteriaBean2.setDescription(null);
		criteriaBean2.setFieldPlaceholder("#CMs/MTLD");
		criteriaBean2.setFieldType("Text Field");
		criteriaBean2.setFieldValidation("number_two_digit");
		criteriaBean2.setPriorityValue(4);
		criteriaBean2.setFiledRequired(true);
		criteriaBean2.setId(2);
		criteriaBean2.setName(TFAConstants.BALANCE_COHORTS_FOR_1CM_2CM);
		criteriaBeanList.add(criteriaBean2);
		return criteriaBeanList;
	}

	@Before
	public void setUp() throws Exception {
		expectedCohortMap = new HashMap<String, List<?>>();
		expectedCohortDeail = createCohort(14);
		criteriaBeanList = createCriteriaListForGraph();
		Map<String, Double> percentageMap = new LinkedHashMap<String, Double>();
		List<Map<String, Double>> mapList = new ArrayList<Map<String, Double>>();
		percentageMap.put(TFAConstants.RESTRICT_MTLD_CM_RATIO, 73.33);
		percentageMap.put(TFAConstants.BALANCE_COHORTS_FOR_1CM_2CM, 56.33);
		percentageMap.put(TFAConstants.OVERALL_PROGRESS_KEY, 64.83);
		mapList.add(percentageMap);
		expectedCohortMap.put(TFAConstants.CRITERIA_MAP_SCORE_KEY, mapList);

		sessionUser = new User();
		sessionUser.setUserId(5);
		sessionUser.setLoginId("tfatest");
		sessionUser.setPassword("tfatest");
		sessionUser.setFirstName("TFA");
		sessionUser.setLastName("User");
		sessionUser.setEmailId("tfa@tfa.com");
		Region region = new Region();
		region.setRegionId(1);
		sessionUser.setRegion(region);

		expectedMtlds = getMTLDList(1);
		expectedCorps = getCorpsMemberList(true);
		expectedUnhierdcorps = getUnhiredCorpsMemberList(false);
		expectedHierdcorps = getHiredCorpsMemberList(true);

	}

	/* Below test case is for Group Detail Pop up scenario. */
	@Test()
	public void testGetGroupDetails() throws Exception {
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(
				expectedCohortDeail);

		Map<String, String> actualMap = cohortService.getGroupDetails(1);

		Map<String, String> expectedMap = new HashMap<String, String>();

		expectedMap.put(TFAConstants.GROUP_SCHOOL_DISTRICT,
				expectedCohortDeail.getSchoolDistrictRepresented());
		expectedMap.put(TFAConstants.GROUP_SPED_PERCENTAGE,
				expectedCohortDeail.getSpedModifierPercentage());
		expectedMap.put(TFAConstants.GROUP_NEIGHBOURHOOD_REPRESENTED,
				expectedCohortDeail.getNeighbourhoodRepresented());
		expectedMap.put(TFAConstants.GROUP_ECE_PERCETAGE,
				expectedCohortDeail.getEcePercentage());
		expectedMap.put(TFAConstants.GROUP_ONE_YEAR_CORP_PERCENTAGE,
				expectedCohortDeail.getOneYearCorpPercentage());
		expectedMap.put(TFAConstants.GROUP_TWO_YEAR_CORP_PERCENTAGE,
				expectedCohortDeail.getTwoYearCorpPercentage());
		expectedMap.put(TFAConstants.GROUP_CHARTER_PARTNER_PERCENTAGE,
				expectedCohortDeail.getCharterPartnerPercentage());
		expectedMap.put(TFAConstants.GROUP_DISTRICT_PARTNER_PERCENTAGE,
				expectedCohortDeail.getDistrictPartnerPercentage());
		expectedMap.put(TFAConstants.GROUP_LOW_INCOME_PERCENTAGE,
				expectedCohortDeail.getLowIncomePercentage());
		expectedMap.put(TFAConstants.GROUP_FEEDER_PATTERN_HS,
				expectedCohortDeail.getFeederPatternHS());

		Assert.assertEquals("Executed Successful", expectedMap, actualMap);
	}

	private CorpsMember getCorpsMember(Integer id, int corpsYear,
			String subjectGroup, String gradeLevel, String subjectModifier,
			School school, String cmoAffiliation, Double totalScore,
			Map<Integer, Double> criteriaScorMap, Region region) {
		CorpsMember corps = new CorpsMember();

		corps.setId(id);
		corps.setCorpsYear(corpsYear);
		corps.setSubjectGroup(subjectGroup);
		corps.setGradeLevel(gradeLevel);
		corps.setSubjectModifier(subjectModifier);

		corps.setSchool(school);
		corps.setCmoAffiliation(cmoAffiliation);
		corps.setRegion(region);
		corps.setTotalScore(totalScore);
		//corps.setCriteriaScore(criteriaScorMap);
		return corps;
	}

	private List<CorpsMember> getCorpsMemberList(Boolean flag) {
		Region region = new Region();
		region.setRegionId(1);

		List<CorpsMember> coprsList = new ArrayList<CorpsMember>();
		School school = createSchool(8901, "CHARTER", "Neighborhood6",
				41.5441677, -87.3582961, null);
		coprsList.add(getCorpsMember(1, 2012, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(2, 2012, "ENGLISH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(3, 2008, "MATH", "MIDDLE", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(4, 2013, "MATH", "HIGH", "SPEDINCL", null,
				"Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(5, 2009, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(6, 2013, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));

		coprsList.add(getCorpsMember(7, 2012, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(8, 2012, "ENGLISH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(9, 2008, "MATH", "MIDDLE", "", school,
				"Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(10, 2013, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(11, 2009, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(12, 2013, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));

		/*
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 */

		return coprsList;
	}

	private List<CorpsMember> getHiredCorpsMemberList(Boolean flag) {
		Region region = new Region();
		region.setRegionId(1);

		List<CorpsMember> coprsList = new ArrayList<CorpsMember>();
		School school = createSchool(8901, "CHARTER", "Neighborhood6",
				41.5441677, -87.3582961, null);
		coprsList.add(getCorpsMember(7, 2012, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(8, 2012, "ENGLISH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(9, 2008, "MATH", "MIDDLE", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(10, 2013, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(11, 2009, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(12, 2013, "MATH", "HIGH", "SPEDINCL",
				school, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		/*
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 */

		return coprsList;
	}

	private List<CorpsMember> getUnhiredCorpsMemberList(Boolean flag) {
		Region region = new Region();
		region.setRegionId(1);

		List<CorpsMember> coprsList = new ArrayList<CorpsMember>();

		coprsList.add(getCorpsMember(7, 2012, "MATH", "HIGH", "SPEDINCL", null,
				"Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(8, 2012, "ENGLISH", "HIGH", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(9, 2008, "MATH", "MIDDLE", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(10, 2013, "MATH", "HIGH", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(11, 2009, "MATH", "HIGH", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		coprsList.add(getCorpsMember(12, 2013, "MATH", "HIGH", "SPEDINCL",
				null, "Glendale Elementary School District", 0.0,
				new HashMap<Integer, Double>(), region));
		/*
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 * coprsList.add(getCorpsMember()); coprsList.add(getCorpsMember());
		 */

		return coprsList;
	}

	private MTLD getMTLD(Integer id, String principalPrefrence,
			String subjectTaught, String specialityArea, String primarySubject,
			Double latitude, Double longitude, Double scoreMTLDToCohort,
			String cmoAffiliation, Set<School> schoolSet, Region region) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setPrincipalPrefrence(principalPrefrence);
		mtld.setCmoAffiliation(cmoAffiliation);
		mtld.setSubjectTaught(subjectTaught);
		mtld.setSpecialityArea(specialityArea);
		mtld.setPrimarySubject(primarySubject);
		mtld.setLatitude(latitude);
		mtld.setLongitude(longitude);
		mtld.setScoreMTLDToCohort(scoreMTLDToCohort);
		mtld.setCmoAffiliation(cmoAffiliation);
		mtld.setPriorSchoolsWorked(schoolSet);
		mtld.setRegion(region);
		return mtld;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<MTLD> getMTLDList(int regionId) {

		Set schoolSet = new HashSet<School>();
		Region region = new Region();
		region.setRegionId(regionId);
		School school = createSchool(8901, "CHARTER", "Neighborhood6",
				41.5441677, -87.3582961, null);
		schoolSet.add(school);

		List<MTLD> mtldList = new ArrayList<MTLD>();

		mtldList.add(getMTLD(1, null, "MATH", "SCIENCE", "MATH", 41.5441677,
				-87.3582961, 0.0, "Glendale Elementary School District",
				schoolSet, region));

		return mtldList;
	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are R included
	 * in cohort formation
	 * 
	 * All the cohort contains two corps member.
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Test()
	public void testCreateGroup() throws Exception {
		Mockito.when(mtldCmRepository.getCorpMemberListByRegionId(1))
				.thenReturn(expectedCorps);
		Mockito.when(mtldCmRepository.getUnhiredCorpMemberListByRegionId(1))
				.thenReturn(expectedUnhierdcorps);
		Mockito.when(mtldCmRepository.getHiredCorpMemberListByRegionId(1))
				.thenReturn(expectedHierdcorps);
		Mockito.when(mtldCmRepository.getMTLDListByRegionId(1)).thenReturn(
				expectedMtlds);

		List<CriteriaBean> criteriaBeanList1 = createCriteriaList(1);
		Map<String, List<?>> expectedCohortMap = cohortService.createGroup(
				criteriaBeanList1, 2, Boolean.FALSE, Boolean.FALSE,
				Boolean.TRUE, sessionUser);

		List<CohortBean> cohortBeans = (List<CohortBean>) expectedCohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region 1 total number of copmember size is 6
		Assert.assertEquals(6,
				expectedCohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT).get(0));
		// For Region 1 total number cohort required is 2
		Assert.assertEquals(2, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			Assert.assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			Assert.assertEquals(3, cohortBean.getCohortDetailBean().size());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				Assert.assertTrue(cohortDetailBean.getCorpsMember()
						.getSchoolBean() != null);

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
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateGroupForPriorityForAllTheCriteriaIsZero()
			throws Exception {
		Mockito.when(mtldCmRepository.getCorpMemberListByRegionId(1))
				.thenReturn(expectedCorps);
		Mockito.when(mtldCmRepository.getUnhiredCorpMemberListByRegionId(1))
				.thenReturn(expectedUnhierdcorps);
		Mockito.when(mtldCmRepository.getMTLDListByRegionId(1)).thenReturn(
				expectedMtlds);

		List<CriteriaBean> criteriaBeanList20 = createCriteriaList(20);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList20, 5, Boolean.FALSE, Boolean.TRUE,
				Boolean.FALSE, sessionUser);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region 1 total number of copmember size in cohort is zero because
		// the priority is zero for all criteria
		Assert.assertEquals(0, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT)
				.get(0));
		// For Region 1 total number cohort required is 5 but as value for
		// PriorityForAllTheCriteriaIsZero is False so it is return zero
		// there is not any finalize cohort exist
		Assert.assertEquals(0, cohortBeans.size());

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and only hired corps member are included in
	 * cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList1()
			throws Exception {
		Mockito.when(mtldCmRepository.getCorpMemberListByRegionId(1))
				.thenReturn(expectedCorps);
		Mockito.when(mtldCmRepository.getUnhiredCorpMemberListByRegionId(1))
				.thenReturn(expectedUnhierdcorps);
		Mockito.when(mtldCmRepository.getHiredCorpMemberListByRegionId(1))
				.thenReturn(expectedHierdcorps);
		Mockito.when(mtldCmRepository.getMTLDListByRegionId(1)).thenReturn(
				expectedMtlds);

		List<CriteriaBean> criteriaBeanList1 = createCriteriaList(1);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList1, 2, Boolean.FALSE, Boolean.FALSE,
				Boolean.TRUE, sessionUser);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// For Region Boston total number of Hired copmember size is 13
		Assert.assertEquals(6, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT)
				.get(0));
		// For Region Boston total number cohort required is 3
		Assert.assertEquals(2, cohortBeans.size());

		// No MTld present in any cohort and only hired Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			Assert.assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				Assert.assertTrue(cohortDetailBean.getCorpsMember()
						.getSchoolBean() != null);

			}

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember and all type of corps member are included in
	 * cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateGroupWithoutMTLDAndHiredCorpsMemberForCriteriaList3()
			throws Exception {
		Mockito.when(mtldCmRepository.getCorpMemberListByRegionId(1))
				.thenReturn(expectedCorps);
		Mockito.when(mtldCmRepository.getUnhiredCorpMemberListByRegionId(1))
				.thenReturn(expectedUnhierdcorps);
		Mockito.when(mtldCmRepository.getHiredCorpMemberListByRegionId(1))
				.thenReturn(expectedHierdcorps);
		Mockito.when(mtldCmRepository.getMTLDListByRegionId(1)).thenReturn(
				expectedMtlds);

		List<CriteriaBean> criteriaBeanList3 = createCriteriaList(3);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList3, 2, Boolean.FALSE, Boolean.TRUE,
				Boolean.TRUE, sessionUser);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// total number of copmember size is 12
		Assert.assertEquals(12, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT)
				.get(0));
		// total number cohort required is 2
		Assert.assertEquals(2, cohortBeans.size());

		// No MTld present in any cohort and both type of Corps member are
		// included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			Assert.assertEquals("No MTLD present in Cohort formation", null,
					cohortBean.getSeededMtldBean());

		}

	}

	/*
	 * This test case will cover first scenario in which first seeding member
	 * for the cohort is CorpMember or MTLD and only hired and Unhired corps
	 * member are included in cohort formation
	 * 
	 * Taking the arbitrary selected priority for criteria
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateGroupWithMTLDAndBothTypeOfCorpsMemberForCriteriaList9()
			throws Exception {

		Mockito.when(mtldCmRepository.getCorpMemberListByRegionId(1))
				.thenReturn(expectedCorps);
		Mockito.when(mtldCmRepository.getUnhiredCorpMemberListByRegionId(1))
				.thenReturn(expectedUnhierdcorps);
		Mockito.when(mtldCmRepository.getHiredCorpMemberListByRegionId(1))
				.thenReturn(expectedHierdcorps);
		Mockito.when(mtldCmRepository.getMTLDListByRegionId(1)).thenReturn(
				expectedMtlds);

		List<CriteriaBean> criteriaBeanList10 = createCriteriaList(10);

		Map<String, List<?>> cohortMap = cohortService.createGroup(
				criteriaBeanList10, 2, Boolean.TRUE, Boolean.TRUE,
				Boolean.TRUE, sessionUser);

		List<CohortBean> cohortBeans = (List<CohortBean>) cohortMap
				.get(TFAConstants.COHORT_LIST);

		// total number of Hired and Unhired copmember size is
		// 12
		Assert.assertEquals(12, cohortMap.get(TFAConstants.CORPS_MEMBERS_COUNT)
				.get(0));
		// total number cohort required is 2
		Assert.assertEquals(2, cohortBeans.size());

		// MTld present in any cohort if any MTLD present for the region as well
		// as Corp member are the first seeded member for the cohort
		// and hired Corps member and Unhired corpsmember are included in Cohort
		for (CohortBean cohortBean : cohortBeans) {
			if (cohortBean.getSeededMtldBean() == null) {
				Assert.assertEquals(
						"CorpMember is the first seeded member to the Cohort",
						null, cohortBean.getSeededMtldBean());
			} else {

				Assert.assertEquals("MTLD is the first seeded member", null,
						cohortBean.getSeededCMBean());
			}

			for (CohortDetailBean cohortDetailBean : cohortBean
					.getCohortDetailBean()) {

				if (cohortDetailBean.getCorpsMember().getSchoolBean() != null) {
					Assert.assertTrue(cohortDetailBean.getCorpsMember()
							.getSchoolBean() != null);
				} else {
					Assert.assertTrue(cohortDetailBean.getCorpsMember()
							.getSchoolBean() == null);
				}

			}

		}

	}

	/*
	 * Below test case checks the method calculates percentage for each criteria
	 * using the CM Score.
	 */
	@Test
	public void testCalculatePercentageForCriteriaGraph() throws Exception {
		Map<String, List<?>> cohortMap = new HashMap<String, List<?>>();
		List<CMBean> corps = new ArrayList<CMBean>();
		corps.add(createCorpMemberBean(1, "2012", 1.0, 0.8));
		corps.add(createCorpMemberBean(2, "2012", 0.7, 0.0));
		corps.add(createCorpMemberBean(3, "2012", 0.5, 0.89));
		Map<String, List<?>> criteriaBarGraphMap = cohortService
				.calculatePercentageForCriteriaGraph(corps,
						createCriteriaListForGraph(), cohortMap);
		assertTrue(criteriaBarGraphMap.equals(expectedCohortMap));
	}

	@After
	public void verify() throws Exception {

		// Mockito.verify(mtldCmRepository, VerificationModeFactory.times(1))
		// .getCriteriaList();

		Mockito.reset(mtldCmRepository);
	}

	public CMBean createCorpMemberBean(int id, String corpsYear,
			Double mtldCMRatioScore, Double balanceCorpsScore) {
		CMBean corpMember = new CMBean();
		Map<Integer, Double> criteriaScoreMap = new HashMap<Integer, Double>();
		criteriaScoreMap.put(1, mtldCMRatioScore);
		criteriaScoreMap.put(2, balanceCorpsScore);
		corpMember.setId(1);

		corpMember.setSubjectModifier("SPED");
		corpMember.setSubjectGroup("ENGLISH");
		corpMember.setGradeLevel("HIGH");
		corpMember.setCmYear(corpsYear);
		corpMember.setIslowIncomeBackground(true);
		corpMember.setCriteriaScore(criteriaScoreMap);
		return corpMember;
	}

	public Cohort createCohort(int regionId) throws Exception {
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		List<CohortDetail> cohortDetailList = new ArrayList<CohortDetail>();
		Region region = new Region();
		region.setRegionId(regionId);
		cohort.setSchoolRep("4");
		setCohortDetailsValues(cohort, "1", "50.0", "1", "20.0", "50.0",
				"50.0", "50.0", "50.0", "10.0", "Craigmont HS");
		cohort.setId(1);
		cohort.setIsFinalCohort(true);
		cohort.setRegion(region);
		cohortDetailList.add(cohortDetail);
		cohort.setCohortDetails(cohortDetailList);
		return cohort;
	}

	private void setCohortDetailsValues(Cohort cohort,
			String schoolDistrictRepresented, String spedModifierPercentage,
			String neighbourhoodRep, String ecePercentage,
			String oneYearCorpPercentage, String twoYearCorpPercentage,
			String charterPartnerPercentage, String districtPartnerPercentage,
			String lowIncomePercentage, String feederPatternHS)
			throws Exception {

		cohort.setSchoolDistrictRepresented(schoolDistrictRepresented);
		cohort.setSpedModifierPercentage(spedModifierPercentage);
		cohort.setNeighbourhoodRepresented(neighbourhoodRep);
		cohort.setEcePercentage(ecePercentage);
		cohort.setOneYearCorpPercentage(oneYearCorpPercentage);
		cohort.setTwoYearCorpPercentage(twoYearCorpPercentage);
		cohort.setCharterPartnerPercentage(charterPartnerPercentage);
		cohort.setDistrictPartnerPercentage(districtPartnerPercentage);
		cohort.setLowIncomePercentage(lowIncomePercentage);
		cohort.setFeederPatternHS(feederPatternHS);
	}

	private CohortDetail createCohortDetail(int id) {
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setId(id);
		cohortDetail.setNotFitForCohort(false);
		return cohortDetail;
	}

	private CorpsMember createCorpMember(int id, String corpsYear) {
		CorpsMember corpMember = new CorpsMember();
		corpMember.setId(1);

		corpMember.setSubjectModifier("SPED");
		corpMember.setSubjectGroup("ENGLISH");
		corpMember.setGradeLevel("HIGH");
		corpMember.setCorpsYear(2012);
		corpMember.setIslowIncomeBackground(true);
		return corpMember;
	}

	public Region createRegion(int id) {
		Region region = new Region();
		region.setRegionId(id);
		return region;
	}

	public School createSchool(int schoolId, String schoolType,
			String neighbourhood, double lat, double log, String feederPatternHS) {
		School school = new School();
		school.setSchoolId(schoolId);
		school.setDistrict("GREENVILLE PUBLIC SCH DIST");
		school.setSchoolType(schoolType);
		school.setNeighborhood(neighbourhood);
		school.setLongitude(lat);
		school.setLatitude(log);
		school.setFeederPatternHS(feederPatternHS);
		return school;
	}

	@SuppressWarnings("unchecked")
	public List<CriteriaBean> createCriteriaList(int i) throws Exception {

		Map<String, List<CriteriaBean>> criteriaMap = new HashMap<String, List<CriteriaBean>>();
		List<CriteriaBean> criteriaBeanList = null;
		List<CriteriaBean> basicCriteriaList = new ArrayList<CriteriaBean>();
		List<CriteriaBean> contentCriteriaList = new ArrayList<CriteriaBean>();
		List<CriteriaBean> geograpicCriteriaList = new ArrayList<CriteriaBean>();
		List<CriteriaBean> relationhipCriteriaList = new ArrayList<CriteriaBean>();

		CriteriaBean criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA);
		criBean.setId(1);
		basicCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.BALANCECORPSYEARSSCORINGCRITERIA);
		criBean.setId(3);
		basicCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MTLDSCHOOLRATIOSCORINGCRITERIA);
		criBean.setId(4);
		basicCriteriaList.add(criBean);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_BASICS,
				basicCriteriaList);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.GRADELEVELSCORINGCRITERIA);
		criBean.setId(8);
		contentCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.SUBJECTGROUPSCORINGCRITERIA);
		criBean.setId(9);
		contentCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.SUBJECTMODIFIERSCORINGCRITERIA);
		criBean.setId(10);
		contentCriteriaList.add(criBean);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_CONTENT,
				contentCriteriaList);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA);
		criBean.setId(12);
		geograpicCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MINIMIZEDISTRICTSSCORINGCRITERIA);
		criBean.setId(13);
		geograpicCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MATCHNEIGHBORHOODSCORINGCRITERIA);
		criBean.setId(14);
		geograpicCriteriaList.add(criBean);
		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MTLDPRIORSCHOOLPLACEMENTSCORINGCRITERIA);
		criBean.setId(16);
		geograpicCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.FEEDERPATTERNSCORINGCRITERIA);
		criBean.setId(17);
		geograpicCriteriaList.add(criBean);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY,
				geograpicCriteriaList);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.CHARTERNETWORKSCORINGCRITERIA);
		criBean.setId(2);
		relationhipCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.PREVIOUSADVISORSCORINGCRITERIA);
		criBean.setId(18);
		relationhipCriteriaList.add(criBean);

		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.PRINCIPALPREFERENCESCORINGCRITERIA);
		criBean.setId(20);
		relationhipCriteriaList.add(criBean);
		criBean = new CriteriaBean();
		criBean.setClassAPI(TFAConstants.MTLDPRIORSCHOOLPLACEMENTSCORINGCRITERIA);
		criBean.setId(21);
		relationhipCriteriaList.add(criBean);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS,
				relationhipCriteriaList);

		if (i == 1) {
			criteriaBeanList = getCriteriaList1(criteriaMap);
		}

		if (i == 10) {
			criteriaBeanList = getCriteriaList10(criteriaMap);
		}

		if (i == 20) {
			criteriaBeanList = getCriteriaList20(criteriaMap);
		}

		if (i == 3) {
			criteriaBeanList = getCriteriaList3(criteriaMap);
		}

		return criteriaBeanList;
	}

	private List getCriteriaList1(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList2(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList3(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList4(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList5(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList6(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList7(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList8(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList10(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList11(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList12(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList13(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList14(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList15(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList16(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList17(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList18(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList19(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList20(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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

				} else if (criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() > 0) {

					scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
							.forName(
									"org.tfa.mtld.scoring."
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList21(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

	private List getCriteriaList(Map<String, List<CriteriaBean>> criteriaList)
			throws Exception {
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
											+ criteriaBean.getClassAPI().trim())
							.newInstance();

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

}