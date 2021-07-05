package org.tfa.mtld.scoring;

import org.apache.commons.lang.StringUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by npeeters on 3/22/14.
 */
public class SubjectGroupScoringCriteria implements ScoringCriteriaStrategy {
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null
				|| StringUtils.isBlank(mtld.getCorpsSubjectGroup())
				|| StringUtils.isBlank(cm.getSubjectGroup())) {
			return 0;
		}
		return StringUtils.equals(mtld.getCorpsSubjectGroup(),
				cm.getSubjectGroup()) ? 1.0 : 0.0;
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
			if (StringUtils.equals(cohortDetail.getCorpMember().getSubjectGroup(),
					cm.getSubjectGroup())) {
				matchTotal++;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)
				|| StringUtils.isBlank(mtld.getCorpsSubjectGroup())) {
			return 0.0;
		}
		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (StringUtils.equals(cohortDetail.getCorpMember().getSubjectGroup(),
					mtld.getCorpsSubjectGroup())) {
				matchTotal++;
			}
		}
		return matchTotal / cohort.getCohortDetails().size();

	}

}
