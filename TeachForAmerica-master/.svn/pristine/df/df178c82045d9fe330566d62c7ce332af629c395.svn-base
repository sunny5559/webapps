package org.tfa.mtld.scoring;

import org.apache.commons.lang.StringUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/17/14 Time: 4:29 PM To change
 * this template use File | Settings | File Templates.
 */
public class GradeLevelScoringCriteria implements ScoringCriteriaStrategy {
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null
				|| StringUtils.isBlank(mtld.getGradeLevel())
				|| StringUtils.isBlank(cm.getGradeLevel())) {
			return 0.0;
		}
		return StringUtils.equals(mtld.getGradeLevel(), cm.getGradeLevel()) ? 1.0
				: 0.0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0.0;
		}
		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			if (StringUtils
					.equals(cohortDetail.getCorpMember().getGradeLevel(), cm.getGradeLevel())) {
				matchTotal++;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)
				|| StringUtils.isBlank(mtld.getGradeLevel())) {
			return 0.0;
		}
		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (StringUtils.equals(cohortDetail.getCorpMember().getGradeLevel(),
					mtld.getGradeLevel())) {
				matchTotal++;
			}
		}
		return matchTotal / cohort.getCohortDetails().size();
	}
}
