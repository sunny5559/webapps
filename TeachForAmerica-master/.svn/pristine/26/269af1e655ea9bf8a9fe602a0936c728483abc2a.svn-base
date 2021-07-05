package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 3:41 PM To change
 * this template use File | Settings | File Templates.
 */
public class MTLDToCMRatioScoringCriteriaTest {
	private MTLDToCMRatioScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new MTLDToCMRatioScoringCriteria(5);
	}

	// Test against an empty cohort - empty cohorts ALWAYS return zero for
	// everything.
	@Test
	public void testEmptyCohort() {
		Cohort cohort = new Cohort();
		CorpsMember cm = createCm(1000);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(null, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	@Test
	public void testCohortWithOneCm() {
		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		Cohort cohort = createCohort(cm1);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
	}

	@Test
	public void testAddingLastAcceptableCmToCohort() {
		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		Cohort cohort = createCohort(1001, 1002, 1003, 1004);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
	}

	@Test
	public void testCohortAtCapacity() {
		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		Cohort cohort = createCohort(1001, 1002, 1003, 1004, 1005);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
	}

	@Test
	public void testCohortOverCapacity() {
		CorpsMember cm = createCm(1000);
		CorpsMember cm1 = createCm(1001);
		Cohort cohort = createCohort(1001, 1002, 1003, 1004, 1005, 1006, 1007);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		// For this criteria, someone already in the cohort _always_ scores 1,
		// even if the cohort is over size.
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0.0);
	}

	private CorpsMember createCm(int id) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		return cm;
	}

	private Cohort createCohort(int... ids) {
		Cohort cohort = new Cohort();
		for (int id : ids) {
			CorpsMember cm = new CorpsMember();
			cm.setId(id);
			CohortDetail cohortDetail = new CohortDetail();
			cohortDetail.setCorpMember(cm);
			cohort.addCohortDetail(cohortDetail);
			
		}
		return cohort;
	}

	private Cohort createCohort(CorpsMember... cms) {
		Cohort cohort = new Cohort();
		for (CorpsMember cm : cms) {
			CohortDetail cohortDetail = new CohortDetail();
			cohortDetail.setCorpMember(cm);
			cohort.addCohortDetail(cohortDetail);
		}
		return cohort;
	}
}
