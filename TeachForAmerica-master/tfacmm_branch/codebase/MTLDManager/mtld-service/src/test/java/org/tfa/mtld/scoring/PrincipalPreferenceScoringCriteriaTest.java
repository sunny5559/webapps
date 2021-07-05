package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

public class PrincipalPreferenceScoringCriteriaTest {
	private PrincipalPreferenceScoringCriteria criteria = new PrincipalPreferenceScoringCriteria();

	@Test
	public void testScoreMtldToCorpsMemberWithEmptyInput() {
		CorpsMember cm = null;
		MTLD mtld = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithSchoolPreferredMTLD(1, mtld);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		mtld = createMTLD(2000);
		cm = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = new CorpsMember();
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithSchoolPreferredMTLD(1, mtld);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(null, cm), 0);
	}

	@Test
	public void testScoreMtldToCorpsMember() {
		MTLD mtld = createMTLD(2000);
		CorpsMember cm = createCMwithSchoolPreferredMTLD(1, mtld);
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		MTLD mtld2 = createMTLD(2001);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld2, cm), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohortWithEmptyInput() {
		CorpsMember cm = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cm = createCMwithSchoolPreferredMTLD(1, null);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		CorpsMember cm1 = createCMwithSchoolPreferredMTLD(2, null);
	     cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);

		MTLD mtld = createMTLD(2000);
		cm.getSchool().setPrincipalPreferredMTLD(mtld);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohort() {
		MTLD mtld = createMTLD(2000);
		CorpsMember cm = createCMwithSchoolPreferredMTLD(1, mtld);
		CorpsMember cm1 = createCMwithSchoolPreferredMTLD(2, mtld);
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

		MTLD mtld2 = createMTLD(2001);
		CorpsMember cm2 = createCMwithSchoolPreferredMTLD(3, mtld2);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);
       cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);
		CorpsMember cm3 = createCMwithSchoolPreferredMTLD(4, mtld2);
		assertEquals(0.33, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0.1);
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

		CorpsMember cm = new CorpsMember();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		MTLD mtld2 = createMTLD(2001);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld2, cohort), 0);

		CorpsMember cm2 = createCMwithSchoolPreferredMTLD(2, mtld2);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreMtldToCohort(null, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohort() {
		MTLD mtld = createMTLD(2000);
		CorpsMember cm = createCMwithSchoolPreferredMTLD(1, mtld);
		CorpsMember cm1 = createCMwithSchoolPreferredMTLD(2, mtld);
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		 cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1, criteria.scoreMtldToCohort(mtld, cohort), 0);

		MTLD mtld2 = createMTLD(2001);
		CorpsMember cm2 = createCMwithSchoolPreferredMTLD(3, mtld2);
	    cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.67, criteria.scoreMtldToCohort(mtld, cohort), 0.01);

		Cohort cohort2 = new Cohort();
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		assertEquals(0, criteria.scoreMtldToCohort(mtld, cohort2), 0);
	}

	private CorpsMember createCMwithSchoolPreferredMTLD(int id, MTLD mtld) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		School school = new School();
		school.setPrincipalPreferredMTLD(mtld);
		cm.setSchool(school);
		return cm;
	}

	private MTLD createMTLD(int id) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		return mtld;
	}
}
