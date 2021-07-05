package org.tfa.mtld.service.bean;


public class SchoolBean extends Address{

	private Integer schoolId;
	private String schoolTfaUid;

	private String schoolName;

	private String province;

	private String country;

	private String neighborhood;

	private String schoolType;

	private String typeCode;

	private String district;
	private MTLDBean mtldBean;

	private Integer cmCount;
	private Integer mtldCount;
	
	
	
	
	public String getSchoolTfaUid() {
		return schoolTfaUid;
	}

	public void setSchoolTfaUid(String schoolTfaUid) {
		this.schoolTfaUid = schoolTfaUid;
	}

	public MTLDBean getMtldBean() {
		return mtldBean;
	}

	public void setMtldBean(MTLDBean mtldBean) {
		this.mtldBean = mtldBean;
	}

	
	public Integer getCmCount() {
		return cmCount;
	}

	public void setCmCount(Integer cmCount) {
		this.cmCount = cmCount;
	}

	public Integer getMtldCount() {
		return mtldCount;
	}

	public void setMtldCount(Integer mtldCOunt) {
		this.mtldCount = mtldCOunt;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

}
