package org.tfa.mtld.scoring;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

/**
 * Created by npeeters on 3/22/14.
 */
public class SubjectGroupScoringCriteriaTest extends TestCase {

	private SubjectGroupScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new SubjectGroupScoringCriteria();
	}

	// Test scoring an MTLD against a single corps member.
	@Test
	public void testScoreMtldToCorpsMember() {
		MTLD mtld = createMtld(2000, null);
		CorpsMember corpsMember = new CorpsMember();
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(null, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, null), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		corpsMember.setSubjectGroup("");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		corpsMember.setSubjectGroup("ENGLISH");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectGroup("");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectGroup("MATH");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectGroup("ENGLISH");
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
	}

	// Test scoring a single corps member against a cohort. For each test, we
	// will look at both at scoring a CM who is not in the cohort yet as well as
	// scoring
	// for CMs who are already in the cohort.

	// Test against a cohort with a bunch of people, some of whom match.
	@Test
	public void testScoreCorpsMemberToCohortWithSomeMatches() {
		CorpsMember cm = createCm(1000, "ENGLISH");
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm2 = createCm(1002, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "MATH");
		CorpsMember cm4 = createCm(1004, "SCIENCE");
		CorpsMember cm5 = createCm(1005, "SCIENCE");

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
		cohortDetail.setCorpMember(cm5);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.4, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.25, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);
	}

	// Test against a smaller cohort with no ENGLISH corps members
	@Test
	public void testScoreCorpsMemberToCohortWithNoMatches() {
		CorpsMember cm = createCm(1000, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "MATH");
		CorpsMember cm4 = createCm(1004, "SCIENCE");
		CorpsMember cm5 = createCm(1005, "SCIENCE");

		Cohort cohort = new Cohort();
	
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm5);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm5, cohort), 0.0);
	}

	// Test against a cohort where everyone has the same grade level
	@Test
	public void testScoreCorpsMemberToCohortWithAllMatches() {
		CorpsMember cm = createCm(1000, "ENGLISH");
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm2 = createCm(1002, "ENGLISH");

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0.0);
	}

	// Test against an empty cohort
	@Test
	public void testScoreCorpsMemberToEmptyCohort() {
		CorpsMember cm = createCm(1000, "ENGLISH");
		// Test against empty or null cohort.
		Cohort emptyCohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
	}

	// Test against a cohort with one member.
	@Test
	public void testScoreCorpsMemberToCohortWithOneMember() {
		CorpsMember cm = createCm(1000, "ENGLISH");
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "MATH");

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);
	}

	// Test scoring an MTLD against a cohort

	// Test against a cohort with a bunch of people, some of whom match.
	@Test
	public void testScoreMtldToCohortWithSomeMatches() {
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm2 = createCm(1002, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "MATH");
		CorpsMember cm4 = createCm(1004, "SCIENCE");
		CorpsMember cm5 = createCm(1005, "SCIENCE");
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
		cohortDetail.setCorpMember(cm5);
		cohort.addCohortDetail(cohortDetail);
		MTLD mtld = createMtld(2000, "ENGLISH");
		assertEquals(0.4, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		mtld.setCorpsSubjectGroup("MATH");
		assertEquals(0.2, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a smaller cohort with no ENGLISH corps members
	@Test
	public void testScoreMtldToCohortWithNoMatches() {
		CorpsMember cm3 = createCm(1003, "MATH");
		CorpsMember cm4 = createCm(1004, "SCIENCE");
		CorpsMember cm5 = createCm(1005, "SCIENCE");
		Cohort cohort = new Cohort();
		
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm5);
		cohort.addCohortDetail(cohortDetail);
		MTLD mtld = createMtld(2000, "ENGLISH");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a cohort where everyone has the same grade level
	@Test
	public void testScoreMtldToCohortWithAllMatches() {
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm2 = createCm(1002, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "ENGLISH");
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
		
		MTLD mtld = createMtld(2000, "ENGLISH");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a smaller cohort with no ENGLISH corps members
	@Test
	public void testScoreMtldToEmptyCohort() {
		Cohort emptyCohort = new Cohort();
		MTLD mtld = createMtld(2000, "ENGLISH");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, emptyCohort), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, null), 0.0);
	}

	// Test when the MTLD doesn't have a grade level
	@Test
	public void testScoreMtldWithNoGradeLevel() {
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		CorpsMember cm2 = createCm(1002, "ENGLISH");
		CorpsMember cm3 = createCm(1003, "MATH");
		CorpsMember cm4 = createCm(1004, "SCIENCE");
		CorpsMember cm5 = createCm(1005, "SCIENCE");
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
		cohortDetail.setCorpMember(cm5);
		cohort.addCohortDetail(cohortDetail);
		MTLD mtld = createMtld(2000, null);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a cohort with one member.
	@Test
	public void testScoreMtldToCohortWithOneMember() {
		CorpsMember cm1 = createCm(1001, "ENGLISH");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		
		MTLD mtld = createMtld(2000, "ENGLISH");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		mtld.setCorpsSubjectGroup("SCIENCE");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	private CorpsMember createCm(int id, String subjectGroup) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setSubjectGroup(subjectGroup);
		return cm;
	}

	private MTLD createMtld(int id, String subjectGroup) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setCorpsSubjectGroup(subjectGroup);
		return mtld;
	}

}
