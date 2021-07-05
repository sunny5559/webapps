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
public class MatchNeighborhoodScoringCriteria implements
		ScoringCriteriaStrategy {

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null
				|| StringUtils.isBlank(mtld.getNeighborhood())
				|| StringUtils.isBlank(cm.getSchool().getNeighborhood())) {
			return 0;
		}
		return StringUtils.equals(mtld.getNeighborhood(), cm.getSchool()
				.getNeighborhood()) ? 1.0 : 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0;
		}

		if (cm.getSchool() != null && StringUtils.isBlank(cm.getSchool().getNeighborhood())) {
			int numCmsMatchingSchool = 0;
			for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
				if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare
																// a corps
																// member with
					// themselves.
					continue;
				}
				if (cohortDetail.getCorpMember().getSchool() == cm.getSchool()) {
					numCmsMatchingSchool++;
				}
			}
			return ((double) numCmsMatchingSchool)
					/ CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);

		} else {
			int numCmsMatchingNeigbourhood = 0;
			for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
				if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare
																// a corps
																// member with
					// themselves.
					continue;
				}
				//added by Lovely ram
				if (cohortDetail.getCorpMember().getSchool() == null) { //skip
					
					continue;
				}
				if (!StringUtils.isBlank(cohortDetail.getCorpMember()
						.getSchool().getNeighborhood())) {
					if (StringUtils.equals(cm.getSchool().getNeighborhood(),
							cohortDetail.getCorpMember().getSchool()
									.getNeighborhood())) {
						numCmsMatchingNeigbourhood++;
					}
				}
			}
			return ((double) numCmsMatchingNeigbourhood)
					/ CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		}
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)
				|| StringUtils.isBlank(mtld.getNeighborhood())) {
			return 0.0;
		}
		int numCmsMatchingNeigbourhood = 0;
		if (cohort != null) {
			for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
				// Added By lovely Ram
				if (cohortDetail.getCorpMember().getSchool() != null) {
					if (!StringUtils.isBlank(cohortDetail.getCorpMember()
							.getSchool().getNeighborhood())) {
						if (StringUtils.equals(mtld.getNeighborhood(),
								cohortDetail.getCorpMember().getSchool()
										.getNeighborhood())) {
							numCmsMatchingNeigbourhood++;
						}
					}

				}
			}
		}

		return ((double) numCmsMatchingNeigbourhood)
				/ cohort.getCohortDetails().size();
	}
}
