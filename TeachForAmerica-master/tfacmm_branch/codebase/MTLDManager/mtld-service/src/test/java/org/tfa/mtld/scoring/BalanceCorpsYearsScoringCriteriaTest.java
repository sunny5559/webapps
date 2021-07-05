package org.tfa.mtld.scoring;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 4:18 PM To change
 * this template use File | Settings | File Templates.
 */
public class BalanceCorpsYearsScoringCriteriaTest {

	BalanceCorpsYearsScoringCriteria criteria;

	@Before
	public void setUp() {
		criteria = new BalanceCorpsYearsScoringCriteria();
	}

	// Empty cohort always returns zero.
	@Test
	public void testEmptyCohort() {
		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		Cohort cohort = new Cohort();
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(null, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, null), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		cohort.setCohortDetails(new ArrayList<CohortDetail>());
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test a cohort with more first year than second year
	@Test
	public void testCohortWithMoreFirstYears() {
	
	
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y1 = createCm(1003, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm4y1 = createCm(1004, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm5y2 = createCm(1005, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm6y2 = createCm(1006, TFAConstants.CORPS_YEAR2, cohort);


		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm5y2, cohort), 0.0);
	}

	// Test a cohort with more second year than first year
	@Test
	public void testCohortWithMoreSecondYears() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y2 = createCm(1003, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm4y2 = createCm(1004, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm5y2 = createCm(1005, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm6y2 = createCm(1006, TFAConstants.CORPS_YEAR2, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.5, criteria.scoreCorpsMemberToCohort(cm5y2, cohort), 0.0);
	}

	// Test a cohort with exact balance between first and second years.
	@Test
	public void testCohortWithEvenBalance() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y1 = createCm(1003, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm4y2 = createCm(1004, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm5y2 = createCm(1005, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm6y2 = createCm(1006, TFAConstants.CORPS_YEAR2, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm5y2, cohort), 0.0);
	}

	// Test a cohort that's very imbalanced
	@Test
	public void testImbalancedCohort() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y1 = createCm(1003, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm4y1 = createCm(1004, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm5y1 = createCm(1005, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm6y1 = createCm(1006, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm7y1 = createCm(1007, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm8y1 = createCm(1008, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm9y1 = createCm(1009, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm10y2 = createCm(1010, TFAConstants.CORPS_YEAR2, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(0.1111, criteria.scoreCorpsMemberToCohort(cm, cohort),
				0.01);
		assertEquals(0.1111, criteria.scoreCorpsMemberToCohort(cm1y1, cohort),
				0.01);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm10y2, cohort),
				0.0);
	}

	// Test a cohort with only one year
	@Test
	public void testCohortWithOnlyFirstYears() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y1 = createCm(1003, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm4y1 = createCm(1004, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm5y1 = createCm(1005, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test a cohort with only one member
	@Test
	public void testCohortWithOneCorpsMember() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.0, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
	}

	// Test a very large cohort that's nearly balanced.
	@Test
	public void testLargeCohort() {
		Cohort cohort = new Cohort();
		CorpsMember cm1y1 = createCm(1001, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm2y1 = createCm(1002, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm3y1 = createCm(1003, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm4y1 = createCm(1004, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm5y1 = createCm(1005, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm6y1 = createCm(1006, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm7y1 = createCm(1007, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm8y1 = createCm(1008, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm9y1 = createCm(1009, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm10y1 = createCm(1010, CriteriaScoringUtils.CURRENTCORPYEAR, cohort);
		CorpsMember cm11y2 = createCm(1011, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm12y2 = createCm(1012, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm13y2 = createCm(1013, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm14y2 = createCm(1014, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm15y2 = createCm(1015, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm16y2 = createCm(1016, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm17y2 = createCm(1017, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm18y2 = createCm(1018, TFAConstants.CORPS_YEAR2, cohort);
		CorpsMember cm19y2 = createCm(1019, TFAConstants.CORPS_YEAR2, cohort);

		CorpsMember cm = createCm(1000, CriteriaScoringUtils.CURRENTCORPYEAR);
		assertEquals(0.9, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(0.9, criteria.scoreCorpsMemberToCohort(cm1y1, cohort), 0.0);
		cm.setCorpsYear(TFAConstants.CORPS_YEAR2);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm, cohort), 0.0);
		assertEquals(1.0, criteria.scoreCorpsMemberToCohort(cm11y2, cohort),
				0.0);
	}

	private CorpsMember createCm(int id, Integer corpsYear) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setCorpsYear(corpsYear);
		return cm;
	}

	private CorpsMember createCm(int id, Integer corpsYear, Cohort cohort) {
		CorpsMember cm = new CorpsMember();
		cm.setId(id);
		cm.setCorpsYear(corpsYear);
		CohortDetail cohortDetail = new CohortDetail();
		cohortDetail.setCorpMember(cm);
		cohort.addCohortDetail(cohortDetail);
		return cm;
	}
}
