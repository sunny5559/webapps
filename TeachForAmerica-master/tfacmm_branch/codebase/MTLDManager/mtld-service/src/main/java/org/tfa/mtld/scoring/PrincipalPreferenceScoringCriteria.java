package org.tfa.mtld.scoring;

import org.apache.log4j.Logger;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

public class PrincipalPreferenceScoringCriteria implements
		ScoringCriteriaStrategy {
	private static final Logger log = Logger
			.getLogger(PrincipalPreferenceScoringCriteria.class);

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null || cm.getSchool() == null
				|| cm.getSchool().getPrincipalPreferredMTLD() == null) {
			return 0;
		}
		return mtld.equals(cm.getSchool().getPrincipalPreferredMTLD()) ? 1 : 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0;
		}

		// if cm's school doesn't have preferred mtld, skip
		if (cm.getSchool() == null
				|| cm.getSchool().getPrincipalPreferredMTLD() == null) {
			return 0;
		}

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail: cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			if (cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getPrincipalPreferredMTLD() != null
					&& cohortDetail.getCorpMember().getSchool().getPrincipalPreferredMTLD()
							.equals(cm.getSchool().getPrincipalPreferredMTLD())) {
				matchTotal++;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);

		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)) {
			return 0;
		}

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getPrincipalPreferredMTLD() != null
					&& cohortDetail.getCorpMember().getSchool().getPrincipalPreferredMTLD()
							.equals(mtld)) {
				matchTotal++;
			}
		}

		return matchTotal / cohort.getCohortDetails().size();
	}

}
