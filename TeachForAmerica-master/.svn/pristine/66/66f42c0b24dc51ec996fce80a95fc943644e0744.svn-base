package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

public class PreviousAdvisorScoringCriteria implements ScoringCriteriaStrategy {

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null || cm.getPreviousAdvisor() == null) {
			return 0;
		}
		return mtld.equals(cm.getPreviousAdvisor()) ? 1 : 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0;
		}
		if (cm.getPreviousAdvisor() == null)
			return 0;

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			if (cohortDetail.getCorpMember().getPreviousAdvisor() != null
					&& cm.getPreviousAdvisor().equals(
							cohortDetail.getCorpMember().getPreviousAdvisor())) {
				matchTotal++;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);

		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)) {
			return 0;
		}

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().getPreviousAdvisor() != null
					&& mtld.equals(cohortDetail.getCorpMember().getPreviousAdvisor())) {
				matchTotal++;
			}
		}

		return matchTotal / cohort.getCohortDetails().size();
	}

}
