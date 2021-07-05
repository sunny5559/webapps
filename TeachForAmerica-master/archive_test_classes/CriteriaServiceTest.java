package org.tfa.mtld.service.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.tfa.mtld.service.bean.CriteriaBean;

@ContextConfiguration({ "file:src/main/resources/applicationContext-service.xml" })
public class CriteriaServiceTest {

	@Autowired
	public CriteriaService criteriaService;

	@Before
	public void setUp() throws Exception{
		if (criteriaService == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"applicationContext-service.xml");
			criteriaService = (CriteriaService) context
					.getBean("criteriaServiceImpl");
		}
	}

	/*
	 * Below test case is for Criteria Category and Criteria List.Category count
	 * should 4 and Criteria List size should 15.
	 */
	@Test
	public void testGetCriteriaList() throws Exception {
	
		Assert.assertNotNull(criteriaService);

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
			Assert.assertTrue(count == 15);// Total Criteria count should be 15.
		

	}

}
