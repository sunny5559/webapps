package org.tfa.mtld.data.repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.Region;
import org.tfa.mtld.data.repository.MtldCmRepository;

/**
 * @author divesh.solanki
 * 
 */
// @ContextConfiguration({
// "file:src/main/resources/applicationContext-service.xml" })
public class MtldCmRepositoryImplTest extends TFARepositoryImplTest {

	@Autowired
	public static MtldCmRepository mtldCmRepository;

	@BeforeClass
	public static void setUp() throws SQLException, IOException {
		TFARepositoryImplTest.setUp();
		if (mtldCmRepository == null) {

			mtldCmRepository = (MtldCmRepository) getApplicationContext()
					.getBean("mtldCmRepositoryImpl");
			
		}
		Assert.assertNotNull(mtldCmRepository);
	}
	@AfterClass
	public static void tearDown() throws SQLException, IOException {
		TFARepositoryImplTest.tearDown();
	}

	// Will return list of CM including hired and unhired, and only unassigned
	// this will not include any assigned cm which are there in finalize
	// cohort(s).
	@Test
	public void testGetCorpMemberListByRegionId() throws Exception {
		Integer regionId = 15;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<CorpsMember> corps = mtldCmRepository
				.getCorpMemberListByRegionId(regionId);
		if (corps != null) {
			assertEquals(1, corps.size());
		}

	}

	// get all the MTLD for the region
	@Test
	public void testGetMTLDListByRegionId() throws Exception {
		Integer regionId = 11;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<MTLD> mtlds = mtldCmRepository.getMTLDListByRegionId(regionId);
		if (mtlds != null) {
			assertEquals(4, mtlds.size());
		}

	}
	
	// get all the Unassigned MTLD for the region
	@Test
	public void testGetUnassignedMTLDListByRegionId() throws Exception {
		Integer regionId = 11;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<MTLD> mtlds = mtldCmRepository.getUnassignedMTLDListByRegionId(regionId);
		if (mtlds != null) {
			assertEquals(4, mtlds.size());
		}

	}

	// Will return list of CM including only hired, and only unassigned this
	// will not include any assigned corps which are there in finalize
	// cohort(s).
	@Test
	public void testGetHiredCorpMemberListByRegionId() throws Exception {
		Integer regionId = 13;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<CorpsMember> corps = mtldCmRepository
				.getHiredCorpMemberListByRegionId(regionId);
		if (corps != null) {
			assertEquals(2, corps.size());
		}

	}

	// Will return list of CM including unhired, and only unassigned this will
	// not include any assigned cm which are there in finalize cohort(s).
	@Test
	public void testGetUnhiredCorpMemberListByRegionId() throws Exception {
		Integer regionId = 13;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<CorpsMember> corps = mtldCmRepository
				.getUnhiredCorpMemberListByRegionId(regionId);
		if (corps != null) {
			assertEquals(0, corps.size());
		}

	}

	/**
	 * ' This method is used to flush all the cohort records from db which are
	 * not finalize.
	 * */
	@Test
	public void testFlushUnfinalizeCohort() throws Exception {
		Integer regionId = 13;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		mtldCmRepository.flushUnfinalizeCohort(regionId);
	}

	/**
	 * ' This method validates the functionality to save the cohort in database.
	 * 
	 * */
	@Test
	public void testSaveCohort() throws Exception {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Integer regionId = 11;
		Cohort cohort = createCohort(regionId);
		mtldCmRepository.saveCohort(cohort);
		assertEquals("Executed Successful", 1, mtldCmRepository
				.getCohortListByRegionId(regionId).size());
	}

	/**
	 * ' This method validates the functionality to delete the cohortDetail
	 * object from database.
	 * 
	 * */
	@Test
	public void testDeleteCohortDetail() throws Exception {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Integer regionId = 13;
		Cohort newCohort = createCohort(regionId);
		mtldCmRepository.saveCohort(newCohort);
		List<Cohort> cohortList = mtldCmRepository
				.getCohortListByRegionId(regionId);

		Cohort testCohort = null;
		for (Cohort cohort : cohortList) {
			if (cohort.getCreatedBy().equalsIgnoreCase("testuser")) {
				testCohort = cohort;
				break;
			}
		}
		List<CohortDetail> cohortDetails = testCohort.getCohortDetails();
		Iterator<CohortDetail> iterator = cohortDetails.iterator();
		while (iterator.hasNext()) {
			CohortDetail cohortDetail = (CohortDetail) iterator.next();
			mtldCmRepository.deleteCohortDetail(cohortDetail);
			assertEquals("Executed Successful", 0,
					mtldCmRepository.getCohort(testCohort.getId())
							.getCohortDetails().size());
			break;
		}

	}

	/**
	 * ' This method validates the functionality to get the Cohort by cohortId.
	 * 
	 * */
	@Test
	public void testGetCohort() throws Exception {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Integer regionId = 13;
		List<Cohort> cohortList = mtldCmRepository
				.getCohortListByRegionId(regionId);
		Cohort existingCohort = null;
		for (Cohort savedCohort : cohortList) {
			existingCohort = savedCohort;
		}
		if (existingCohort != null) {
			assertEquals("Executed Successful", existingCohort.getId(),
					mtldCmRepository.getCohort(existingCohort.getId()).getId());
		}

	}

	/**
	 * ' This method validates the functionality to get the CorpMember by
	 * corpId.
	 * 
	 * */
	@Test
	public void testGetCorpsMemberById() throws Exception {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Integer regionId = 13;
		List<CorpsMember> corpsList = mtldCmRepository
				.getCorpMemberListByRegionId(regionId);
		CorpsMember existingCorpMember = null;
		for (CorpsMember corpMember : corpsList) {
			existingCorpMember = corpMember;
		}
		if (existingCorpMember != null) {
			assertEquals(
					"Executed Successful",
					existingCorpMember.getId(),
					mtldCmRepository.getCorpsMemberById(
							existingCorpMember.getId()).getId());
		}

	}

	/**
	 * This method validates the functionality to get the mtld by mtldId.
	 * 
	 * */
	@Test
	public void testGetMTLDById() throws Exception {
		Integer regionId = 13;
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		List<MTLD> mtldList = mtldCmRepository.getMTLDListByRegionId(regionId);
		MTLD existingMTLD = null;
		for (MTLD mtld : mtldList) {
			existingMTLD = mtld;
		}
		if (existingMTLD != null) {
			assertEquals("Executed Successful", existingMTLD.getId(),
					mtldCmRepository.getMTLDById(existingMTLD.getId()).getId());
		}
	}

	public Cohort createCohort(int regionId) throws BeansException, IOException {
		if(getApplicationContext() == null ) {
			initializeAndSetData();
		}
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		List<CohortDetail> cohortDetailList = new ArrayList<CohortDetail>();
		Region region = new Region();
		region.setRegionId(regionId);
		region.setRegionCode("CHICAGO");
		region.setRegionName("CHICAGO");
		//cohort.setId(1);
		cohort.setCreatedBy("testuser");
		cohort.setCreatedDate(new Date());
		cohort.setSchoolRep("4");
		cohort.setIsFinalCohort(true);
		cohort.setRegion(region);
		cohortDetailList.add(cohortDetail);
		cohort.setCohortDetails(cohortDetailList);
		return cohort;
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
		if (mtldCmRepository == null) {

			mtldCmRepository = (MtldCmRepository) applicationContext
					.getBean("mtldCmRepositoryImpl");

		}
	}
}
