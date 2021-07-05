package org.tfa.mtld.service.bean;

import java.util.List;


public class CriteriaFormBean {

	
	private List<CriteriaCategoryBean> criteriaCategoryBeanList;
	
	private Boolean includeMTLD = Boolean.FALSE;

	private Boolean includeUnhiredCM = Boolean.FALSE;

	private int cohortCount;
	
	 private Boolean isPriorityForAllTheCriteriaIsNotZero=Boolean.FALSE;

	public int getCohortCount() {
		return cohortCount;
	}

	public void setCohortCount(int cohortCount) {
		this.cohortCount = cohortCount;
	}

	public List<CriteriaCategoryBean> getCriteriaCategoryBeanList() {
		return criteriaCategoryBeanList;
	}

	public void setCriteriaCategoryBeanList(List<CriteriaCategoryBean> criteriaCategoryBeanList) {
		this.criteriaCategoryBeanList = criteriaCategoryBeanList;
	}

	public Boolean getIncludeMTLD() {
		return includeMTLD;
	}

	public void setIncludeMTLD(Boolean includeMTLD) {
		this.includeMTLD = includeMTLD;
	}

	public Boolean getIncludeUnhiredCM() {
		return includeUnhiredCM;
	}

	public void setIncludeUnhiredCM(Boolean includeUnhiredCM) {
		this.includeUnhiredCM = includeUnhiredCM;
	}

	public Boolean getIsPriorityForAllTheCriteriaIsNotZero() {
		return isPriorityForAllTheCriteriaIsNotZero;
	}

	public void setIsPriorityForAllTheCriteriaIsNotZero(
			Boolean isPriorityForAllTheCriteriaIsNotZero) {
		this.isPriorityForAllTheCriteriaIsNotZero = isPriorityForAllTheCriteriaIsNotZero;
	}
	
	
	
	
	
}
