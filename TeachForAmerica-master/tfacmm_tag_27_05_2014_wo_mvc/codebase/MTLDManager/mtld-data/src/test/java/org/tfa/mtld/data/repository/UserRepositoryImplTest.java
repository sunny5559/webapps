package org.tfa.mtld.data.repository;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
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
import org.tfa.mtld.data.model.User;

/**
 * @author divesh.solanki
 * 
 */

public class UserRepositoryImplTest extends TFARepositoryImplTest {

	@Autowired
	private static UserRepository userRepositoryImpl;

	@BeforeClass
	public static void setup() throws SQLException {
		if (userRepositoryImpl == null) {
			userRepositoryImpl = (UserRepository) getApplicationContext()
					.getBean("userRepositoryImpl");
		}
		Assert.assertNotNull(userRepositoryImpl);

	}
	@AfterClass
	public static void tearDown() throws SQLException, IOException {
		TFARepositoryImplTest.tearDown();
	}
	
	@Test
	public void testUserLogin() throws Exception {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Assert.assertNotNull(userRepositoryImpl);
		User user = userRepositoryImpl.userLogin("tfa", "tfa");
		Assert.assertNotNull(user);
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
		if (userRepositoryImpl == null) {
			userRepositoryImpl = (UserRepository) applicationContext
					.getBean("userRepositoryImpl");
		}
	}
	
}
