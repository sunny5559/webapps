package org.tfa.mtld.service.bean;

import java.util.Map;

import org.apache.log4j.Logger;

public class CMBean implements Cloneable {

	private Integer id;

	private String tfaMasterUID;
	private String firstName;

	private String lastName;

	private String cmYear;

	private String subjectGroup;

	private String gradeLevel;

	private String subjectModifier;

	private Boolean isHired;
	
	private String hiredStatus;

	
	private SchoolBean schoolBean;
	private Boolean personOfColor;
	private Boolean islowIncomeBackground;
	private String cmoAffiliation;

	private String partnerTypeCode;

	private String feederPatternHS;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private Integer corpsYear;
	
	public Integer getCorpsYear() {
		return corpsYear;
	}

	public void setCorpsYear(Integer corpsYear) {
		this.corpsYear = corpsYear;
	}

	//Added by Divesh Solanki for export functionality
	//This field will just comes in and goes out
	private String ethnicity;
	
	
	public String getHiredStatus() {
		return hiredStatus;
	}

	public void setHiredStatus(String hiredStatus) {
		this.hiredStatus = hiredStatus;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}


	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFeederPatternHS() {
		return feederPatternHS;
	}

	public void setFeederPatternHS(String feederPatternHS) {
		this.feederPatternHS = feederPatternHS;
	}

	public String getPartnerTypeCode() {
		return partnerTypeCode;
	}

	public void setPartnerTypeCode(String partnerTypeCode) {
		this.partnerTypeCode = partnerTypeCode;
	}

	private Map<Integer, Double> criteriaScore;

	Logger logger = Logger.getLogger(CMBean.class);
	
	

	public Boolean getPersonOfColor() {
		return personOfColor;
	}

	public void setPersonOfColor(Boolean personOfColor) {
		this.personOfColor = personOfColor;
	}

	public Boolean getIslowIncomeBackground() {
		return islowIncomeBackground;
	}

	public void setIslowIncomeBackground(Boolean islowIncomeBackground) {
		this.islowIncomeBackground = islowIncomeBackground;
	}


	public String getCmoAffiliation() {
		return cmoAffiliation;
	}

	public void setCmoAffiliation(String cmoAffiliation) {
		this.cmoAffiliation = cmoAffiliation;
	}

	public String getTfaMasterUID() {
		return tfaMasterUID;
	}

	public void setTfaMasterUID(String tfaMasterUID) {
		this.tfaMasterUID = tfaMasterUID;
	}

	public Map<Integer, Double> getCriteriaScore() {
		return criteriaScore;
	}

	public void setCriteriaScore(Map<Integer, Double> criteriaScore) {
		this.criteriaScore = criteriaScore;
	}

	public String getSubjectModifier() {
		return subjectModifier;
	}

	public void setSubjectModifier(String subjectModifier) {
		this.subjectModifier = subjectModifier;
	}

	public SchoolBean getSchoolBean() {
		return schoolBean;
	}

	public Boolean getIsHired() {
		return isHired;
	}

	public void setIsHired(Boolean isHired) {
		this.isHired = isHired;
	}

	public void setSchoolBean(SchoolBean schoolBean) {
		this.schoolBean = schoolBean;
	}

	public String getCmYear() {
		return cmYear;
	}

	public void setCmYear(String cmYear) {
		this.cmYear = cmYear;
	}

	public String getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(String subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public String getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(String gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Override
	public CMBean clone() {
		try {
			return (CMBean) super.clone();
		} catch (final CloneNotSupportedException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
