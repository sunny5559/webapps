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
 *         If MTLD School District = School District in group : (count of
 *         matching school district in group/count of CMs in group)
 */
public class MTLDDistrictScoringCriteria implements ScoringCriteriaStrategy {
	private static final Logger log = Logger
			.getLogger(MTLDDistrictScoringCriteria.class);

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null || mtld.getCorpsSchool() == null
				|| cm.getSchool() == null) {
			return 0;
		}
		if (mtld.getCorpsSchool().equals(cm.getSchool())) {
			return 1;
		}
		if (StringUtils.isEmpty(mtld.getCorpsSchool().getDistrict())
				|| StringUtils.isEmpty(cm.getSchool().getDistrict())) {
			return 0;
		}
		return mtld.getCorpsSchool().getDistrict()
				.equals(cm.getSchool().getDistrict()) ? 1 : 0;
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

		double matchTotal = 0.0;
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
				matchTotal++; // same schools have same district
				continue;
			}
			if (StringUtils.isEmpty(cm.getSchool().getDistrict())
					|| StringUtils.isEmpty(cohortDetail.getCorpMember().getSchool().getDistrict())) {
				continue;
			}
			if (StringUtils.equalsIgnoreCase(cm.getSchool().getDistrict(),
					cohortDetail.getCorpMember().getSchool().getDistrict())) {
				matchTotal++;
				continue;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);

		if (log.isDebugEnabled())
			log.debug("matchTotal: " + matchTotal);
		if (log.isDebugEnabled())
			log.debug("total: " + total);
		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)) {
			return 0;
		}
		if (mtld.getCorpsSchool() == null) {
			return 0;
		}

		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() == null)
				continue;
			if (cohortDetail.getCorpMember().getSchool() == null)
				continue;
			if (cohortDetail.getCorpMember().getSchool().equals(mtld.getCorpsSchool())) {
				matchTotal++;
				continue;
			}
			if (StringUtils.isEmpty(mtld.getCorpsSchool().getDistrict())
					|| StringUtils.isEmpty(cohortDetail.getCorpMember().getSchool().getDistrict())) {
				continue;
			}
			if (StringUtils.equalsIgnoreCase(mtld.getCorpsSchool()
					.getDistrict(), cohortDetail.getCorpMember().getSchool().getDistrict())) {
				matchTotal++;
				continue;
			}
		}

		if (log.isDebugEnabled())
			log.debug("matchTotal: " + matchTotal);
		if (log.isDebugEnabled())
			log.debug("cm size: " + cohort.getCohortDetails().size());
		return matchTotal / cohort.getCohortDetails().size();
	}

}
