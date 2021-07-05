package org.tfa.mtld.service.bean;

import java.util.List;

public class CriteriaCategoryBean {
	
	private String categoryName;
	
	private Integer id;
	
	private List<CriteriaBean> criteriaBeans;
	

	public List<CriteriaBean> getCriteriaBeans() {
		return criteriaBeans;
	}

	public void setCriteriaBeans(List<CriteriaBean> criteriaBeans) {
		this.criteriaBeans = criteriaBeans;
	}




	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	

}
