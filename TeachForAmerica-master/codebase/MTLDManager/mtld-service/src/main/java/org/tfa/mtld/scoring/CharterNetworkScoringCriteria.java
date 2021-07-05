package org.tfa.mtld.scoring;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * @author sguan
 *         <p/>
 *         If Charter Network (CM) = Charter Network (CM) in group : count of
 *         shared charter network in group/count of CMs in group
 */
public class CharterNetworkScoringCriteria implements ScoringCriteriaStrategy {
	private static final Logger log = Logger
			.getLogger(CharterNetworkScoringCriteria.class);

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null || mtld.getCmoAffiliation() == null
				|| cm.getSchool() == null
				|| cm.getSchool().getCmoAffiliation() == null) {
			return 0;
		} else {
			return StringUtils.equalsIgnoreCase(mtld.getCmoAffiliation(), cm
					.getSchool().getCmoAffiliation()) ? 1.0 : 0.0;
		}
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0;
		}
		// field validation
		if (cm.getSchool() == null || (cm.getSchool() != null && StringUtils.isEmpty(cm.getSchool().getCmoAffiliation()))) {
			return 0;
		}

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a
															// corps member with
				// themselves.
				continue;
			}
			if (cohortDetail.getCorpMember().getSchool() != null) {
				if (StringUtils.equalsIgnoreCase(cohortDetail.getCorpMember()
						.getSchool().getCmoAffiliation(), cm.getSchool()
						.getCmoAffiliation())) {
					matchTotal++;
				}
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)) {
			return 0;
		}
		// field validation
		if (StringUtils.isEmpty(mtld.getCmoAffiliation())) {
			return 0;
		}

		double matchTotal = 0.0;
		if (cohort != null) {
			for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
				// added by lovely Ram
				if (cohortDetail != null && cohortDetail.getCorpMember().getSchool() != null) {
					if (StringUtils.equals(cohortDetail.getCorpMember()
							.getSchool().getCmoAffiliation(),
							mtld.getCmoAffiliation())) {
						matchTotal++;
					}
				}
			}
		}

		if (log.isDebugEnabled())
			log.debug("matchTotal: " + matchTotal);
		if (log.isDebugEnabled())
			log.debug("cm size: " + cohort.getCohortDetails().size());
		return matchTotal / cohort.getCohortDetails().size();
	}
}
