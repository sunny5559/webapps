package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.tfa.mtld.data.model.Criteria;
import org.tfa.mtld.data.model.CriteriaCategory;
import org.tfa.mtld.data.repository.CriteriaRepository;
import org.tfa.mtld.service.bean.CriteriaBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CriteriaServiceMockTest {

	@Autowired
	private CriteriaService criteriaService;

	@Autowired
	private CriteriaRepository criteriaRepository;


	List<CriteriaCategory> expectedModelList;

	@Configuration
	static class CriteriaServiceMockTestContextConfiguration {

		@Bean
		public CriteriaRepository criteriaRepository() {
			return Mockito.mock(CriteriaRepository.class);
		}

		@Bean
		public CriteriaServiceImpl criteriaService() {
			return new CriteriaServiceImpl();
		}

	}

	@Before
	public void setUp() throws Exception {
		expectedModelList = new ArrayList<CriteriaCategory>();
		CriteriaCategory basicCategory = new CriteriaCategory();
		basicCategory.setName("Basics");
		basicCategory.setId(1);
		

		List<Criteria> basicCriteria = new ArrayList<Criteria>();
		Criteria basic1 = new Criteria();
		basic1.setClassAPI("MTLDToCMRatioScoringCriteria");
		basic1.setCriteriaCategory(basicCategory);
		basic1.setDescription(null);
		basic1.setFieldPlaceholder("#CMs/MTLD");
		basic1.setFieldType("Text Field");
		basic1.setFieldValidation("number_two_digit");
		basic1.setFiledRequired(true);
		basic1.setId(1);
		basic1.setName("Restrict MTLD-CM ratio");
		basic1.setDescription(null);

		basicCriteria.add(basic1);

		Criteria basic2 = new Criteria();
		basic2.setClassAPI("BalanceCorpsYearsScoringCriteria");
		basic2.setCriteriaCategory(basicCategory);
		basic2.setFieldPlaceholder(null);
		basic2.setFieldType(null);
		basic2.setFieldValidation(null);
		basic2.setFiledRequired(false);
		basic2.setId(3);
		basic2.setName("Balance cohorts for 1CM and 2CM based on user defined parameter");
		basic2.setDescription(null);

		basicCriteria.add(basic2);
		basicCategory.setCriteriaList(basicCriteria);
		expectedModelList.add(basicCategory);

		CriteriaCategory contentCategory = new CriteriaCategory();
		contentCategory.setName("Content");
		contentCategory.setId(2);

		List<Criteria> contentCriteria = new ArrayList<Criteria>();
		Criteria content1 = new Criteria();
		content1.setClassAPI("GradeLevelScoringCriteria");
		content1.setCriteriaCategory(contentCategory);
		content1.setFieldPlaceholder(null);
		content1.setFieldType(null);
		content1.setFieldValidation(null);
		content1.setFiledRequired(null);
		content1.setId(8);
		content1.setName("Match CMs with same grade level");
		content1.setDescription("(e.g. ELEM, MS, HS)");

		contentCriteria.add(content1);
		contentCategory.setCriteriaList(contentCriteria);
		expectedModelList.add(contentCategory);

		CriteriaCategory geographicCategory = new CriteriaCategory();
		geographicCategory.setName("Geographic");
		geographicCategory.setId(3);

		List<Criteria> geographicCriteria = new ArrayList<Criteria>();
		Criteria geographic1 = new Criteria();
		geographic1.setClassAPI("MinimizeTravelDistanceScoringCriteria");
		geographic1.setCriteriaCategory(geographicCategory);
		geographic1.setFieldPlaceholder("MTLD Distance");
		geographic1.setFieldType("Text Field");
		geographic1.setFieldValidation("decimal_tenth_place");
		geographic1.setFiledRequired(true);
		geographic1.setId(12);
		geographic1.setName("Minimize MTLD Travel Distance");
		geographic1.setDescription("(e.g. distance between CMs)");

		geographicCriteria.add(geographic1);
		geographicCategory.setCriteriaList(geographicCriteria);
		expectedModelList.add(geographicCategory);

		CriteriaCategory relationshipsCategory = new CriteriaCategory();
		relationshipsCategory.setName("Relationships");
		relationshipsCategory.setId(4);

		List<Criteria> relationshipsCriteria = new ArrayList<Criteria>();
		Criteria relationships1 = new Criteria();
		relationships1.setClassAPI("CharterNetworkScoringCriteria");
		relationships1.setCriteriaCategory(relationshipsCategory);
		relationships1.setFieldPlaceholder(null);
		relationships1.setFieldType(null);
		relationships1.setFieldValidation(null);
		relationships1.setFiledRequired(null);
		relationships1.setId(2);
		relationships1.setName("Match CMs at same CMO");
		relationships1.setDescription(null);

		relationshipsCriteria.add(relationships1);
		relationshipsCategory.setCriteriaList(relationshipsCriteria);
		expectedModelList.add(3, relationshipsCategory);
	}

	@Test()
	public void testGetCriteriaList() throws Exception {
		Assert.assertNotNull(criteriaService);

		Mockito.when(criteriaRepository.getCriteriaList()).thenReturn(
				expectedModelList);

		HashMap<String, List<CriteriaBean>> criteriaMap = criteriaService
				.getCriteriaList();

		Assert.assertNotNull(criteriaMap);
		Assert.assertTrue(criteriaMap.size() == 4); // Criteria Categories
													// count should be 4.
		int count = 0;
		for (Map.Entry<String, List<CriteriaBean>> entry : criteriaMap
				.entrySet()) {
			count += entry.getValue().size();

		}
		Assert.assertTrue(count == 5);// Total Criteria count should be 5.
	}

	@After
	public void verify() throws Exception {
	
//			Mockito.verify(criteriaRepository, VerificationModeFactory.times(1))
//					.getCriteriaList();
		
		Mockito.reset(criteriaRepository);
	}

}
