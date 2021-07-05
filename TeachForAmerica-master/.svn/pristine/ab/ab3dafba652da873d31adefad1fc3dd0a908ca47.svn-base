package org.tfa.mtld.service.services;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.CriteriaCategoryBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;
import org.tfa.mtld.service.bean.SchoolBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CohortUpdateServiceMockTest {
	@Autowired
	public CohortService cohortService;

	@Autowired
	MtldCmRepository mtldCmRepository;


	@Autowired
	public CohortUpdateService cohortUpdateService;

	Cohort expectedCohort;

	CorpsMember expectedCorpMember;

	MTLD expectedMtld;

	CriteriaFormBean criteriaFormBean;


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

		@Bean
		public CohortUpdateServiceImpl cohortUpdateService() {
			return new CohortUpdateServiceImpl();
		}
	}

	@Before
	public void setUp() throws Exception {
		// expected object for testGetCohortDetails
		expectedCohort = createCohort(1, "1", "1", "Neighborhood2");
		CohortDetail cohortDetail = createCohortDetail(1);
		CorpsMember corpMember = createCorpMember(1, 2012);
		expectedMtld = createMtld(1);
		School school = createSchool(1, "DISTRICT", "Neighborhood2");
		corpMember.setSchool(school);
		Region region = createRegion(1);
		cohortDetail.setCorpMember(corpMember);
		List<CohortDetail> cohortDetailSet = new ArrayList<CohortDetail>();
		cohortDetailSet.add(cohortDetail);
		expectedCohort.setRegion(region);
		expectedCohort.setCohortDetails(cohortDetailSet);
		expectedCohort.setMtld(expectedMtld);
		criteriaFormBean = createCriteriaList();
		

		expectedCorpMember = createCorpMember(1, 2012);
		expectedCorpMember.setSchool(school);
		// expected object for
	}

	@After
	public void verify() {
		Mockito.reset(mtldCmRepository);
	}

	/*
	 * Below test case checks the method getCohortDetails which fetches the
	 * cohort object from database for the specified cohortId.
	 */
	@Test
	public void testGetCohortDetails() throws Exception {
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		CohortBean cohortBean = cohortUpdateService.getCohortDetails(
				expectedCohort.getId(), criteriaFormBean);
		// Assert.assertEquals("Executed Successful", , expectedcohortBean);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method saveUpdatedCohort which updates the
	 * finalCohort flag as true in database for the specified cohort.
	 */
	@Test
	public void testSaveUpdatedCohort() throws TFAInvalidCohortException,
			Exception {
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		CohortBean cohortBean = cohortUpdateService.saveUpdatedCohort(
				expectedCohort.getId(), criteriaFormBean, null);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method unlockCohort which updates the
	 * finalCohort flag as false in database for the specified cohort.
	 */
	@Test
	public void testUnlockCohort() throws Exception {
		expectedCohort.setIsFinalCohort(false);
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		CohortBean cohortBean = cohortUpdateService.unlockCohort(
				expectedCohort.getId(), null);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method addCMToCohort which adds the passed CM
	 * to the specified cohort.
	 */
	@Test
	public void testAddCMToCohort() throws Exception {
		expectedCohort.setCohortDetails(null);
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		Mockito.when(mtldCmRepository.getCorpsMemberById(1)).thenReturn(
				expectedCorpMember);

		CohortBean cohortBean = cohortUpdateService.addCMToCohort(
				true, 1, expectedCohort.getId(), criteriaFormBean);

		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method removeCMFromCohort which removes the
	 * passed CM to the specified cohort.
	 */
	@Test
	public void testRemoveCMFromCohort() throws Exception {
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		Mockito.when(mtldCmRepository.getCorpsMemberById(1)).thenReturn(
				expectedCorpMember);
		CohortBean cohortBean = cohortUpdateService.removeCMFromCohort(1,
				expectedCohort.getId(), criteriaFormBean);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method addSeededMember which adds the seeded
	 * MTLD in the specified cohort.
	 */
	@Test
	public void testAddSeededMember() throws Exception {
		expectedCohort.setMtld(null);
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		Mockito.when(mtldCmRepository.getMTLDById(1)).thenReturn(expectedMtld);
		CohortBean cohortBean = cohortUpdateService.addSeededMember(1, 1,
				criteriaFormBean);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	/*
	 * Below test case checks the method removeSeededMember which removes the
	 * specified MTLD from the cohort.
	 */
	@Test
	public void testRemoveSeededMember() throws Exception {
		Mockito.when(mtldCmRepository.getCohort(1)).thenReturn(expectedCohort);
		Mockito.when(mtldCmRepository.getMTLDById(1)).thenReturn(expectedMtld);
		CohortBean cohortBean = cohortUpdateService.removeSeededMember("hired",
				1, 1, criteriaFormBean);
		CohortBean expectedcohortBean = new CohortBean();
		cohortService.convertCohortToCohortBean(expectedCohort,
				expectedcohortBean);
		assertTrue(equals(cohortBean, expectedcohortBean));
	}

	public CriteriaFormBean createCriteriaList() {
		CriteriaFormBean criteriaFormBean = new CriteriaFormBean();
		List<CriteriaCategoryBean> cateoryBeanList = new ArrayList<CriteriaCategoryBean>();
		CriteriaCategoryBean criteriaCategoryBean = new CriteriaCategoryBean();
		criteriaCategoryBean.setCategoryName(TFAConstants.CRITERIA_CATEGORY_BASICS);
		
		
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();

		CriteriaBean criteriaBean = new CriteriaBean();

		criteriaBean.setClassAPI("MTLDToCMRatioScoringCriteria");
		criteriaBean.setDescription(null);
		criteriaBean.setFieldPlaceholder("#CMs/MTLD");
		criteriaBean.setFieldType("Text Field");
		criteriaBean.setFieldValidation("number_two_digit");
		criteriaBean.setFieldRequired(true);
		criteriaBean.setId(1);
		criteriaBean.setName("Restrict MTLD-CM ratio");
		criteriaBean.setPriorityValue(4);
		criteriaBeanList.add(criteriaBean);
		CriteriaBean criteriaBean2 = new CriteriaBean();

		criteriaBean2.setClassAPI("BalanceCorpsYearsScoringCriteria");
		criteriaBean2.setDescription(null);
		criteriaBean2.setFieldPlaceholder("#CMs/MTLD");
		criteriaBean2.setFieldType("Text Field");
		criteriaBean2.setFieldValidation("number_two_digit");
		criteriaBean2.setFieldRequired(true);
		criteriaBean2.setPriorityValue(4);
		criteriaBean2.setId(2);
		criteriaBean2
				.setName("Balance cohorts for 1CM and 2CM based on user defined parameter");
		criteriaBeanList.add(criteriaBean2);
		
		criteriaCategoryBean.setCriteriaBeans(criteriaBeanList);
		cateoryBeanList.add(criteriaCategoryBean);
		criteriaFormBean.setCriteriaCategoryBeanList(cateoryBeanList);
		
		return criteriaFormBean;
	}

	public Map<String, List<CriteriaBean>> createCriteriaMap() {
		Map<String, List<CriteriaBean>> criteriaMap = new LinkedHashMap<String, List<CriteriaBean>>();

		List<CriteriaBean> basicCriteriaBeanList = new ArrayList<CriteriaBean>();

		CriteriaBean c1 = new CriteriaBean();
		c1.setClassAPI("MTLDToCMRatioScoringCriteria");
		c1.setDescription(null);
		c1.setFieldPlaceholder("#CMs/MTLD");
		c1.setFieldType("Text Field");
		c1.setFieldValidation("number_two_digit");
		c1.setFieldRequired(true);
		c1.setId(1);
		c1.setName("Restrict MTLD-CM ratio");
		c1.setDescription(null);
		basicCriteriaBeanList.add(c1);

		CriteriaBean c2 = new CriteriaBean();
		c2.setClassAPI("BalanceCorpsYearsScoringCriteria");
		c2.setFieldPlaceholder(null);
		c2.setFieldType(null);
		c2.setFieldValidation(null);
		c2.setFieldRequired(false);
		c2.setId(3);
		c2.setName("Balance cohorts for 1CM and 2CM based on user defined parameter");
		c2.setDescription(null);
		basicCriteriaBeanList.add(c2);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_BASICS,
				basicCriteriaBeanList);

		List<CriteriaBean> contentCriteriaBeanList = new ArrayList<CriteriaBean>();

		CriteriaBean content = new CriteriaBean();
		content.setClassAPI("GradeLevelScoringCriteria");
		content.setFieldPlaceholder(null);
		content.setFieldType(null);
		content.setFieldValidation(null);
		content.setFieldRequired(null);
		content.setId(8);
		content.setName("Match CMs with same grade level");
		content.setDescription("(e.g. ELEM, MS, HS)");
		contentCriteriaBeanList.add(content);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_CONTENT,
				contentCriteriaBeanList);

		List<CriteriaBean> geographicCriteriaBeanList = new ArrayList<CriteriaBean>();
		CriteriaBean geographic = new CriteriaBean();
		geographic.setClassAPI("MinimizeTravelDistanceScoringCriteria");
		geographic.setFieldPlaceholder("MTLD Distance");
		geographic.setFieldType("Text Field");
		geographic.setFieldValidation("decimal_tenth_place");
		geographic.setFieldRequired(true);
		geographic.setId(12);
		geographic.setName("Minimize MTLD Travel Distance");
		geographic.setDescription("(e.g. distance between CMs)");
		geographicCriteriaBeanList.add(geographic);

		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY,
				geographicCriteriaBeanList);

		List<CriteriaBean> relationshipsCriteriaBeanList = new ArrayList<CriteriaBean>();
		CriteriaBean relationships = new CriteriaBean();
		relationships.setClassAPI("CharterNetworkScoringCriteria");
		relationships.setFieldPlaceholder(null);
		relationships.setFieldType(null);
		relationships.setFieldValidation(null);
		relationships.setFieldRequired(null);
		relationships.setId(2);
		relationships.setName("Match CMs at same CMO");
		relationships.setDescription(null);
		relationshipsCriteriaBeanList.add(relationships);
		criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS,
				relationshipsCriteriaBeanList);
		return criteriaMap;
	}

	public boolean equals(Object o1, Object o2) {
		CohortBean cohortBean1 = (CohortBean) o1;
		CohortBean cohortBean2 = (CohortBean) o2;
		if (cohortBean1.getId() == cohortBean2.getId()
				&& cohortBean1.getIsFinalCohort() == cohortBean2
						.getIsFinalCohort()
				&& cohortBean1.getCharterPartnerPercentage().equalsIgnoreCase(
						cohortBean2.getCharterPartnerPercentage())
				&& cohortBean1.getEcePercentage().equalsIgnoreCase(
						cohortBean2.getEcePercentage())
				&& cohortBean1.getElemPercentage().equalsIgnoreCase(
						cohortBean2.getElemPercentage())
				&& cohortBean1.getHsGradePercentage().equalsIgnoreCase(
						cohortBean2.getHsGradePercentage())
				&& cohortBean1.getMsGradePercentage().equalsIgnoreCase(
						cohortBean2.getMsGradePercentage())
				&& cohortBean1.getSpedModifierPercentage().equalsIgnoreCase(
						cohortBean2.getSpedModifierPercentage())
				&& cohortBean1.getTwoYearCorpPercentage().equalsIgnoreCase(
						cohortBean2.getTwoYearCorpPercentage())
				&& cohortBean1.getDistrictPartnerPercentage().equalsIgnoreCase(
						cohortBean2.getDistrictPartnerPercentage())
				&& cohortBean1.getLowIncomePercentage().equalsIgnoreCase(
						cohortBean2.getLowIncomePercentage())
				&& cohortBean1.getOneYearCorpPercentage().equalsIgnoreCase(
						cohortBean2.getOneYearCorpPercentage())
				&& cohortBean1.getNeighbourhoodRepresented().equalsIgnoreCase(
						cohortBean2.getNeighbourhoodRepresented())
				&& cohortBean1.getSchoolDistrictRepresented().equalsIgnoreCase(
						cohortBean2.getSchoolDistrictRepresented())
				&& cohortBean1.getSchoolRep().equalsIgnoreCase(
						cohortBean2.getSchoolRep())
				&& cohortBean1.getCohortDetailBean().size() == cohortBean2
						.getCohortDetailBean().size()
				&& ((cohortBean1.getSeededMtldBean() != null && cohortBean2
						.getSeededMtldBean() != null) ? cohortBean1
						.getSeededMtldBean().getId() == cohortBean2
						.getSeededMtldBean().getId() : (cohortBean1
						.getSeededCMBean() != null && cohortBean2
						.getSeededCMBean() != null) ? cohortBean1
						.getSeededCMBean().getId() == cohortBean2
						.getSeededCMBean().getId() : true)) {

			if (cohortBean1.getCohortDetailBean().size() != 0) {
				for (int i = 0; i < cohortBean1.getCohortDetailBean().size(); i++) {
					CohortDetailBean cohortDetailBean1 = cohortBean1
							.getCohortDetailBean().get(i);
					CohortDetailBean cohortDetailBean2 = cohortBean2
							.getCohortDetailBean().get(i);
					if (cohortDetailBean1.isNotFitForCohort() == cohortDetailBean2
							.isNotFitForCohort()) {
						CMBean corpMember1 = cohortDetailBean1.getCorpsMember();
						CMBean corpMember2 = cohortDetailBean2.getCorpsMember();
						if (corpMember1.getId() == corpMember2.getId()
								&& corpMember1.getSchoolBean().getSchoolId() == corpMember2
										.getSchoolBean().getSchoolId()
								&& corpMember1.getSubjectModifier()
										.equalsIgnoreCase(
												corpMember2
														.getSubjectModifier())
								&& corpMember1.getSubjectGroup()
										.equalsIgnoreCase(
												corpMember2.getSubjectGroup())
								&& corpMember1.getGradeLevel()
										.equalsIgnoreCase(
												corpMember2.getGradeLevel())
								&& corpMember1.getCmYear().equalsIgnoreCase(
										corpMember2.getCmYear())
								&& corpMember1.getIslowIncomeBackground() == corpMember2
										.getIslowIncomeBackground()) {
							SchoolBean schoolBean1 = corpMember1
									.getSchoolBean();
							SchoolBean schoolBean2 = corpMember2
									.getSchoolBean();
							if (schoolBean1.getSchoolId() == schoolBean2
									.getSchoolId()
									&& schoolBean1.getDistrict()
											.equalsIgnoreCase(
													schoolBean2.getDistrict())
									&& schoolBean1
											.getSchoolType()
											.equalsIgnoreCase(
													schoolBean2.getSchoolType())
									&& schoolBean1.getNeighborhood()
											.equalsIgnoreCase(
													schoolBean2
															.getNeighborhood())
									&& schoolBean1.getLongitude() == schoolBean2
											.getLongitude()
									&& schoolBean1.getLatitude() == schoolBean2
											.getLatitude()) {
								return true;
							}
						}
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public Cohort createCohort(int id, String schoolRep,
			String schoolDistrictRepresented, String neighbourhoodRep) {
		Cohort cohort = new Cohort();
		cohort.setId(id);
		cohort.setCharterPartnerPercentage("0.0");
		cohort.setEcePercentage("0.0");
		cohort.setElemPercentage("0.0");
		cohort.setHsGradePercentage("100.0");
		cohort.setMsGradePercentage("0.0");
		cohort.setSpedModifierPercentage("100.0");
		cohort.setTwoYearCorpPercentage("100.0");
		cohort.setDistrictPartnerPercentage("100.0");
		cohort.setLowIncomePercentage("100.0");
		cohort.setOneYearCorpPercentage("0.0");
		cohort.setNeighbourhoodRepresented(neighbourhoodRep);
		cohort.setSchoolDistrictRepresented(schoolDistrictRepresented);
		cohort.setSchoolRep(schoolRep);
		cohort.setIsFinalCohort(true);
		return cohort;
	}

	public CohortDetail createCohortDetail(int id) {
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCriteriaScore("1=1.0,3=.89,8=.75,12=0.5");
		cohortDetail.setId(id);
		cohortDetail.setNotFitForCohort(false);
		return cohortDetail;
	}

	public CorpsMember createCorpMember(int id, Integer corpsYear) {
		CorpsMember corpMember = new CorpsMember();
		corpMember.setId(id);

		corpMember.setSubjectModifier("SPED");
		corpMember.setSubjectGroup("ENGLISH");
		corpMember.setGradeLevel("HIGH");
		corpMember.setCorpsYear(corpsYear);
		corpMember.setIslowIncomeBackground(true);
		return corpMember;
	}

	public Region createRegion(int id) {
		Region region = new Region();
		region.setRegionId(id);
		return region;
	}

	public School createSchool(int schoolId, String schoolType,
			String neighbourhood) {
		School school = new School();
		school.setSchoolId(schoolId);
		school.setDistrict("GREENVILLE PUBLIC SCH DIST");
		school.setSchoolType(schoolType);
		school.setNeighborhood(neighbourhood);
		school.setLongitude(0.0);
		school.setLatitude(0.0);
		school.setFeederPatternHS(null);
		return school;
	}

	public MTLD createMtld(int id) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		return mtld;
	}
}
