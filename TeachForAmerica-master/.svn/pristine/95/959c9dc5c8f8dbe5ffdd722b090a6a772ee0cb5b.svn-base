package org.tfa.mtld.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Criteria class mapped with criteria table.
 * 
 * @author vaibhav.poorey.
 * @version 1.0, 12 March, 2014.
 */

@Entity
@Table(name = "criteria")
// @Component
public class Criteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "criteria_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "field_required")
	private Boolean filedRequired;

	@Column(name = "field_type")
	private String fieldType;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private CriteriaCategory criteriaCategory;

	@Column(name = "field_validation")
	private String fieldValidation;

	@Column(name = "class_api")
	private String classAPI;

	@Column(name = "FIELD_PLACEHOLDER")
	private String fieldPlaceholder;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	public String getFieldPlaceholder() {
		return fieldPlaceholder;
	}

	public void setFieldPlaceholder(String fieldPlaceholder) {
		this.fieldPlaceholder = fieldPlaceholder;
	}

	public String getClassAPI() {
		return classAPI;
	}

	public void setClassAPI(String classAPI) {
		this.classAPI = classAPI;
	}

	public String getFieldValidation() {
		return fieldValidation;
	}

	public void setFieldValidation(String fieldValidation) {
		this.fieldValidation = fieldValidation;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getFiledRequired() {
		return filedRequired;
	}

	public void setFiledRequired(Boolean filedRequired) {
		this.filedRequired = filedRequired;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public CriteriaCategory getCriteriaCategory() {
		return criteriaCategory;
	}

	public void setCriteriaCategory(CriteriaCategory criteriaCategory) {
		this.criteriaCategory = criteriaCategory;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Criteria criteria = (Criteria) o;

		if (id != null ? !id.equals(criteria.id) : criteria.id != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
