package org.tfa.mtld.service.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.service.constants.TFAConstants;

public class CriteriaScoringUtils {

	static Logger logger = Logger.getLogger(CriteriaScoringUtils.class);

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
				if (corpCohortDetail.getCorpMember().equals(cm)) {
					return (cohort.getCohortDetails().size() - 1);
				}
				i++;
			}

		}
		return cohort.getCohortDetails().size();
	}

	public static void calculateCohortDetails(Cohort cohort) throws Exception {
		if (cohort != null && cohort.getCohortDetails() != null) {
			if (!cohort.getCohortDetails().isEmpty()) {
				cohort.setSchoolDistrictRepresented(String
						.valueOf(calculateSchoolDistricts(cohort)));

				cohort.setSpedModifierPercentage(String
						.valueOf(calculateSPEDPercentage(cohort,
								TFAConstants.SUBJECT_MODIFIER)));
				cohort.setElemPercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_ELEM)));
				cohort.setMsGradePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_MS)));
				cohort.setHsGradePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_HIGH)));
				cohort.setEcePercentage(String
						.valueOf(calculateGradeLevelPercentage(cohort,
								TFAConstants.GRADE_LEVEL_PREK)));

				cohort.setOneYearCorpPercentage(String
						.valueOf(calculateYearPercentage(cohort,
								TFAConstants.CORPS_YEAR1,
								TFAConstants.FIRST_YEAR_CM)));
				cohort.setTwoYearCorpPercentage(String
						.valueOf(calculateYearPercentage(cohort,
								TFAConstants.CORPS_YEAR2,
								TFAConstants.SECOND_YEAR_CM)));

				cohort.setCharterPartnerPercentage(String
						.valueOf(calculatePlacementPartnerPercentage(cohort,
								TFAConstants.PLACEMENT_PARTNER_CHARTER)));
				cohort.setDistrictPartnerPercentage(String
						.valueOf(calculatePlacementPartnerPercentage(cohort,
								TFAConstants.PLACEMENT_PARTNER_DISTRICT)));

				cohort.setLowIncomePercentage(String
						.valueOf(calcualtelowIncomeBackgroundPercentage(cohort)));

				cohort.setNeighbourhoodRepresented(String
						.valueOf(calcualteNeighborhoodRepresented(cohort)));

				cohort.setFeederPatternHS(calcualteFeederPatternHS(cohort));
			} else {
				cohort.setSchoolDistrictRepresented("0");
				cohort.setSpedModifierPercentage("0.0");
				cohort.setElemPercentage("0.0");
				cohort.setMsGradePercentage("0.0");
				cohort.setHsGradePercentage("0.0");
				cohort.setEcePercentage("0.0");

				cohort.setOneYearCorpPercentage("0.0");
				cohort.setTwoYearCorpPercentage("0.0");

				cohort.setCharterPartnerPercentage("0.0");
				cohort.setDistrictPartnerPercentage("0.0");

				cohort.setLowIncomePercentage("0.0");

				cohort.setNeighbourhoodRepresented("");

				cohort.setFeederPatternHS("");
			}

		}
		calculateSchoolsRepresented(cohort);
	}

	/**
	 * This method calculates no of distinct school in the passed cohort.
	 * 
	 * @param cohort
	 *            Cohort for which schoolRepresented needs to be calculated.
	 * 
	 * @return cohort
	 */
	public static Cohort calculateSchoolsRepresented(Cohort cohort)
			throws Exception {
		logger.debug("Inside method calculateSchoolsRepresented");
		School school = null;
		Map<School, Integer> schoolMap = new HashMap<School, Integer>();

		int i = 0;
		int cohortDetailSize = cohort.getCohortDetails().size();

		while (i < cohortDetailSize) {
			if (cohort.getCohortDetails().get(i).getCorpMember().getSchool() != null) {
				school = cohort.getCohortDetails().get(i).getCorpMember()
						.getSchool();
				if (!schoolMap.containsKey(school)) {
					schoolMap.put(school, 1);
				} 
			}
			i++;
		}
		cohort.setSchoolRep(Integer.toString(schoolMap.size()));
		logger.debug("End of the method calculateSchoolsRepresented");
		return cohort;
	}

	private static int calculateSchoolDistricts(Cohort cohort) {

		Set<String> schoolDistricts = new HashSet<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			// Added By Lovely Ram
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getDistrict() != null
					&& cohortDetail.getCorpMember().getSchool().getDistrict()
							.length() > 0) {
				schoolDistricts.add(cohortDetail.getCorpMember().getSchool()
						.getDistrict());
			}
		}
		return schoolDistricts.size();

	}

	private static Double calculatePercentage(Double count, int totalCorps) {
		Double percentage = 0.0;
		if (count > 0.0) {

			percentage = Double.valueOf(new DecimalFormat("0.##")
					.format((count / totalCorps) * 100));
		}

		return percentage;
	}

	private static Double calculateSPEDPercentage(Cohort cohort,
			String modifierValue) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSubjectModifier() != null
					&& cohortDetail.getCorpMember().getSubjectModifier()
							.equalsIgnoreCase(modifierValue)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	private static Double calculateGradeLevelPercentage(Cohort cohort,
			String gradeValue) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getGradeLevel() != null
					&& cohortDetail.getCorpMember().getGradeLevel()
							.equalsIgnoreCase(gradeValue)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	/*
	 * Due to value changes in data base.For first year cm corps year should be
	 * equals or grater then 2013 else it's second year cm.
	 */

	private static Double calculateYearPercentage(Cohort cohort,
			Integer yearValue, String yearCalcualtion) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {

			if (yearCalcualtion.equalsIgnoreCase(TFAConstants.FIRST_YEAR_CM)
					&& cohortDetail.getCorpMember() != null
					&& null != cohortDetail.getCorpMember().getCorpsYear()
					&& cohortDetail.getCorpMember().getCorpsYear().intValue() >= yearValue
							.intValue()) {

				count++;
			}
			if (yearCalcualtion.equalsIgnoreCase(TFAConstants.SECOND_YEAR_CM)
					&& cohortDetail.getCorpMember() != null
					&& null != cohortDetail.getCorpMember().getCorpsYear()
					&& cohortDetail.getCorpMember().getCorpsYear().intValue() <= yearValue
							.intValue()) {
				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());

	}

	private static Double calculatePlacementPartnerPercentage(Cohort cohort,
			String partnerType) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool().getSchoolType() != null
					&& cohortDetail.getCorpMember().getSchool().getSchoolType()
							.equalsIgnoreCase(partnerType)) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());
	}

	private static Double calcualtelowIncomeBackgroundPercentage(Cohort cohort) {

		Double count = 0.0;

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getIslowIncomeBackground() != null
					&& cohortDetail.getCorpMember().getIslowIncomeBackground()) {

				count++;
			}
		}

		return calculatePercentage(count, cohort.getCohortDetails().size());
	}

	private static String calcualteNeighborhoodRepresented(Cohort cohort) {

		List<String> neighborhoods = new ArrayList<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {

			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getNeighborhood() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getNeighborhood().length() > 0) {
				neighborhoods.add(cohortDetail.getCorpMember().getSchool()
						.getNeighborhood());
			}

		}

		int max = 0;
		int curr = 0;
		String maxNeighborhood = null;
		Set<String> unique = new HashSet<String>(neighborhoods);

		for (String key : unique) {
			curr = Collections.frequency(neighborhoods, key);

			if (max < curr) {
				max = curr;
				maxNeighborhood = key;
			}
		}

		return maxNeighborhood;

	}

	private static String calcualteFeederPatternHS(Cohort cohort) {

		List<String> feederPatterns = new ArrayList<String>();

		for (CohortDetail cohortDetail : cohort.getCohortDetails()) {
			if (cohortDetail.getCorpMember() != null
					&& cohortDetail.getCorpMember().getSchool() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getFeederPatternHS() != null
					&& cohortDetail.getCorpMember().getSchool()
							.getFeederPatternHS().length() > 0) {
				feederPatterns.add(cohortDetail.getCorpMember().getSchool()
						.getFeederPatternHS());
			}

		}

		int max = 0;
		int curr = 0;
		String maxFeederPattern = null;
		Set<String> unique = new HashSet<String>(feederPatterns);

		for (String key : unique) {
			curr = Collections.frequency(feederPatterns, key);

			if (max < curr) {
				max = curr;
				maxFeederPattern = key;
			}
		}

		return maxFeederPattern;

	}

}
