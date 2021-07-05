package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

public class CharterNetworkScoringCriteriaTest {
	private CharterNetworkScoringCriteria criteria = new CharterNetworkScoringCriteria();

	@Test
	public void testScoreMtldToCorpsMemberWithEmptyInput() {
		CorpsMember cm = null;
		MTLD mtld = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithCMO(1, "cmo1");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		mtld = createMTLD(0, "cmo1");
		cm = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = new CorpsMember();
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
	}

	@Test
	public void testScoreMtldToCorpsMember() {
		CorpsMember cm = createCMwithCMO(1, "cmo1");
		MTLD mtld = createMTLD(0, "cmo1");
		assertEquals(1.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm.getSchool().setCmoAffiliation("cmo2");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohortWithEmptyInput() {
		CohortDetail cohortDetail = null;
		CorpsMember cm = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cm = createCMwithCMO(1, "cmo1");
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		cohortDetail  = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		CorpsMember cm1 = createCMwithCMO(2, "cmo1");
		cohortDetail  = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		

		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(null, cohort), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohort() {
		CorpsMember cm = createCMwithCMO(1, "cmo1");
		CorpsMember cm1 = createCMwithCMO(2, "cmo1");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail =null;
		

		cohortDetail  = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohortDetail  = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		CorpsMember cm2 = createCMwithCMO(3, "cmo2");

		cohortDetail  = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohortWithEmptyInput() {
		MTLD mtld = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		mtld = createMTLD(0, "");
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		CorpsMember cm = createCMwithCMO(1, null);

		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohort() {
		MTLD mtld = createMTLD(0, "cmo1");
		CorpsMember cm = createCMwithCMO(1, "cmo1");
		CorpsMember cm1 = createCMwithCMO(2, "cmo1");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1, criteria.scoreMtldToCohort(mtld, cohort), 0);

		CorpsMember cm2 = createCMwithCMO(3, "cmo2");
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

	private CorpsMember createCMwithCMO(int id, String cmo) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		School school = new School();
		cm.setSchool(school);
		school.setCmoAffiliation(cmo);
		return cm;
	}

	private MTLD createMTLD(int id, String cmo) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		mtld.setCmoAffiliation(cmo);
		return mtld;
	}
}
