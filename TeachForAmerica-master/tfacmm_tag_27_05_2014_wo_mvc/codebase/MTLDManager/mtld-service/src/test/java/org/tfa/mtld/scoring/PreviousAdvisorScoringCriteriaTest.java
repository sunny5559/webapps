package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

public class PreviousAdvisorScoringCriteriaTest {
	private PreviousAdvisorScoringCriteria criteria = new PreviousAdvisorScoringCriteria();

	@Test
	public void testScoreMtldToCorpsMemberWithEmptyInput() {
		CorpsMember cm = null;
		MTLD mtld = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithPreviousAdvisor(1, 2000);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		mtld = createMTLD(2000);
		cm = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = new CorpsMember();
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
	}

	@Test
	public void testScoreMtldToCorpsMember() {
		CorpsMember cm = createCMwithPreviousAdvisor(1, 2000);
		MTLD mtld = createMTLD(2000);
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm.setPreviousAdvisor(createMTLD(234));
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohortWithEmptyInput() {
		CorpsMember cm = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cm = createCMwithPreviousAdvisor(1, 2000);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohort() {
		CorpsMember cm = createCMwithPreviousAdvisor(1, 2000);
		CorpsMember cm1 = createCMwithPreviousAdvisor(2, 2000);
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);

	    cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);

		CorpsMember cm2 = createCMwithPreviousAdvisor(3, 2001);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);
	    cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohortWithEmptyInput() {
		MTLD mtld = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		mtld = createMTLD(2000);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohort() {
		MTLD mtld = createMTLD(2000);
		CorpsMember cm = createCMwithPreviousAdvisor(1, 2000);
		CorpsMember cm1 = createCMwithPreviousAdvisor(2, 2000);
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		CorpsMember cm2 = createCMwithPreviousAdvisor(3, 2001);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.67, criteria.scoreMtldToCohort(mtld, cohort), 0.01);

		Cohort cohort2 = new Cohort();
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort2), 0);
	}

	private CorpsMember createCMwithPreviousAdvisor(int id, int mtldId) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setPreviousAdvisor(createMTLD(mtldId));
		return cm;
	}

	private MTLD createMTLD(int id) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		return mtld;
	}
}
