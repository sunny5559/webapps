package org.tfa.mtld.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * School class mapped with school table.
 * 
 * @author vaibhav.poorey.
 * @version 1.0, 13 March, 2014.
 */

@Entity
@Table(name = "school")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="School")
public class School implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "school_id")
	private Integer schoolId;

	@Column(name = "name")
	private String schoolName;

	@Column(name = "address")
	private String address;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "province")
	private String province;

	@Column(name = "country")
	private String country;

	@Column(name = "neighborhood")
	private String neighborhood;

	@Column(name = "type")
	private String schoolType;

	@Column(name = "type_code")
	private String typeCode;

	@Column(name = "district")
	private String district;
	
	@Column(name = "SCHOOL_TFA_UID")
	private String schoolTfaUid;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;


	@Column(name = "TFASCHOOLID")
	private String tfaSchoolId;

	
	

	@OneToOne
	@JoinColumn(name = "principal_preferred_mtld_id")
	private MTLD principalPreferredMTLD;

	@Column(name = "cmo_affiliation")
	private String cmoAffiliation;

	@Column(name = "feeder_pattern_hs")
	private String feederPatternHS;
	
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

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	
	
	
	public String getSchoolTfaUid() {
		return schoolTfaUid;
	}

	public void setSchoolTfaUid(String schoolTfaUid) {
		this.schoolTfaUid = schoolTfaUid;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public MTLD getPrincipalPreferredMTLD() {
		return principalPreferredMTLD;
	}

	public void setPrincipalPreferredMTLD(MTLD principalPreferredMTLD) {
		this.principalPreferredMTLD = principalPreferredMTLD;
	}

	public String getCmoAffiliation() {
		return cmoAffiliation;
	}

	public void setCmoAffiliation(String cmoAffiliation) {
		this.cmoAffiliation = cmoAffiliation;
	}

	public String getFeederPatternHS() {
		return feederPatternHS;
	}

	public void setFeederPatternHS(String feederPatternHS) {
		this.feederPatternHS = feederPatternHS;
	}

	

	public String getTfaSchoolId() {
		return tfaSchoolId;
	}

	public void setTfaSchoolId(String tfaSchoolId) {
		this.tfaSchoolId = tfaSchoolId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		School school = (School) o;

		if (schoolId != null ? !schoolId.equals(school.schoolId)
				: school.schoolId != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return schoolId != null ? schoolId.hashCode() : 0;
	}
}
