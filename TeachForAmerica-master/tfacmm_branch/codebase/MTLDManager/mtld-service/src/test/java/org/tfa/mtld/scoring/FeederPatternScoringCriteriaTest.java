package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.School;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 5:51 PM To change
 * this template use File | Settings | File Templates.
 */
public class FeederPatternScoringCriteriaTest {

	private FeederPatternScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new FeederPatternScoringCriteria();
	}

	// Test empty cohort, yada yada yada
	@Test
	public void testMatchCmToEmptyCohort() {
		CorpsMember cm = createCm(1000, "PS123");
		Cohort cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(null, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test against a cohort with a bunch of people, some of whom match.
	@Test
	public void testScoreCorpsMemberToCohortWithSomeMatches() {
		CohortDetail cohortDetail = null;
		CorpsMember cm = createCm(1000, "PS123");
		CorpsMember cm1 = createCm(1001, "PS123");
		CorpsMember cm2 = createCm(1002, "PS123");
		CorpsMember cm3 = createCm(1003, "PS456");
		CorpsMember cm4 = createCm(1004, "PS789");
		CorpsMember cm5 = createCm(1005, "PS789");

		Cohort cohort = new Cohort();
		
		cohortDetail = new CohortDetail();
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

	// Test against a smaller cohort with no matching corps members
	@Test
	public void testScoreCorpsMemberToCohortWithNoMatches() {
		CohortDetail cohortDetail = null;
		CorpsMember cm = createCm(1000, "PS123");
		CorpsMember cm3 = createCm(1003, "PS456");
		CorpsMember cm4 = createCm(1004, "PS789");
		CorpsMember cm5 = createCm(1005, "PS789");

		Cohort cohort = new Cohort();
		
		cohortDetail = new CohortDetail();
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

	// Test against a cohort where everyone has the same feeder HS
	@Test
	public void testScoreCorpsMemberToCohortWithAllMatches() {
		CohortDetail cohortDetail= null;
		CorpsMember cm = createCm(1000, "PS123");
		CorpsMember cm1 = createCm(1001, "PS123");
		CorpsMember cm2 = createCm(1002, "PS123");

		Cohort cohort = new Cohort();
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0.0);
	}

	// Test against a cohort with one member.
	@Test
	public void testScoreCorpsMemberToCohortWithOneMember() {
		CorpsMember cm = createCm(1000, "PS123");
		CorpsMember cm1 = createCm(1001, "PS123");
		CorpsMember cm3 = createCm(1003, "PS456");

		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		
		
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.0);
	}

	private CorpsMember createCm(int id, School school) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setSchool(school);
		return cm;
	}

	private CorpsMember createCm(int id, String feederPatternHS) {
		return createCm(id, createSchool(id + 3000, feederPatternHS));
	}

	private School createSchool(int id, String feederPatternHS) {
		School school = new School();
		school.setSchoolId(id);
		school.setFeederPatternHS(feederPatternHS);
		return school;
	}
}
