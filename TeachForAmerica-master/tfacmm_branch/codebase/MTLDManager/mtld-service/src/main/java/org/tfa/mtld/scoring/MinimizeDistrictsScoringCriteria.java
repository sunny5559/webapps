package org.tfa.mtld.scoring;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * @author sguan
 * 
 *         If District (unassigned) = District in group : 1; Else, If District
 *         (unassigned) <> District in group : 0
 */
public class MinimizeDistrictsScoringCriteria implements
		ScoringCriteriaStrategy {
	private static final Logger log = Logger
			.getLogger(MinimizeDistrictsScoringCriteria.class);

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		//log.warn("this method is meaningless");
		return 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		// validation
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			if (log.isDebugEnabled())
				log.debug("validation not passed");
			return 0;
		}

		if (cm.getSchool() == null) {
			if (log.isDebugEnabled())
				log.debug("cm school is null");
			return 0;
		}

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember().equals(cm)) { // Don't compare a corps member with
										// themselves.
				continue;
			}
			if (cohortDetail.getCorpMember().getSchool() == null) { // skip
				continue;
			}
			if (cohortDetail.getCorpMember().getSchool().equals(cm.getSchool())) {
				if (log.isDebugEnabled())
					log.debug("same school found for " + cm.getId() + " "
							+ cm.getSchool() + " and " + cohortDetail.getCorpMember().getId() + " "
							+ cohortDetail.getCorpMember().getSchool());
				return 1; // same schools have same district
			}

			if (StringUtils.isEmpty(cm.getSchool().getDistrict())
					|| StringUtils.isEmpty(cohortDetail.getCorpMember().getSchool().getDistrict())) {
				continue;
			}
			if (StringUtils.equalsIgnoreCase(cm.getSchool().getDistrict(),
					cohortDetail.getCorpMember().getSchool().getDistrict())) {
				return 1;
			}
		}

		return 0;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		//log.warn("this method is meaningless");
		return 0;
	}

}
