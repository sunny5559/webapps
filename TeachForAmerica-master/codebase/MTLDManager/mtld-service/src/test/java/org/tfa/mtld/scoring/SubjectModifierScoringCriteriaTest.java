package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 2:18 PM To change
 * this template use File | Settings | File Templates.
 */
public class SubjectModifierScoringCriteriaTest {

	private SubjectModifierScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new SubjectModifierScoringCriteria();
	}

	// Test scoring a single corps member against a cohort. For each test, we
	// will look at both at scoring a CM who is not in the cohort yet as well as
	// scoring
	// for CMs who are already in the cohort.

	// Test against a cohort with a bunch of people, some of whom match.
	@Test
	public void testScoreCorpsMemberToCohortWithSomeMatches() {
		CorpsMember cm = createCm(1000, "SPEDINCL");
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		CorpsMember cm2 = createCm(1002, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "ESLPUSHIN");
		CorpsMember cm4 = createCm(1004, "BILINGOTHER");
		CorpsMember cm5 = createCm(1005, "BILINGSPAN");

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

	// Test against a smaller cohort with no SPED corps members
	@Test
	public void testScoreCorpsMemberToCohortWithNoMatches() {
		CorpsMember cm = createCm(1000, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "ESLPUSHIN");
		CorpsMember cm4 = createCm(1004, "BILINGOTHER");
		CorpsMember cm5 = createCm(1005, "BILINGSPAN");

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

	// Test against a cohort where everyone has the same subject modifier
	@Test
	public void testScoreCorpsMemberToCohortWithAllMatches() {
		CorpsMember cm = createCm(1000, "SPEDINCL");
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		CorpsMember cm2 = createCm(1002, "SPEDINCL");

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
		CorpsMember cm = createCm(1000, "SPEDINCL");
		// Test against empty or null cohort.
		Cohort emptyCohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, emptyCohort),
				0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
	}

	// Test against a cohort with one member.
	@Test
	public void testScoreCorpsMemberToCohortWithOneMember() {
		CorpsMember cm = createCm(1000, "SPEDINCL");
		CorpsMember cm1 = createCm(1001, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "BILINGOTHER");

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);
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
		corpsMember.setSubjectModifier("");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		corpsMember.setSubjectModifier("SPEDINCL");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectModifier("");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectModifier("BILINGOTHER");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
		mtld.setCorpsSubjectModifier("SPEDINCL");
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, corpsMember),
				0.0);
	}

	// Test scoring an MTLD against a cohort

	// Test against a cohort with a bunch of people, some of whom match.
	@Test
	public void testScoreMtldToCohortWithSomeMatches() {
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		CorpsMember cm2 = createCm(1002, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "ESLPUSHIN");
		CorpsMember cm4 = createCm(1004, "BILINGOTHER");
		CorpsMember cm5 = createCm(1005, "BILINGSPAN");
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
		MTLD mtld = createMtld(2000, "SPEDINCL");
		assertEquals(0.4, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		mtld.setCorpsSubjectModifier("ESLPUSHIN");
		assertEquals(0.2, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a smaller cohort with no SPED corps members
	@Test
	public void testScoreMtldToCohortWithNoMatches() {
		CorpsMember cm3 = createCm(1003, "ESLPUSHIN");
		CorpsMember cm4 = createCm(1004, "BILINGOTHER");
		CorpsMember cm5 = createCm(1005, "BILINGSPAN");
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
		MTLD mtld = createMtld(2000, "SPEDINCL");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against a cohort where everyone has the same subject modifier
	@Test
	public void testScoreMtldToCohortWithAllMatches() {
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		CorpsMember cm2 = createCm(1002, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "SPEDINCL");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		
		MTLD mtld = createMtld(2000, "SPEDSELFCON");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	// Test against an empty cohort
	@Test
	public void testScoreMtldToEmptyCohort() {
		Cohort emptyCohort = new Cohort();
		MTLD mtld = createMtld(2000, "SPEDINCL");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, emptyCohort), 0.0);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, null), 0.0);
	}

	// Test when the MTLD doesn't have a subject modifier
	@Test
	public void testScoreMtldWithNoSubjectModifier() {
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		CorpsMember cm2 = createCm(1002, "SPEDINCL");
		CorpsMember cm3 = createCm(1003, "ESLPUSHIN");
		CorpsMember cm4 = createCm(1004, "BILINGOTHER");
		CorpsMember cm5 = createCm(1005, "BILINGSPAN");
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
		CorpsMember cm1 = createCm(1001, "SPEDPULLOUT");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		
		MTLD mtld = createMtld(2000, "SPEDINCL");
		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
		mtld.setCorpsSubjectModifier("BILINGOTHER");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0.0);
	}

	@Test
	public void testGetSimplifiedModifier() {
		assertEquals("BILING",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("BILINGOTHER"));
		assertEquals("BILING",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("BILINGSPAN"));
		assertEquals("ESL",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("ESLPULLOUT"));
		assertEquals("ESL",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("ESLPUSHIN"));
		assertEquals("ESL",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("ESLSELFCON"));
		assertEquals("SPED",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("SPEDINCL"));
		assertEquals("SPED",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("SPEDPULLOUT"));
		assertEquals("SPED",
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("SPEDSELFCON"));
		assertEquals(null,
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier("FOO"));
		assertEquals(null,
				SubjectModifierScoringCriteria
						.getSimplifiedSubjectModifier(null));
	}

	private CorpsMember createCm(int id, String subjectModifier) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setSubjectModifier(subjectModifier);
		return cm;
	}

	private MTLD createMtld(int id, String subjectModifier) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setCorpsSubjectModifier(subjectModifier);
		return mtld;
	}

}
