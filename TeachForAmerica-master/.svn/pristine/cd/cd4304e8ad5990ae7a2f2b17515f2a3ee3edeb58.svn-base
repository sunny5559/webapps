package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 11:19 AM To
 * change this template use File | Settings | File Templates.
 */
public class MinimizeTravelDistanceScoringCriteriaTest {

	private MinimizeTravelDistanceScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new MinimizeTravelDistanceScoringCriteria(25.0);
	}

	@Test
	public void testMtldDistanceToCorpsMember() {
		MTLD mtld = createMtld(2000, 45.4132, 45.1234);

		School school1 = createSchool(3001, 45.5511, 45.5213);
		CorpsMember cm1 = createCm(1001, school1);
		assertEquals(1, criteria.scoreMtldToCorpsMember(mtld, cm1), 0.0);

		School school2 = createSchool(3002, 45.6511, 45.6213);
		CorpsMember cm2 = createCm(1002, school2);
		assertEquals((25.0 / 29.2026),
				criteria.scoreMtldToCorpsMember(mtld, cm2), 0.1);

		School school3 = createSchool(3003, 46.4132, 46.1234);
		CorpsMember cm3 = createCm(1003, school3);
		assertEquals((25.0 / 84.2617),
				criteria.scoreMtldToCorpsMember(mtld, cm3), 0.1);
	}

	// Test scoring corps members to cohorts

	// Test a CM within distance of everyone in the cohort
	@Test
	public void testCorpsMemberWithinDistanceOfCohort() {
		School school1 = createSchool(3001, 45.1234, 45.1234);
		School school2 = createSchool(3002, 45.1245, 45.1245);
		School school3 = createSchool(3003, 45.2345, 45.2345);
		CorpsMember cm1 = createCm(1001, school1);
		CorpsMember cm2 = createCm(1002, school1);
		CorpsMember cm3 = createCm(1003, school2);
		CorpsMember cm4 = createCm(1004, school3);

		CorpsMember cm = createCm(1005, school3);

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test a CM outside distance of some people in the cohort
	@Test
	public void testCorpsMemberWithinDistanceOfSomeOfCohort() {
		School school1 = createSchool(3001, 45.1234, 45.1234);
		School school2 = createSchool(3002, 45.1245, 45.1245);
		School school3 = createSchool(3003, 45.5345, 45.5345);
		CorpsMember cm1 = createCm(1001, school1);
		CorpsMember cm2 = createCm(1002, school1);
		CorpsMember cm3 = createCm(1003, school2);
		CorpsMember cm4 = createCm(1004, school3);

		CorpsMember cm = createCm(1005, school1);

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(((1.0 + 1.0 + 1.0 + (25.0 / 34.7593)) / 4),
				criteria.scoreCorpsMemberToCohort(cm, cohort), 0.1);
		assertEquals(((1.0 + 1.0 + (25.0 / 34.7593)) / 3),
				criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.1);
		assertEquals((((25 / 34.7593) + (25 / 34.7593) + (25 / 34.6662)) / 3),
				criteria.scoreCorpsMemberToCohort(cm4, cohort), 0.1);
	}

	// Test blank inputs
	@Test
	public void testCmToCohortWithBlanks() {
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(null, null), 0.0);
		School school = createSchool(3000, 45.1234, 45.1234);
		CorpsMember cm = createCm(1000, school);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		Cohort cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test MTLD to cohort scoring

	// Test an MTLD within distance of everyone in the cohort
	@Test
	public void testMtldWithinDistanceOfCohort() {
		School school1 = createSchool(3001, 45.1234, 45.1234);
		School school2 = createSchool(3002, 45.1245, 45.1245);
		School school3 = createSchool(3003, 45.2345, 45.2345);
		CorpsMember cm1 = createCm(1001, school1);
		CorpsMember cm2 = createCm(1002, school1);
		CorpsMember cm3 = createCm(1003, school2);
		CorpsMember cm4 = createCm(1004, school3);

		MTLD mtld = createMtld(2000, 45.2345, 45.2345);

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test an MTLD outside distance of some people in the cohort
	@Test
	public void testMtldWithinDistanceOfSomeOfCohort() {
		School school1 = createSchool(3001, 45.1234, 45.1234);
		School school2 = createSchool(3002, 45.1245, 45.1245);
		School school3 = createSchool(3003, 45.5345, 45.5345);
		CorpsMember cm1 = createCm(1001, school1);
		CorpsMember cm2 = createCm(1002, school1);
		CorpsMember cm3 = createCm(1003, school2);
		CorpsMember cm4 = createCm(1004, school3);

		MTLD mtld1 = createMtld(2001, school1.getLatitude(),
				school1.getLongitude());
		MTLD mtld2 = createMtld(2002, school3.getLatitude(),
				school3.getLongitude());

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();

		assertEquals(((1.0 + 1.0 + 1.0 + (25.0 / 34.7593)) / 4),
				criteria.scoreMtldToCohort(mtld1, cohort), 0.1);
		
		Iterator<CohortDetail> iterator = cohort.getCohortDetails().iterator();
		while(iterator.hasNext()){
			CohortDetail cohortDetail2 = iterator.next();
			
			if(cohortDetail2.getCorpMember().equals(cm4)){
				iterator.remove();
				break;
			}
			
		}
		
		
		assertEquals((((25 / 34.7593) + (25 / 34.7593) + (25 / 34.6662)) / 3),
				criteria.scoreMtldToCohort(mtld2, cohort), 0.1);
	}

	// Test blank inputs
	@Test
	public void testMtldToCohortWithBlanks() {
		assertEquals(0.0, criteria.scoreMtldToCohort(null, null), 0.0);
		MTLD mtld = createMtld(2000, 45.1234, 45.1234);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, null), 0.0);
		Cohort cohort = new Cohort();
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(createCm(1000,
				createSchool(3000, 45.1234, 45.1234)));
		cohort.addCohortDetail(cohortDetail);
		
		mtld = createMtld(2000, null, null);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	private School createSchool(int id, Double lat, Double lon) {
		School school = new School();
		school.setSchoolId(id);
		school.setLatitude(lat);
		school.setLongitude(lon);
		return school;
	}

	private CorpsMember createCm(int id, School school) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setSchool(school);
		return cm;
	}

	private MTLD createMtld(int id, Double latitude, Double longitude) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setLatitude(latitude);
		mtld.setLongitude(longitude);
		return mtld;
	}
}
