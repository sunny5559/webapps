package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/24/14 Time: 2:39 PM To change
 * this template use File | Settings | File Templates.
 */
public class MTLDPriorSchoolPlacementScoringCriteriaTest {

	private MTLDPriorSchoolPlacementScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new MTLDPriorSchoolPlacementScoringCriteria();
	}

	// Test scoring a single corps member against a cohort. For each test, we
	// will look at both at scoring a CM who is not in the cohort yet as well as
	// scoring
	// for CMs who are already in the cohort.

	// Test against an empty cohort
	@Test
	public void testScoreCorpsMemberToEmptyCohort() {
		CorpsMember cm = createCm(1000);
		// Test against empty or null cohort.
		Cohort emptyCohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
		emptyCohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
	}

	// Test against a cohort with a single school
	@Test
	public void testScoreCorpsMemberToCohortWithOneSchool() {

		School school1 = createSchool(101);
		School school2 = createSchool(102);

		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);

		cm.setSchool(school1);

		// Add to an empty cohort
		Cohort cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		// add to 1 person cohort with same school
		cm1.setSchool(school1);
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);

		// add to 2 person cohort with one school
		cm2.setSchool(school1);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0.0);

		// Test add with a different school
		cm.setSchool(school2);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test against cohort with multiple schools
	@Test
	public void testScoreCorpsMemberToCohortWithMultipleSchools() {

		School school1 = createSchool(101);
		School school2 = createSchool(102);
		School school3 = createSchool(103);

		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);
		CorpsMember cm3 = createCm(1003);
		CorpsMember cm4 = createCm(1004);

		cm.setSchool(school1);

		cm1.setSchool(school1);
		cm2.setSchool(school1);
		cm3.setSchool(school2);
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
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);

		cm4.setSchool(school2);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);

		cm.setSchool(school3);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test matching an MTLD to a single corps member
	@Test
	public void testMatchMTLDToSingleCorpsMember() {
		CorpsMember cm = createCm(1000);
		MTLD mtld = createMtld(2000);
		School school1 = createSchool(3001);
		School school2 = createSchool(3002);

		assertEquals(0.0, criteria.scoreMtldToCorpsMember(null, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(null, cm), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);

		cm.setSchool(school1);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);

		mtld.setPriorSchoolsWorked(new HashSet<School>());
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);

		cm.setSchool(null);
		mtld.getPriorSchoolsWorked().add(school1);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);

		cm.setSchool(school1);
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);

		cm.setSchool(school2);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0.0);
	}

	// Test matching an MTLD to a cohort

	@Test
	public void testMatchMTLDToEmptyCohort() {
		Cohort cohort = new Cohort();
		MTLD mtld = createMtld(2000);
		School school = createSchool(3000);

		assertEquals(0.0, criteria.scoreMtldToCohort(null, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCohort(null, cohort), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		mtld.setPriorSchoolsWorked(new HashSet<School>());
		mtld.getPriorSchoolsWorked().add(school);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	@Test
	public void testMatchMTLDToCohort() {
		Cohort cohort = new Cohort();
		School school1 = createSchool(3001);
		School school2 = createSchool(3002);
		School school3 = createSchool(3003);
		CorpsMember cm1 = createCm(1001);
		CorpsMember cm2 = createCm(1002);
		CorpsMember cm3 = createCm(1003);
		CorpsMember cm4 = createCm(1004);
		MTLD mtld = createMtld(2000);

		cm1.setSchool(school1);
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		cm2.setSchool(school1);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);

		cm3.setSchool(school2);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);

		mtld.setPriorSchoolsWorked(new HashSet<School>());
		mtld.getPriorSchoolsWorked().add(school3);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);

		mtld.getPriorSchoolsWorked().add(school1);
		assertEquals(0.67, criteria.scoreMtldToCohort(mtld, cohort), 0.01);
		cm4.setSchool(school3);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.75, criteria.scoreMtldToCohort(mtld, cohort), 0.0);

	}

	private CorpsMember createCm(int id) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		return cm;
	}

	private MTLD createMtld(int id) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		return mtld;
	}

	private School createSchool(int id) {
		School school = new School();
		school.setSchoolId(id);
		return school;
	}

}
