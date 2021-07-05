package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/22/14 Time: 4:17 PM To change
 * this template use File | Settings | File Templates.
 */
public class BalanceCorpsYearsScoringCriteria implements
		ScoringCriteriaStrategy {

	// This criteria has no meaning for MTLD matching, only for corps members
	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		return 0;
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (CriteriaScoringUtils.isCohortEmptyOtherThanCurrentCM(cohort, cm)) {
			return 0.0;
		}
		double firstYearCount = 0;
		double secondYearCount = 0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			
			if (cohortDetail != null && cohortDetail.getCorpMember() != null && cohortDetail.getCorpMember().getCorpsYear() != null && CriteriaScoringUtils.CURRENTCORPYEAR.intValue() == cohortDetail.getCorpMember().getCorpsYear() 
					) {
				firstYearCount++;
			} else if (cohortDetail != null && cohortDetail.getCorpMember() != null && cohortDetail.getCorpMember().getCorpsYear() != null && CriteriaScoringUtils.CURRENTCORPYEAR.intValue() > cohortDetail.getCorpMember().getCorpsYear() 
					) {
				secondYearCount++;
			}
		
	}
		if (cm.getCorpsYear() != null && CriteriaScoringUtils.CURRENTCORPYEAR.intValue()  <= cm.getCorpsYear() ) {
			if (firstYearCount < secondYearCount) {
				return 1.0;
			}
			else if(firstYearCount == 0){
				return 0.0;
			}
			
			else {
				return secondYearCount / firstYearCount;
			}
		} else {
			if (secondYearCount < firstYearCount) {
				return 1.0;
			}
			else if(secondYearCount == 0){
				return 0.0;
			}
			else {
				return firstYearCount / secondYearCount;
			}
		}
	}

	// This criteria has no meaning for MTLD matching, only for corps members
	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		return 0;
	}
}
