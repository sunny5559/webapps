package org.tfa.mtld.data.repository;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

public class TFARepositoryImplTest {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@BeforeClass
	public static void setUp() throws IOException, SQLException {
		if (applicationContext == null) {
			applicationContext = new FileSystemXmlApplicationContext(
					new java.io.File(".").getCanonicalPath()
							+ "/src/test/resources/applicationContext-test-repository.xml");
		}
		DataSource dataSource = (DataSource) applicationContext
				.getBean("dataSource");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Resource resource = new ClassPathResource("TFACreateDB.sql");
		JdbcTestUtils.executeSqlScript(jdbcTemplate, resource, true);
	}
	
	@AfterClass
	public static void tearDown() throws IOException, SQLException {
		if (applicationContext == null) {
			applicationContext = new FileSystemXmlApplicationContext(
					new java.io.File(".").getCanonicalPath()
							+ "/src/test/resources/applicationContext-test-repository.xml");
		}
		DataSource dataSource = (DataSource) applicationContext
				.getBean("dataSource");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		Resource resource = new ClassPathResource("TFACleanUpDB.sql");
		JdbcTestUtils.executeSqlScript(jdbcTemplate, resource, true);
	}
	
}
