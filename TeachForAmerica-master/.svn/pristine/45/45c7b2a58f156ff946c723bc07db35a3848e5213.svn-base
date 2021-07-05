package org.tfa.mtld.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Cohort class mapped with cohort table.
 * 
 * @author vaibhav.poorey.
 * @version 1.0, 12 March, 2014.
 */

@Entity
@Table(name = "cohort")
public class Cohort implements Serializable, Comparator<Cohort> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "school_per_cohort")
	private String schoolPerCohort;

	@OneToOne
	@JoinColumn(name = "mtld_id")
	private MTLD mtld;

	@OneToOne
	@JoinColumn(name = "cm_id")
	private CorpsMember corpsMember;

	/*
	 * @OneToMany(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "cm_id") private Set<CorpsMember> corpsMembers;
	 */

	@Column(name = "school_represented")
	private String schoolRep;

	@Column(name = "max_distance_between_school")
	private String maxDistanceSchool;

	@Column(name = "outside_constrained_distance")
	private String constrsinedDistance;

	@Column(name = "criteria_matched_percentage")
	private String criteriaMatchedPerc;

	@Column(name = "school_district_represented")
	private String schoolDistrictRepresented;

	@Column(name = "neighbourhood_represented")
	private String neighbourhoodRepresented;

	@Column(name = "sped_modifier_percentage")
	private String spedModifierPercentage;

	@Column(name = "ece_percentage")
	private String ecePercentage;

	@Column(name = "one_year_corp_percentage")
	private String oneYearCorpPercentage;

	@Column(name = "two_year_corp_percentage")
	private String twoYearCorpPercentage;

	@Column(name = "charter_partner_percentage")
	private String charterPartnerPercentage;

	@Column(name = "district_partner_percentage")
	private String districtPartnerPercentage;

	@Column(name = "elem_percentage")
	private String elemPercentage;

	@Column(name = "ms_grade_percentage")
	private String msGradePercentage;

	@Column(name = "hs_grade_percentage")
	private String hsGradePercentage;

	@Column(name = "person_color_percentage")
	private String personColorPercentage;

	@Column(name = "low_income_percentage")
	private String lowIncomePercentage;

	@Column(name = "IS_FINAL_COHORT")
	private Boolean isFinalCohort;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "cohort_id")
	private List<CohortDetail> cohortDetails;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "feeder_pattern_hs")
	private String feederPatternHS;

	@Column(name = "BASICS_CRITERIA_PER")
	private String basicsCriteriaPer;

	@Column(name = "CONTENT_CRITERIA_PER")
	private String contentCriteriaPer;
	@Column(name = "GEOGRAPHIC_CRITERIA_PER")
	private String geographicCriteriaPer;
	@Column(name = "RELATIONSHIPS_CRITERIA_PER")
	private String relationshipsCriteriaPer;

	transient private double cohortScore;

	@Column(name = "overDistance")
	private boolean overDistance;

	@Column(name = "max_distance")
	private String maxDistance;

	public String getBasicsCriteriaPer() {
		return basicsCriteriaPer;
	}

	public String getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(String maxDistance) {
		this.maxDistance = maxDistance;
	}

	public void setBasicsCriteriaPer(String basicsCriteriaPer) {
		this.basicsCriteriaPer = basicsCriteriaPer;
	}

	public String getContentCriteriaPer() {
		return contentCriteriaPer;
	}

	public void setContentCriteriaPer(String contentCriteriaPer) {
		this.contentCriteriaPer = contentCriteriaPer;
	}

	public String getGeographicCriteriaPer() {
		return geographicCriteriaPer;
	}

	public void setGeographicCriteriaPer(String geographicCriteriaPer) {
		this.geographicCriteriaPer = geographicCriteriaPer;
	}

	public String getRelationshipsCriteriaPer() {
		return relationshipsCriteriaPer;
	}

	public void setRelationshipsCriteriaPer(String relationshipsCriteriaPer) {
		this.relationshipsCriteriaPer = relationshipsCriteriaPer;
	}

	public boolean isOverDistance() {
		return overDistance;
	}

	public void setOverDistance(boolean overDistance) {
		this.overDistance = overDistance;
	}

	public Boolean getIsFinalCohort() {
		return isFinalCohort;
	}

	public void setIsFinalCohort(Boolean isFinalCohort) {
		this.isFinalCohort = isFinalCohort;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolPerCohort() {
		return schoolPerCohort;
	}

	public void setSchoolPerCohort(String schoolPerCohort) {
		this.schoolPerCohort = schoolPerCohort;
	}

	public MTLD getMtld() {
		return mtld;
	}

	public void setMtld(MTLD mtld) {
		this.mtld = mtld;
	}

	public CorpsMember getCorpsMember() {
		return corpsMember;
	}

	public void setCorpsMember(CorpsMember corpsMember) {
		this.corpsMember = corpsMember;
	}

	public void addCohortDetail(CohortDetail cd) {
		if (cohortDetails == null) {
			cohortDetails = new ArrayList<CohortDetail>();
		}
		cohortDetails.add(cd);
	}

	public List<CohortDetail> getCohortDetails() {
		return cohortDetails;
	}

	public void setCohortDetails(List<CohortDetail> cohortDetails) {
		this.cohortDetails = cohortDetails;
	}

	public String getSchoolRep() {
		return schoolRep;
	}

	public void setSchoolRep(String schoolRep) {
		this.schoolRep = schoolRep;
	}

	public String getMaxDistanceSchool() {
		return maxDistanceSchool;
	}

	public void setMaxDistanceSchool(String maxDistanceSchool) {
		this.maxDistanceSchool = maxDistanceSchool;
	}

	public String getConstrsinedDistance() {
		return constrsinedDistance;
	}

	public void setConstrsinedDistance(String constrsinedDistance) {
		this.constrsinedDistance = constrsinedDistance;
	}

	public String getCriteriaMatchedPerc() {
		return criteriaMatchedPerc;
	}

	public void setCriteriaMatchedPerc(String criteriaMatchedPerc) {
		this.criteriaMatchedPerc = criteriaMatchedPerc;
	}

	public String getSchoolDistrictRepresented() {
		return schoolDistrictRepresented;
	}

	public void setSchoolDistrictRepresented(String schoolDistrictRepresented) {
		this.schoolDistrictRepresented = schoolDistrictRepresented;
	}

	public String getNeighbourhoodRepresented() {
		return neighbourhoodRepresented;
	}

	public void setNeighbourhoodRepresented(String neighbourhoodRepresented) {
		this.neighbourhoodRepresented = neighbourhoodRepresented;
	}

	public String getSpedModifierPercentage() {
		return spedModifierPercentage;
	}

	public void setSpedModifierPercentage(String spedModifierPercentage) {
		this.spedModifierPercentage = spedModifierPercentage;
	}

	public String getEcePercentage() {
		return ecePercentage;
	}

	public void setEcePercentage(String ecePercentage) {
		this.ecePercentage = ecePercentage;
	}

	public String getOneYearCorpPercentage() {
		return oneYearCorpPercentage;
	}

	public void setOneYearCorpPercentage(String oneYearCorpPercentage) {
		this.oneYearCorpPercentage = oneYearCorpPercentage;
	}

	public String getTwoYearCorpPercentage() {
		return twoYearCorpPercentage;
	}

	public void setTwoYearCorpPercentage(String twoYearCorpPercentage) {
		this.twoYearCorpPercentage = twoYearCorpPercentage;
	}

	public String getCharterPartnerPercentage() {
		return charterPartnerPercentage;
	}

	public void setCharterPartnerPercentage(String charterPartnerPercentage) {
		this.charterPartnerPercentage = charterPartnerPercentage;
	}

	public String getDistrictPartnerPercentage() {
		return districtPartnerPercentage;
	}

	public void setDistrictPartnerPercentage(String districtPartnerPercentage) {
		this.districtPartnerPercentage = districtPartnerPercentage;
	}

	public String getElemPercentage() {
		return elemPercentage;
	}

	public void setElemPercentage(String elemPercentage) {
		this.elemPercentage = elemPercentage;
	}

	public String getMsGradePercentage() {
		return msGradePercentage;
	}

	public void setMsGradePercentage(String msGradePercentage) {
		this.msGradePercentage = msGradePercentage;
	}

	public String getHsGradePercentage() {
		return hsGradePercentage;
	}

	public void setHsGradePercentage(String hsGradePercentage) {
		this.hsGradePercentage = hsGradePercentage;
	}

	public String getPersonColorPercentage() {
		return personColorPercentage;
	}

	public void setPersonColorPercentage(String personColorPercentage) {
		this.personColorPercentage = personColorPercentage;
	}

	public String getLowIncomePercentage() {
		return lowIncomePercentage;
	}

	public void setLowIncomePercentage(String lowIncomePercentage) {
		this.lowIncomePercentage = lowIncomePercentage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public double getCohortScore() {
		return cohortScore;
	}

	public void setCohortScore(double cohortScore) {
		this.cohortScore = cohortScore;
	}

	public String getFeederPatternHS() {
		return feederPatternHS;
	}

	public void setFeederPatternHS(String feederPatternHS) {
		this.feederPatternHS = feederPatternHS;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Cohort cohort = (Cohort) o;

		if (id != null ? !id.equals(cohort.id) : cohort.id != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public int compare(Cohort o1, Cohort o2) {
		if (o1.getCohortScore() < o2.getCohortScore())
			return -1;
		if (o1.getCohortScore() > o2.getCohortScore())
			return 1;
		return 0;
	}

}
