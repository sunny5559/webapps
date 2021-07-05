package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 3:39 PM To change
 * this template use File | Settings | File Templates.
 */
public class MTLDToCMRatioScoringCriteria implements ScoringCriteriaStrategy {
	private int maxCmCount;

	public MTLDToCMRatioScoringCriteria(int maxCmCount) {
		this.maxCmCount = maxCmCount;
	}

	// For this criteria, MTLD scoring is meaningless, so always return 0.
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		return 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0.0;
		}
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) {
				return 1.0;
			}
		}
		if (cohort.getCohortDetails().size() < maxCmCount) {
			return 1.0;
		}

		else {
			return 0.0;
		}
	}

	// For this criteria, MTLD scoring is meaningless, so always return 0.
	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		return 0;
	}
}
