package org.tfa.mtld.scoring;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 2:18 PM To change
 * this template use File | Settings | File Templates.
 */
public class SubjectModifierScoringCriteria implements ScoringCriteriaStrategy {
	private static Set<String> esl;
	private static Set<String> bilingual;
	private static Set<String> sped;

	static {
		esl = new HashSet<String>();
		esl.add("ESLPULLOUT");
		esl.add("ESLPUSHIN");
		esl.add("ESLSELFCON");
		bilingual = new HashSet<String>();
		bilingual.add("BILINGOTHER");
		bilingual.add("BILINGSPAN");
		sped = new HashSet<String>();
		sped.add("SPEDINCL");
		sped.add("SPEDPULLOUT");
		sped.add("SPEDSELFCON");
	}

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null
				|| StringUtils.isBlank(mtld.getCorpsSubjectModifier())
				|| StringUtils.isBlank(cm.getSubjectModifier())) {
			return 0.0;
		}
		return StringUtils.equals(
				getSimplifiedSubjectModifier(mtld.getCorpsSubjectModifier()),
				getSimplifiedSubjectModifier(cm.getSubjectModifier())) ? 1.0
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
					.equals(getSimplifiedSubjectModifier(cohortDetail.getCorpMember()
							.getSubjectModifier()),
							getSimplifiedSubjectModifier(cm
									.getSubjectModifier()))) {
				matchTotal++;
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return matchTotal / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortMTLDEmpty(cohort, mtld)
				|| StringUtils.isBlank(mtld.getCorpsSubjectModifier())) {
			return 0.0;
		}
		double matchTotal = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (StringUtils
					.equals(getSimplifiedSubjectModifier(cohortDetail.getCorpMember()
							.getSubjectModifier()),
							getSimplifiedSubjectModifier(mtld
									.getCorpsSubjectModifier()))) {
				matchTotal++;
			}
		}
		return matchTotal / cohort.getCohortDetails().size();
	}

	public static String getSimplifiedSubjectModifier(String subjectModifier) {
		if (StringUtils.isBlank(subjectModifier)) {
			return null;
		} else if (esl.contains(subjectModifier)) {
			return "ESL";
		} else if (bilingual.contains(subjectModifier)) {
			return "BILING";
		} else if (sped.contains(subjectModifier)) {
			return "SPED";
		} else {
			return null;
		}
	}
}
