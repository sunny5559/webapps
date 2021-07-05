package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;

/**
 * @author sguan
 * 
 */
public class MinimizeDistrictsScoringCriteriaTest {
	private MinimizeDistrictsScoringCriteria criteria = new MinimizeDistrictsScoringCriteria();

	@Test
	public void testScoreMtldToCorpsMemberWithEmptyInput() {
		CorpsMember cm = null;
		MTLD mtld = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithSchoolDistrict(1, null);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		mtld = createMTLD(1000);
		cm = null;
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = new CorpsMember();
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		cm = createCMwithSchoolDistrict(1, "dis1");
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(null, cm), 0);
	}

	@Test
	public void testScoreMtldToCorpsMember() {
		MTLD mtld = createMTLD(0);
		CorpsMember cm = createCMwithSchoolDistrict(1, "dis1");
		assertEquals(0, criteria.scoreMtldToCorpsMember(mtld, cm), 0);
		MTLD mtld2 = createMTLD(234);
		assertEquals(0.0, criteria.scoreMtldToCorpsMember(mtld2, cm), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohortWithEmptyInput() {
		CorpsMember cm = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cm = createCMwithSchoolDistrict(1, null);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);

		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
	}

	@Test
	public void testScoreCorpsMemberToCohort() {
		CorpsMember cm = createCMwithSchoolDistrict(1, "dis1");
		CorpsMember cm1 = createCMwithSchoolDistrict(2, "dis1");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();

		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);

		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();

		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1, cohort), 0);

		CorpsMember cm2 = createCMwithSchoolDistrict(3, "dis2");
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort), 0);

		School school = new School();
		CorpsMember cm3 = new CorpsMember();
		cm3.setId(333);
		cm3.setSchool(school);
		CorpsMember cm4 = new CorpsMember();
		cm4.setId(444);
		cm4.setSchool(school);
		Cohort cohort2 = new Cohort();
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm4);
		cohort2.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm3, cohort2), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm4, cohort2), 0);

		school.setSchoolId(231);
		assertEquals(cm3.getSchool(), cm4.getSchool());
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm3, cohort2), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm4, cohort2), 0);

		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm2, cohort2), 0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm3, cohort), 0);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm3);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm4, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohortWithEmptyInput() {
		MTLD mtld = null;
		Cohort cohort = null;
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		mtld = createMTLD(0);
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		cohort = new Cohort();
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreMtldToCohort(mtld, cohort), 0);
	}

	@Test
	public void testScoreMtldToCohort() {
		MTLD mtld = createMTLD(0);
		CorpsMember cm = createCMwithSchoolDistrict(1, "dis1");
		CorpsMember cm1 = createCMwithSchoolDistrict(2, "dis1");
		Cohort cohort = new Cohort();
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm1);
		cohort.addCohortDetail(cohortDetail);

		assertEquals(0, criteria.scoreMtldToCohort(mtld, cohort), 0);

		CorpsMember cm2 = createCMwithSchoolDistrict(3, "dis3");
		cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort.addCohortDetail(cohortDetail);
		assertEquals(0, criteria.scoreMtldToCohort(mtld, cohort), 0.01);

		Cohort cohort2 = new Cohort();
		 cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm2);
		cohort2.addCohortDetail(cohortDetail);
		assertEquals(0, criteria.scoreMtldToCohort(mtld, cohort2), 0);
	}

	private CorpsMember createCMwithSchoolDistrict(int id, String district) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		School school = new School();
		school.setSchoolId(id + 2000);
		school.setDistrict(district);
		cm.setSchool(school);
		return cm;
	}

	private MTLD createMTLD(int id) {
		MTLD mtld = new MTLD();
		mtld.setId(id);
		return mtld;
	}
}
