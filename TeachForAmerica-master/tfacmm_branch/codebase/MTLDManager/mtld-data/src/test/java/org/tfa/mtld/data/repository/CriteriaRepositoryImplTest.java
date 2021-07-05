package org.tfa.mtld.data.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.tfa.mtld.data.model.CriteriaCategory;

/**
 * @author divesh.solanki
 * 
 */

public class CriteriaRepositoryImplTest extends TFARepositoryImplTest {

	@Autowired
	public static CriteriaRepository criteriaRepository;

	@BeforeClass
	public static void setUp() throws SQLException, IOException {
		TFARepositoryImplTest.setUp();
		if (criteriaRepository == null) {
			criteriaRepository = (CriteriaRepository) getApplicationContext()
					.getBean("criteriaRepositoryImpl");
		}

		Assert.assertNotNull(criteriaRepository);

	}
	@AfterClass
	public static void tearDown() throws SQLException, IOException {
		TFARepositoryImplTest.tearDown();
	}
	/*
	 * Below test case is for criteria categories.Total criteria categories
	 * should 4 in count values should be Basics , Geographic , Content ,
	 * Relationships.
	 */
	@Test
	public void testGetCriteriaList() throws Exception {
		
		if(getApplicationContext() == null ){
			initializeAndSetData();
		}
		List<CriteriaCategory> criteriaCategories = criteriaRepository
				.getCriteriaList();
		Assert.assertNotNull(criteriaCategories);
		Assert.assertTrue(criteriaCategories.size() > 0);
		Assert.assertTrue(criteriaCategories.size() == 4); // Criteria
															// Categories count
															// should be 4.
	
		// Iterating list to match each category name

		for (CriteriaCategory category : criteriaCategories) {
			
			Assert.assertTrue(category.getName().equalsIgnoreCase("Basics")
					|| category.getName().equalsIgnoreCase("Geographic")
					|| category.getName().equalsIgnoreCase("Content")
					|| category.getName().equalsIgnoreCase("Relationships"));

		}

	}
	
	public void initializeAndSetData() throws BeansException, IOException{
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
				new java.io.File(".").getCanonicalPath()
				+ "/src/test/resources/applicationContext-test-repository.xml");

		DataSource dataSource = (DataSource) applicationContext
				.getBean("dataSource");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Resource resource = new ClassPathResource("TFACreateDB.sql");
		JdbcTestUtils.executeSqlScript(jdbcTemplate, resource, true);
		if (criteriaRepository == null) {
			criteriaRepository = (CriteriaRepository) applicationContext
					.getBean("criteriaRepositoryImpl");
		}
	}
}
