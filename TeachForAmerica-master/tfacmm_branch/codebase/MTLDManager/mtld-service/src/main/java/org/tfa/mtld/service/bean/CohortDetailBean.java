package org.tfa.mtld.service.bean;


public class CohortDetailBean{
	private CMBean corpsMember;

	private Integer id;
	
    private boolean isNotFitForCohort;
	 
	private Character firstSeedingCorpsmember;
	private String criteriaScore;;

	 
	 
	 
	public String getCriteriaScore() {
		return criteriaScore;
	}

	public void setCriteriaScore(String criteriaScore) {
		this.criteriaScore = criteriaScore;
	}

	public Character getFirstSeedingCorpsmember() {
		return firstSeedingCorpsmember;
	}

	public void setFirstSeedingCorpsmember(Character firstSeedingCorpsmember) {
		this.firstSeedingCorpsmember = firstSeedingCorpsmember;
	}

	public boolean isNotFitForCohort() {
		return isNotFitForCohort;
	}

	public void setNotFitForCohort(boolean isNotFitForCohort) {
		this.isNotFitForCohort = isNotFitForCohort;
	}

	public CMBean getCorpsMember() {
		return corpsMember;
	}

	public void setCorpsMember(CMBean corpsMember) {
		this.corpsMember = corpsMember;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
