package org.tfa.mtld.data.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MTLD class mapped with mtld table.
 * 
 * @author vaibhav.poorey.
 * @version 1.0, 13 March, 2014.
 */

@Entity
@Table(name = "mtld")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="MTLD")
public class MTLD implements Serializable, Comparator<MTLD> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "mtld_id")
	private Integer id;

	@Column(name = "placement_advisor_tfa_uid")
	private String PlacementAdvisorTfaId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "tenure_role")
	private String tenureRole;

	@Column(name = "current_tenure")
	private Integer currentTenure;

	@Column(name = "alum")
	private Boolean alum;

	@ManyToOne
	@JoinColumn(name = "region_taught")
	private Region regionTaught;

	@Column(name = "subject_taught")
	private String subjectTaught;

	@Column(name = "speciality_area")
	private String specialityArea;

	@Column(name = "teaching_year_experince")
	private Integer TeachingExperince;

	@Column(name = "primary_subject")
	private String primarySubject;

	@Column(name = "demographic_info")
	private String demographicInfo;

	@Column(name = "primary_mode_trasport")
	private String primaryModeTransport;

	@Column(name = "cmo_affilation")
	private String cmoAffiliation; 


	@Column(name = "zip_code")
	private String zipCode;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@Column(name = "person_color")
	private Boolean personColor;

	@Column(name = "low_income_background")
	private Boolean lowIncomeBackground;

	@Column(name = "principal_prefrence")
	private String principalPrefrence;

	@ManyToOne
	@JoinColumn(name = "corps_school_id")
	private School corpsSchool;

	@ManyToOne
	@JoinColumn(name = "placement_school_id")
	private School mtldPlacementSchool;

	/*
	 * @OneToMany(fetch=FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "prior_school_id") private Set<School>
	 * priorSchoolsWorked;
	 */

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "mtld_id")
	private Set<MTLDSchool> priorSchoolsWorked;
	

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "corps_region_id")
	private Region corpsRegion;

	@Column(name = "grade_level")
	private String gradeLevel;

	@Column(name = "corps_years")
	private Integer corpsYears;

	@Column(name = "corps_pp_name")
	private String corpsPPName;

	@Column(name = "corps_subject_group")
	private String corpsSubjectGroup;

	@Column(name = "corps_subject_modifier")
	private String corpsSubjectModifier;

	@Column(name = "placement_district")
	private String placementDistrict;

	@Column(name = "neighborhood")
	private String neighborhood;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "ethnicity")
	private String ethnicity;

	//Added by Divesh Solanki for export/import
	@Column(name = "MD_TLD")
	private String mDTLD;
	transient double scoreMTLDToCohort;

	public double getScoreMTLDToCohort() {
		return scoreMTLDToCohort;
	}

	public void setScoreMTLDToCohort(double scoreMTLDToCohort) {
		this.scoreMTLDToCohort = scoreMTLDToCohort;
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

	public School getCorpsSchool() {
		return corpsSchool;
	}

	public void setCorpsSchool(School corpsSchool) {
		this.corpsSchool = corpsSchool;
	}

	public Integer getCorpsYears() {
		return corpsYears;
	}

	public void setCorpsYears(Integer corpsYears) {
		this.corpsYears = corpsYears;
	}

	public String getCorpsPPName() {
		return corpsPPName;
	}

	public void setCorpsPPName(String corpsPPName) {
		this.corpsPPName = corpsPPName;
	}

	public String getCorpsSubjectGroup() {
		return corpsSubjectGroup;
	}

	public void setCorpsSubjectGroup(String corpsSubjectGroup) {
		this.corpsSubjectGroup = corpsSubjectGroup;
	}

	public String getCorpsSubjectModifier() {
		return corpsSubjectModifier;
	}

	public void setCorpsSubjectModifier(String corpsSubjectModifier) {
		this.corpsSubjectModifier = corpsSubjectModifier;
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

	public String getTenureRole() {
		return tenureRole;
	}

	public void setTenureRole(String tenureRole) {
		this.tenureRole = tenureRole;
	}

	public Boolean getAlum() {
		return alum;
	}

	public void setAlum(Boolean alum) {
		this.alum = alum;
	}

	public String getSubjectTaught() {
		return subjectTaught;
	}

	public void setSubjectTaught(String subjectTaught) {
		this.subjectTaught = subjectTaught;
	}

	public String getSpecialityArea() {
		return specialityArea;
	}

	public void setSpecialityArea(String specialityArea) {
		this.specialityArea = specialityArea;
	}

	public Integer getTeachingExperince() {
		return TeachingExperince;
	}

	public void setTeachingExperince(Integer teachingExperince) {
		TeachingExperince = teachingExperince;
	}

	public String getDemographicInfo() {
		return demographicInfo;
	}

	public void setDemographicInfo(String demographicInfo) {
		this.demographicInfo = demographicInfo;
	}

	public String getPrimaryModeTransport() {
		return primaryModeTransport;
	}

	public void setPrimaryModeTransport(String primaryModeTransport) {
		this.primaryModeTransport = primaryModeTransport;
	}


	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Boolean getPersonColor() {
		return personColor;
	}

	public void setPersonColor(Boolean personColor) {
		this.personColor = personColor;
	}

	public Boolean getLowIncomeBackground() {
		return lowIncomeBackground;
	}

	public void setLowIncomeBackground(Boolean lowIncomeBackground) {
		this.lowIncomeBackground = lowIncomeBackground;
	}

	public String getPlacementAdvisorTfaId() {
		return PlacementAdvisorTfaId;
	}

	public void setPlacementAdvisorTfaId(String placementAdvisorTfaId) {
		PlacementAdvisorTfaId = placementAdvisorTfaId;
	}

	public String getPrincipalPrefrence() {
		return principalPrefrence;
	}

	public void setPrincipalPrefrence(String principalPrefrence) {
		this.principalPrefrence = principalPrefrence;
	}

	public String getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(String gradeLevel) {
		this.gradeLevel = gradeLevel;
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

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	public Region getCorpsRegion() {
		return corpsRegion;
	}

	public void setCorpsRegion(Region corpsRegion) {
		this.corpsRegion = corpsRegion;
	}

	public Region getRegionTaught() {
		return regionTaught;
	}

	public void setRegionTaught(Region regionTaught) {
		this.regionTaught = regionTaught;
	}

	public Integer getCurrentTenure() {
		return currentTenure;
	}

	public void setCurrentTenure(Integer currentTenure) {
		this.currentTenure = currentTenure;
	}

	public School getMtldPlacementSchool() {
		return mtldPlacementSchool;
	}

	public void setMtldPlacementSchool(School mtldPlacementSchool) {
		this.mtldPlacementSchool = mtldPlacementSchool;
	}



	public String getPlacementDistrict() {
		return placementDistrict;
	}

	public void setPlacementDistrict(String placementDistrict) {
		this.placementDistrict = placementDistrict;
	}

	public Set<MTLDSchool> getPriorSchoolsWorked() {
		return priorSchoolsWorked;
	}

	public void setPriorSchoolsWorked(Set<MTLDSchool> priorSchoolsWorked) {
		this.priorSchoolsWorked = priorSchoolsWorked;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getPrimarySubject() {
		return primarySubject;
	}

	public void setPrimarySubject(String primarySubject) {
		this.primarySubject = primarySubject;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}


	
	
	public String getCmoAffiliation() {
		return cmoAffiliation;
	}

	public void setCmoAffiliation(String cmoAffiliation) {
		this.cmoAffiliation = cmoAffiliation;
	}

	public String getmDTLD() {
		return mDTLD;
	}

	public void setmDTLD(String mDTLD) {
		this.mDTLD = mDTLD;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MTLD mtld = (MTLD) o;

		if (id != null ? !id.equals(mtld.id) : mtld.id != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public int compare(MTLD o1, MTLD o2) {
		if (o1.getScoreMTLDToCohort() < o2.getScoreMTLDToCohort())
			return -1;
		if (o1.getScoreMTLDToCohort() > o2.getScoreMTLDToCohort())
			return 1;
		return 0;
	}

}
