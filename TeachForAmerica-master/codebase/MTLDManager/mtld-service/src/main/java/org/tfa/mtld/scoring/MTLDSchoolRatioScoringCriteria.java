package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by npeeters on 3/22/14.
 */
public class MTLDSchoolRatioScoringCriteria implements ScoringCriteriaStrategy {

	private double ratio;

	public MTLDSchoolRatioScoringCriteria() {
	}

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		// This is meaningless!!!
		return 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)
				|| cm.getSchool() == null) {
			return 0.0;
		}

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cm.equals(cohortDetail.getCorpMember())) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			if (cm.getSchool().equals(cohortDetail.getCorpMember().getSchool())) {
				return 1.0;
			}
		}
		return 0.0;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		// This is meaningless!!!
		return 0;
	}
}
