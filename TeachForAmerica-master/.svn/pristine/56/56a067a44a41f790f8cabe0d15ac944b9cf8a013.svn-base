package org.tfa.mtld.service.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
import org.tfa.mtld.data.model.Region;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.data.repository.SchoolRepository;
import org.tfa.mtld.service.bean.SchoolBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SchoolServiceMockTest {
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private SchoolRepository schoolRepository;
	

	
	SchoolBean schoolBean;
	School expectedSchool;
	
	@Configuration
	static class SchoolServiceMockTestContextConfiguration {
		@Bean
		public SchoolServiceImpl schoolService() {
			return new SchoolServiceImpl();
		}

		@Bean
		public SchoolRepository userRepository() {
			return Mockito.mock(SchoolRepository.class);
		}
	}
	
	@Before
	public void setup() {
		 List<School> schoolDetails = new ArrayList<School>();
		expectedSchool = new School();
		expectedSchool.setSchoolId(183);
		expectedSchool.setAddress("4826 W ERIE ST");
		expectedSchool.setLatitude(41.8922696);
		expectedSchool.setLongitude(-87.7465888);
		expectedSchool.setSchoolId(184);
		expectedSchool.setAddress("1700 W 83RD ST");
		expectedSchool.setLatitude(41.7430469);
		expectedSchool.setLongitude(-87.6656924);
		Region region = new Region();
		region.setRegionId(13);
		expectedSchool.setRegion(region);
		schoolDetails.add(expectedSchool);

		try {
			Mockito.when( schoolRepository.getSchoolDetails(13))
					.thenReturn(schoolDetails);
			Mockito.when( schoolRepository.getSchoolDetails())
			.thenReturn(schoolDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSchoolDetailsListByRegionId() throws Exception {
		Integer regionId = 13;
		List<School> schools = schoolService
				.getSchoolDetails(regionId);
		if (schools != null) {
			assertEquals(1, schools.size());
		}

	}
	
	@Test
	public void testGetSchoolLatLongWithNull() throws Exception {
		Integer regionId = 13;
		List<School> schools = schoolService
				.getSchoolDetails(regionId);
		if (schools != null) {
			assertEquals(1, schools.size());
		}

	}
	

	
	@Test
	public void testGetSchoolDetails() throws Exception{
		List<School> schools = schoolService
				.getSchoolDetails();
		if (schools != null) {
			assertEquals(1, schools.size());
		}
	}
	
	@Test
	public void testUpdateSchoolLatLong() throws Exception{
		School school = new School();
		school.setSchoolId(183);
		school.setAddress("4826 W ERIE ST"); 
		school.setLatitude(41.8922696);
		school.setLongitude(-87.7086474);
		
		schoolService.updateSchoolLatLong(school);
		
		List<School> schoolsResult = schoolService
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
	
	@Test
	public void testUpdateSchoolDetails() throws Exception{
		School school = new School();
		school.setSchoolId(183);
		school.setAddress("4826 W ERIE Street"); 
		school.setLatitude(41.8922696);
		school.setLongitude(-87.7086474);
		Region region = new Region();
		region.setRegionId(13);
		school.setRegion(region);
		schoolService.updateSchoolDetails(school.getRegion().getRegionId());
		List<School> schoolsResult = schoolService
				.getSchoolDetails(school.getRegion().getRegionId());
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
	
	@Test
	public void testUpdateSchoolDetailsOnStartUp() throws Exception{
		School school = new School();
		school.setSchoolId(183);
		school.setAddress("4826 W ERIE Street"); 
		school.setLatitude(41.8922696);
		school.setLongitude(-87.7086474);
		Region region = new Region();
		region.setRegionId(13);
		school.setRegion(region);
		schoolService.updateSchoolDetails(school.getRegion().getRegionId());
		List<School> schoolsResult = schoolService
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
	
	
	@After
	public void verify() {
		Mockito.reset(schoolRepository);
	}
}
