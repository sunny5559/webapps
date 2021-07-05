package org.tfa.mtld.data.repository;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.tfa.mtld.data.model.Region;

/**
 * @author divesh.solanki
 * 
 */

public class RegionRepositoryImplTest extends TFARepositoryImplTest {

	@Autowired
	public static RegionRepository regionRepository;

	@BeforeClass
	public static void setUp() throws IOException, SQLException {	
		TFARepositoryImplTest.setUp();
		if (regionRepository == null) {
			regionRepository = (RegionRepository) getApplicationContext()
					.getBean("regionRepositoryImpl");
		}

		Assert.assertNotNull(regionRepository);

	}
	@AfterClass
	public static void tearDown() throws SQLException, IOException {
		TFARepositoryImplTest.tearDown();
	}
	@Test
	public void testGetRegionByRegionCode() throws Exception {

		//Temp-Fix: need to re-visit this for surfire plugin
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		//Temp-Fix end her		
		
		Region region = regionRepository.getRegionByRegionCode("CHICAGO");
		Assert.assertNotNull(region);
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
		if (regionRepository == null) {
			regionRepository = (RegionRepository) applicationContext
					.getBean("regionRepositoryImpl");
		}
	}

}
