/**
 * 
 */
package org.tfa.mtld.data.repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
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
import org.tfa.mtld.data.model.School;

/**
 * @author arun.rathore
 *
 */
public class SchoolRepositoryImplTest extends TFARepositoryImplTest {
	
	@Autowired
	public static SchoolRepositoryImpl schoolRepositoryImpl;

	@BeforeClass
	public static void setUp() throws SQLException, IOException {
		TFARepositoryImplTest.setUp();
		if (schoolRepositoryImpl == null) {
			schoolRepositoryImpl = (SchoolRepositoryImpl) getApplicationContext()
					.getBean("schoolRepositoryImpl");
		}

		Assert.assertNotNull(schoolRepositoryImpl);

	}
	@AfterClass
	public static void tearDown() throws SQLException, IOException {
		TFARepositoryImplTest.tearDown();
	}
	//public List<School> getSchoolDetails(Integer regionId) throws Exception; 
	
	/* Will return list of School for a particular 
	 * Region
	*/
		@Test
		public void testGetSchoolDetailsListByRegionId() throws Exception {
			Integer regionId = 11;
			if(getApplicationContext() == null ) {
				initializeAndSetData();
			}
			List<School> schools = schoolRepositoryImpl
					.getSchoolDetails(regionId);
			if (schools != null) {
				assertEquals(5, schools.size());
			}

		}
		
		/* Will return list of School
	    * 
		*/
		@Test
		public void testGetSchoolDetailsList() throws Exception {
			if(getApplicationContext() == null ) {
				initializeAndSetData();
			}
			List<School> schools = schoolRepositoryImpl
					.getSchoolDetails();
			if (schools != null) {
				assertEquals(5, schools.size());
			}

		}
		
		//public void updateSchoolLatLong(School school) throws Exception; 
		/* Will update the lat and long for schools
		*/
		@Test
		public void testUpdateSchoolLatLong() throws Exception {
			School school = new School();
			school.setSchoolId(193778);
			school.setAddress("4826 W ERIE ST"); 
			school.setLatitude(41.8922696);
			school.setLongitude(-87.7086474);
			school.setSchoolId(193777);
			school.setAddress("8045 S KENWOOD AVE");
			school.setLatitude(41.7485369);
			school.setLongitude(-87.5914254);
			if(getApplicationContext() == null ) {
				initializeAndSetData();
			}
			schoolRepositoryImpl
					.updateSchoolLatLong(school);
			 
			 List<School> schoolsResult = schoolRepositoryImpl
						.getSchoolDetails();
			 if (schoolsResult != null) {
				 for (int i =0; i < schoolsResult.size(); i ++ ) {
					 if(school.getSchoolId() == schoolsResult.get(i).getSchoolId()){
					assertEquals(school.getAddress(), schoolsResult.get(i).getAddress());
					assertEquals(school.getLatitude(), schoolsResult.get(i).getLatitude());
					assertEquals(school.getLongitude(), schoolsResult.get(i).getLongitude());
					 }
				}
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
			if (schoolRepositoryImpl == null) {

				schoolRepositoryImpl = (SchoolRepositoryImpl) applicationContext
						.getBean("schoolRepositoryImpl");

			}
		}
		

}
