package org.tfa.mtld.web.utils;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;


import org.tfa.mtld.scoring.MTLDToCMRatioScoringCriteria;
import org.tfa.mtld.scoring.MinimizeTravelDistanceScoringCriteria;
import org.tfa.mtld.scoring.ScoringCriteriaStrategy;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.constants.TFAConstants;

public class CommonUtility {

	public static Boolean isPriorityForAllTheCriteriaIsNotZero = Boolean.FALSE;
	public static final String SESSION_CRITERIA_MAP = "criteriaMap";
	public static final String SESSION_CRITERIABEAN_LIST = "criteriaBeanList";

	public static void createCriteriaMap(
			CriteriaBean criteriaBean, List<CriteriaBean> criteriaBeans,
			HttpSession session) throws Exception {
		isPriorityForAllTheCriteriaIsNotZero = Boolean.FALSE;

		@SuppressWarnings("unchecked")
		Map<String, List<CriteriaBean>> criteriaMap = (LinkedHashMap<String, List<CriteriaBean>>) session
				.getAttribute(CommonUtility.SESSION_CRITERIA_MAP);

		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		if (criteriaMap != null) {

			for (Map.Entry<String, List<CriteriaBean>> entry : criteriaMap
					.entrySet()) {

				if (entry.getKey().equalsIgnoreCase(
						TFAConstants.CRITERIA_CATEGORY_BASICS)) {

					List<CriteriaBean> foundationCriteriaList = createCriteriaList(
							TFAConstants.CRITERIA_CATEGORY_BASICS,
							criteriaBean.getBasicsValues(), entry.getValue());
					for (CriteriaBean criteriaBeanObj : foundationCriteriaList) {
						scoringCriteriaStrategy = null;
						if (criteriaBeanObj.getPriorityValue() != 0) {
							CommonUtility.isPriorityForAllTheCriteriaIsNotZero = Boolean.TRUE;
						}

						if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
								.equalsIgnoreCase(criteriaBeanObj.getClassAPI()
										.trim())) {
							if (criteriaBeanObj.getPriorityValue() != 0
									&& criteriaBeanObj.getFieldValue() != null
									&& !("0").equalsIgnoreCase(criteriaBeanObj.getFieldValue())) {
								scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
										Integer.parseInt(criteriaBeanObj
												.getFieldValue()));
							}

						} else if (criteriaBeanObj.getPriorityValue() != 0) {
							scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
									.forName(
											"org.tfa.mtld.scoring."
													+ criteriaBeanObj
															.getClassAPI()
															.trim())
									.newInstance();
						}
						criteriaBeanObj
								.setScoringCriteriaStrategy(scoringCriteriaStrategy);

						criteriaBeans.add(criteriaBeanObj);
					}

					criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_BASICS,
							foundationCriteriaList);
				}

				if (entry.getKey().equalsIgnoreCase(
						TFAConstants.CRITERIA_CATEGORY_CONTENT)) {

					List<CriteriaBean> contentCriteriaList = createCriteriaList(
							TFAConstants.CRITERIA_CATEGORY_CONTENT,
							criteriaBean.getContentValues(), entry.getValue());
					for (CriteriaBean criteriaBeanObj : contentCriteriaList) {
						scoringCriteriaStrategy = null;
						if (criteriaBeanObj.getPriorityValue() != 0) {
							CommonUtility.isPriorityForAllTheCriteriaIsNotZero = Boolean.TRUE;
						}

						if (criteriaBeanObj.getPriorityValue() != 0) {
							scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
									.forName(
											"org.tfa.mtld.scoring."
													+ criteriaBeanObj
															.getClassAPI()
															.trim())
									.newInstance();
						}

						criteriaBeanObj
								.setScoringCriteriaStrategy(scoringCriteriaStrategy);

						criteriaBeans.add(criteriaBeanObj);
					}
					criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_CONTENT,
							contentCriteriaList);

				}

				if (entry.getKey().equalsIgnoreCase(
						TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY)) {

					List<CriteriaBean> geographicCriteriaList = createCriteriaList(
							TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY,
							criteriaBean.getGeographyValues(), entry.getValue());
					for (CriteriaBean criteriaBeanObj : geographicCriteriaList) {
						scoringCriteriaStrategy = null;
						if (criteriaBeanObj.getPriorityValue() != 0) {
							CommonUtility.isPriorityForAllTheCriteriaIsNotZero = Boolean.TRUE;
						}

						if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
								.equalsIgnoreCase(criteriaBeanObj.getClassAPI()
										.trim())) {

							if (criteriaBeanObj.getPriorityValue() != 0
									&& criteriaBeanObj.getFieldValue() != null
									&& !("0").equalsIgnoreCase(criteriaBeanObj.getFieldValue())) {
								scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
										Double.parseDouble(criteriaBeanObj
												.getFieldValue()));
							}
						} else if (criteriaBeanObj.getPriorityValue() != 0) {
							scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
									.forName(
											"org.tfa.mtld.scoring."
													+ criteriaBeanObj
															.getClassAPI()
															.trim())
									.newInstance();
						}
						criteriaBeanObj
								.setScoringCriteriaStrategy(scoringCriteriaStrategy);

						criteriaBeans.add(criteriaBeanObj);
					}
					criteriaMap.put(TFAConstants.CRITERIA_CATEGORY_GEOGRAPHY,
							geographicCriteriaList);

				}

				if (entry.getKey().equalsIgnoreCase(
						TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS)) {

					List<CriteriaBean> relationshipsCriteriaList = createCriteriaList(
							TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS,
							criteriaBean.getRelationshipsValues(),
							entry.getValue());

					for (CriteriaBean criteriaBeanObj : relationshipsCriteriaList) {
						scoringCriteriaStrategy = null;
						if (criteriaBeanObj.getPriorityValue() != 0) {
							CommonUtility.isPriorityForAllTheCriteriaIsNotZero = Boolean.TRUE;
						}

						if (criteriaBeanObj.getPriorityValue() != 0) {
							scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
									.forName(
											"org.tfa.mtld.scoring."
													+ criteriaBeanObj
															.getClassAPI()
															.trim())
									.newInstance();
						}

						criteriaBeanObj
								.setScoringCriteriaStrategy(scoringCriteriaStrategy);

						criteriaBeans.add(criteriaBeanObj);
					}

					criteriaMap.put(
							TFAConstants.CRITERIA_CATEGORY_RELATIONSHIPS,
							relationshipsCriteriaList);

				}

			}
		}
		session.setAttribute(CommonUtility.SESSION_CRITERIA_MAP, criteriaMap);
		session.setAttribute(CommonUtility.SESSION_CRITERIABEAN_LIST, criteriaBeans); 
	}

	/**
	 * @param categoryName
	 * @param valueString
	 * @return ArrayList<CriteriaBean> Below method is used to iterate each
	 *         criteria value string and create list of Criteria Beans.
	 */
	public static List<CriteriaBean> createCriteriaList(String categoryName,
			String valueString, List<CriteriaBean> sessionBeanList)
			throws Exception {

	
		StringTokenizer categoryValueString = new StringTokenizer(valueString,
				",");

		int count = 0;
		while (categoryValueString.hasMoreElements()) {

			CriteriaBean bean = (CriteriaBean) sessionBeanList.get(count);

			String[] criteriaValueString = categoryValueString.nextToken()
					.split("#");

			bean.setId(Integer.parseInt(criteriaValueString[0]));
			bean.setPriorityValue(Integer.parseInt(criteriaValueString[1]));
			bean.setFieldValue(criteriaValueString[2]);

			count++;

		}
		return sessionBeanList;
	}

	
}
