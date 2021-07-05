package org.tfa.mtld.scoring;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;
import org.tfa.mtld.service.utils.DistanceUtil;

/**
 * Created by IntelliJ IDEA. User: jrobins Date: 3/17/14 Time: 4:30 PM To change
 * this template use File | Settings | File Templates.
 */
public class MinimizeTravelDistanceScoringCriteria implements
		ScoringCriteriaStrategy {

	private double maxTravelDistance;

	public MinimizeTravelDistanceScoringCriteria(double maxTravelDistance) {
		this.maxTravelDistance = maxTravelDistance;
	}

	public double scoreMtldToCorpsMember(MTLD mtld, CorpsMember cm) {
		if (mtld == null || cm == null || cm.getSchool() == null
				|| mtld.getLatitude() == null || mtld.getLongitude() == null
				|| cm.getSchool().getLatitude() == null
				|| cm.getSchool().getLongitude() == null) {
			return 0.0;
		}
		double distance = DistanceUtil.calculateDistance(mtld.getLatitude(),
				mtld.getLongitude(), cm.getSchool().getLatitude(), cm
						.getSchool().getLongitude());
		if (distance < maxTravelDistance) {
			return 1.0;
		} else {
			return maxTravelDistance / distance;
		}
	}

	public double scoreCorpsMemberToCohort(CorpsMember cm, Cohort cohort) {
		if (cm == null || cohort == null || cohort.getCohortDetails() == null
				|| cohort.getCohortDetails().isEmpty()) {
			return 0.0;
		}

		double totalDistanceScore = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cm.equals(cohortDetail.getCorpMember())) { // Don't score a corps member against
										// themselves
				continue;
			}
			// added by Lovely Ram
			if (cohortDetail.getCorpMember().getSchool() == null) { // skip
				continue;
			}
			if(cm.getSchool() == null){
				break;
			}
			
			double distance = 0.0;
			if(cohortDetail.getCorpMember()
					.getSchool().getLatitude() != null && cohortDetail.getCorpMember()
							.getSchool().getLongitude() != null && cm.getSchool().getLatitude() != null && cm.getSchool().getLongitude() != null){
				distance = DistanceUtil.calculateDistance(cohortDetail.getCorpMember().getSchool()
						.getLatitude(), cohortDetail.getCorpMember().getSchool().getLongitude(), cm
						.getSchool().getLatitude(), cm.getSchool()
						.getLongitude());
			}
		
			
			if (distance < maxTravelDistance) {
				totalDistanceScore++;
			} else {
				if(distance > 0){
				totalDistanceScore += (maxTravelDistance / distance);
				}
			}
		}
		int total = CriteriaScoringUtils.cohortSizeWithoutCm(cohort, cm);
		return totalDistanceScore / total;
	}

	public double scoreMtldToCohort(MTLD mtld, Cohort cohort) {
		if (mtld == null || cohort == null || cohort.getCohortDetails() == null
				|| cohort.getCohortDetails().size() == 0
				|| mtld.getLatitude() == null || mtld.getLongitude() == null) {
			return 0.0;
		}

		double totalDistanceScore = 0.0;
		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			
			// added by Lovely Ram
			if (cohortDetail.getCorpMember().getSchool() == null) { // skip
				continue;
			}
			
				double distance = 0;
				
				if(cohortDetail.getCorpMember()
						.getSchool().getLatitude() != null && cohortDetail.getCorpMember()
								.getSchool().getLongitude() != null && mtld.getLatitude() != null && mtld.getLongitude() != null){
				distance = DistanceUtil.calculateDistance(cohortDetail.getCorpMember()
						.getSchool().getLatitude(), cohortDetail.getCorpMember().getSchool()
						.getLongitude(), mtld.getLatitude(), mtld
						.getLongitude());
				}
		
				
				
				
				if (distance < maxTravelDistance) {
					totalDistanceScore++;
				} else {
					if(distance > 0){
					totalDistanceScore += (maxTravelDistance / distance);
					}
				}
				
		
			

		}
		int total = cohort.getCohortDetails().size();
		return totalDistanceScore / total;
	}
}
