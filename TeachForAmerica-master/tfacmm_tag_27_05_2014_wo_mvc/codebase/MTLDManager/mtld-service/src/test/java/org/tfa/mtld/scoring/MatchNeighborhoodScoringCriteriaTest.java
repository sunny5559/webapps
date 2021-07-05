package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

/**
 * Created by npeeters on 3/22/14.
 */
public class MatchNeighborhoodScoringCriteriaTest {

	private MatchNeighborhoodScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new MatchNeighborhoodScoringCriteria();
	}

	// Test against an empty cohort
	@Test
	public void testScoreCorpsMemberToEmptyCohort() {
		CorpsMember cm = createCm(1000);
		// Test against empty or null cohort.
		Cohort emptyCohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
		cm.setSchool(createSchool(2000, "BEDSTUY"));
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
		emptyCohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
	}

	// Score one CM against cohort with single CM
	@Test
	public void testScoreCorpsMemberToSingleCMCohort() {

		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);

		School school1 = createSchool(101, "BEDSTUY");
		School school2 = createSchool(102, "BEDSTUY");
		School school3 = createSchool(103, "GREENPOINT");
		School school4 = createSchool(104, null);
		School school5 = createSchool(105, null);

		Cohort cohort1 = new Cohort();
		Cohort cohort2 = new Cohort();

		cm1.setSchool(school1);
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort1.addCohortDetail(cohortDetail);

		// Test cm school & neighborhood matches cohort CM school & neighborhood
		cm.setSchool(school1);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);
		// Test cm school differs neighborhood matches cohort CM neighborhood
		cm.setSchool(school2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);
		// Test cm school & neighborhood differ cohort CM school & neighborhood
		cm.setSchool(school3);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);
		// Test with the CM already in the cohort
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort1), 0.0);

		// Test cm & cohort CM school matches with no neighborhood
		cm2.setSchool(school4);
	    cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		
		cm.setSchool(school4);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort2), 0.0);
		// Test cm & cohort CM school differs with no neighborhoods
		cm.setSchool(school5);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort2), 0.0);
		// Test cm & cohort CM school differs - cm has neighborhood
		cm.setSchool(school1);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort2), 0.0);
		// Test with the CM already in the cohort
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort2), 0.0);
	}

	// Score one CM against cohort with three CMs with Neighborhoods
	@Test
	public void testScoreCorpsMemberToThreeCMCohortWithNeighborhoods() {

		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);
		CorpsMember cm3 = createCm(1003);
		CorpsMember cm4 = createCm(1004);

		School school1 = createSchool(101, "BEDSTUY");
		School school2 = createSchool(102, "GREENPOINT");
		School school3 = createSchool(103, "GREENPOINT");
		School school4 = createSchool(104, "WILLIAMSBURG");
		School school5 = createSchool(105, "BEDSTUY");
		School school6 = createSchool(106, "GREENPOINT");
		School school7 = createSchool(107, "GREENPOINT");

		Cohort cohort1 = new Cohort();
		cm1.setSchool(school1);
		cm2.setSchool(school2);
		cm3.setSchool(school3);

		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort1.addCohortDetail(cohortDetail);
		
		// Test cm neighborhood is different to all in cohort
		cm.setSchool(school4);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);
		// Test cm school and neighborhood is same as one cm in cohort
		cm.setSchool(school1);
		assertEquals(0.33, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.1);
		// Test cm neighborhood is same as one cm in cohort (school is
		// different)
		cm.setSchool(school5);
		assertEquals(0.33, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.1);
		// Test cm neighborhood is same as two cms in cohort (school is same)
		cm.setSchool(school2);
		assertEquals(0.66, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.1);
		// Test cm neighborhood is same as two cms in cohort (school is
		// different)
		cm.setSchool(school6);
		assertEquals(0.66, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.1);
		// Test with cms already in the cohort
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort1), 0.0);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm2, cohort1), 0.0);

		Cohort cohort2 = new Cohort();
		cm4.setSchool(school6);
	    cohortDetail = new CohortDetail();
		
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort2.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort2.addCohortDetail(cohortDetail);
		
		// Test CM matches on neighborhood all different schools
		cm.setSchool(school7);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort2), 0.1);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm2, cohort2), 0.1);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm4, cohort2), 0.1);
	}

	// Score one CM against cohort with three CMs with No Neighborhoods
	@Test
	public void testScoreCorpsMemberToThreeCMCohortWithoutNeighborhoods() {

		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);
		CorpsMember cm3 = createCm(1003);

		School school1 = createSchool(101, null);
		School school2 = createSchool(102, null);

		Cohort cohort1 = new Cohort();
		cm1.setSchool(school1);
		cm2.setSchool(school1);
		cm3.setSchool(school1);

		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort1.addCohortDetail(cohortDetail);
		

		// Test cm neighborhood is different to all in cohort
		cm.setSchool(school1);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort1), 0.0);
		cm.setSchool(school2);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.0);

		cm3.setSchool(school2);
		cm.setSchool(school1);
		assertEquals(0.66, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.01);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm1, cohort1), 0.0);

		cm2.setSchool(school2);
		assertEquals(0.33, criteria.scoreCorpsMemberToCohort(cm, cohort1), 0.01);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort1), 0.0);
	}

	// Score one CM against cohort with single CM
	@Test
	public void testScoreMTLDToSingleCMCohort() {

		MTLD mtld = createMtld(1000, null);

		CorpsMember cm1 = createCm(1001);

		School school1 = createSchool(101, "BEDSTUY");
		School school2 = createSchool(102, null);

		Cohort cohort1 = new Cohort();

		cm1.setSchool(school1);
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort1.addCohortDetail(cohortDetail);
		

		// Test mtld no neighborhood doesnt matches cohort CM neighborhood
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort1), 0.0);

		// Test mtld neighborhood matches cohort CM neighborhood
		mtld.setNeighborhood("BEDSTUY");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort1), 0.0);
		// Test mtld neighborhood differ cohort CM neighborhood
		mtld.setNeighborhood("GREENPOINT");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort1), 0.0);

		// Test mtld neighborhood cohort school has no neighborhood
		cm1.setSchool(school2);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort1), 0.0);

	}

	// Test matching MTLD to CM
	@Test
	public void testScoreMtldToCorpsMember() {
		MTLD mtld = createMtld(10000, "BEDSTUY");
		MTLD mtldNullNeigborhood = createMtld(10000, null);
		CorpsMember cm1 = createCm(10001);
		School school1 = createSchool(1001, "BEDSTUY");
		School school2 = createSchool(1002, null);
		School school3 = createSchool(1003, "WILLIAMSBURG");

		assertEquals(0.0,
				criteria.scoreMtldToCorpsMember(mtldNullNeigborhood, cm1), 0.01);

		cm1.setSchool(school1);
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, cm1), 0.01);
		assertEquals(0.0,
				criteria.scoreMtldToCorpsMember(mtldNullNeigborhood, cm1), 0.01);
		cm1.setSchool(school2);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm1), 0.01);
		assertEquals(0.0,
				criteria.scoreMtldToCorpsMember(mtldNullNeigborhood, cm1), 0.01);
		cm1.setSchool(school3);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm1), 0.01);
		assertEquals(0.0,
				criteria.scoreMtldToCorpsMember(mtldNullNeigborhood, cm1), 0.01);
	}

	// Score MTLD against cohort with three CMs with Neighborhoods
	@Test
	public void testScoreMTLDToThreeCMCohortWithNeighborhoods() {

		MTLD mtld = createMtld(1000, null);

		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);
		CorpsMember cm3 = createCm(1003);
		CorpsMember cm4 = createCm(1004);

		School school1 = createSchool(101, "BEDSTUY");
		School school2 = createSchool(102, "GREENPOINT");
		School school3 = createSchool(103, "GREENPOINT");
		School school4 = createSchool(104, "WILLIAMSBURG");
		School school5 = createSchool(105, "BEDSTUY");
		School school6 = createSchool(106, "GREENPOINT");
		School school7 = createSchool(107, "GREENPOINT");

		// School school5 = createSchool(105, null);

		Cohort cohort1 = new Cohort();
		cm1.setSchool(school1);
		cm2.setSchool(school2);
		cm3.setSchool(school3);

		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort1.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort1.addCohortDetail(cohortDetail);
		
		// Test mtld neighborhood is different to all in cohort
		mtld.setNeighborhood("WILLIAMSBURG");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort1), 0.0);
		// Test mtld school and neighborhood is same as one cm in cohort
		mtld.setNeighborhood("BEDSTUY");
		assertEquals(0.33, criteria.scoreMtldToCohort(mtld, cohort1), 0.1);
		// Test mtld neighborhood is same as two cms in cohort (school is same)
		mtld.setNeighborhood("GREENPOINT");
		assertEquals(0.66, criteria.scoreMtldToCohort(mtld, cohort1), 0.1);

		Cohort cohort2 = new Cohort();
		cm4.setSchool(school6);
		 cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort2.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort2.addCohortDetail(cohortDetail);
		
		// Test CM matches on neighborhood all different schools
		mtld.setNeighborhood("GREENPOINT");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort2), 0.1);

	}

	private CorpsMember createCm(int id) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		return cm;
	}

	private School createSchool(int id, String neighborhood) {
		School school = new School();
		school.setSchoolId(id);
		school.setNeighborhood(neighborhood);
		return school;
	}

	private MTLD createMtld(int id, String neighborhood) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setNeighborhood(neighborhood);
		return mtld;
	}

}
