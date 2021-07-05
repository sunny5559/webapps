package org.tfa.mtld.data.model;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Cohort class mapped with CohortDetail table.
 * 
 * @author Lovely Ram.
 * @version 1.0, 12 March, 2014.
 */
@Entity
@Table(name = "cohort_detail")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="CohortDetail")
public class CohortDetail implements Serializable, Comparator<CohortDetail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cohort_detail_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cohort_id")
	private Cohort cohort;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cm_id")
	private CorpsMember corpMember;

	@Column(name = "criteria_score")
	private String criteriaScore;
	
	@Column(name = "is_NotFitForCohort")
	private Boolean isNotFitForCohort=Boolean.FALSE;
	
	@Column(name = "first_seeding_corpsmember")
	private Character firstSeedingCorpsmember;

	
	
	
	
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CorpsMember getCorpMember() {
		return corpMember;
	}

	public void setCorpMember(CorpsMember corpMember) {
		this.corpMember = corpMember;
	}

	public Cohort getCohort() {
		return cohort;
	}

	public void setCohort(Cohort cohort) {
		this.cohort = cohort;
	}

	public String getCriteriaScore() {
		return criteriaScore;
	}

	public void setCriteriaScore(String criteriaScore) {
		this.criteriaScore = criteriaScore;
	}


	@Override
	public int compare(CohortDetail o1, CohortDetail o2) {
		if (o1.getId()< o2.getId())
			return -1;
		if (o1.getId() > o2.getId())
			return 1;
		return 0;

	}

}
