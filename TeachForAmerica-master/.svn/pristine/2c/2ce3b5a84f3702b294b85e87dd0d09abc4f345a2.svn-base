package org.tfa.mtld.service.utils;

import org.apache.log4j.Logger;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

public class CriteriaScoringUtils {

	static Logger logger = Logger.getLogger(CriteriaScoringUtils.class);

	public static Integer CURRENTCORPYEAR = 2014;

	/**
	 * Validate cohort and cm input
	 * 
	 * @param cohort
	 * @param cm
	 * @return true if cohort or cm is empty or the only cm in cohort is current
	 *         cm
	 */
	public static boolean isCohortEmptyOtherThanCurrentCM(Cohort cohort,
			CorpsMember cm) {
		/*
		 * if (cm == null || cohort == null || cohort.getCohortDetails() == null
		 * || cohort.getCohortDetails().isEmpty() ||
		 * (cohort.getCohortDetails().size() == 1)) {
		 * 
		 * return true; }
		 */

		if (cm == null || cohort == null) {
			return true;
		}

		else if (cohort.getCohortDetails() == null) {
			return true;
		}

		else if (cohort.getCohortDetails().isEmpty()) {
			return true;
		}

		else if (cohort.getCohortDetails() != null
				&& cohort.getCohortDetails().size() == 1) {

			int i = 0;
			int cohortSize = cohort.getCohortDetails().size();
			while (i < cohortSize) {
				CohortDetail corpCohortDetail = cohort.getCohortDetails()
						.get(i);
				if (corpCohortDetail.getCorpMember().equals(cm)) {
					return true;
				}
				i++;
			}

		}

		return false;
	}

	/**
	 * Validate cohort and mtld input
	 * 
	 * @param cohort
	 * @param mtld
	 * @return true if cohort or mtld is empty
	 */
	public static boolean isCohortMTLDEmpty(Cohort cohort, MTLD mtld) {

		if (cohort == null || cohort.getCohortDetails() == null || mtld == null) {
			return true;
		}

		else if (cohort.getCohortDetails().isEmpty()) {
			return true;
		}
		return false;
	}

	public static int cohortSizeWithoutCm(Cohort cohort, CorpsMember cm) {

		if (cohort != null && cohort.getCohortDetails() != null
				&& cohort.getCohortDetails().size() >= 1) {
			int i = 0;
			int cohortSize = cohort.getCohortDetails().size();
			while (i < cohortSize) {
				CohortDetail corpCohortDetail = cohort.getCohortDetails()
						.get(i);
				if (cm.equals(corpCohortDetail.getCorpMember())) {
					return (cohort.getCohortDetails().size() - 1);
				}
				i++;
			}

		}
		return (null == cohort.getCohortDetails() ? 0 : cohort
				.getCohortDetails().size());
	}

}
