package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.MTLDSchool;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/24/14 Time: 2:39 PM To change
 * this template use File | Settings | File Templates.
 */
public class MTLDPriorSchoolPlacementScoringCriteria implements
		ScoringCriteriaStrategy {
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		double score = 0.0;

		if (mtld == null || cm == null || cm.getSchool() == null
				|| mtld.getPriorSchoolsWorked() == null
				|| mtld.getPriorSchoolsWorked().isEmpty()) {
			return 0.0;
		}

		for (MTLDSchool mtldschool : mtld.getPriorSchoolsWorked()) {
			if (mtldschool.getSchool().getSchoolId()== cm.getSchool().getSchoolId()) {
				score = 1.0;
			}
			if (score == 1.0) {
				break;
			}
		}

		return score;

	}

	// For corps members, we want to simply minimize the number of schools in
	// the district.
	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)
				|| cm.getSchool() == null) {
			return 0.0;
		}

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cm.equals(cohortDetail.getCorpMember())) { // Don't compare a
															// corps member with
				// themselves.
				continue;
			}
			// added by Lovely Ram
			if (cohortDetail.getCorpMember().getSchool() == null) { // skip
				continue;
			}
			if (cm.getSchool().equals(cohortDetail.getCorpMember().getSchool())) {
				return 1.0;
			}
		}
		return 0.0;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)
				|| mtld.getPriorSchoolsWorked() == null
				|| mtld.getPriorSchoolsWorked().isEmpty()) {
			return 0.0;
		}
		double totalMatches = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().getSchool() != null) {

				for (MTLDSchool school : mtld.getPriorSchoolsWorked()) {
					if (school.getSchool().getSchoolId() == cohortDetail
							.getCorpMember().getSchool().getSchoolId()) {
						totalMatches++;
					}

				}

			}
		}
		return totalMatches / cohort.getCohortDetails().size();
	}
}
