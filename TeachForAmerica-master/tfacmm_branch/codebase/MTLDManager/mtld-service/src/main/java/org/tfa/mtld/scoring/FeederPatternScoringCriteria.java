package org.tfa.mtld.scoring;

import org.apache.commons.lang.StringUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 5:50 PM To change
 * this template use File | Settings | File Templates.
 */
public class FeederPatternScoringCriteria implements ScoringCriteriaStrategy {

	// This criteria has no meaning for MTLD matching, so it will always return
	// zero.
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		return 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0.0;
		}
		double matchTotal = 0.0;
		if(cohort != null){
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			// added By Lovely Ram
			if (cohortDetail != null && cohortDetail.getCorpMember().getSchool() != null) {
				if (cm.getSchool() != null && StringUtils.equals(cohortDetail.getCorpMember().getSchool()
						.getFeederPatternHS(), cm.getSchool()
						.getFeederPatternHS())) {
					matchTotal++;
				}
			}
		}
	}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return matchTotal / total;
	}

	// This criteria has no meaning for MTLD matching, so it will always return
	// zero.
	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		return 0;
	}
}
