package org.tfa.mtld.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MTLD class mapped with mtld table.
 * 
 * @author Lovely Ram
 * @version 1.0, 16 May, 2014.
 */

@Entity
@Table(name = "MTLD_SCHOOL")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="MTLDSchool")
public class MTLDSchool implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "mtld_id")
	private MTLD mtld;
	
	@ManyToOne
	@JoinColumn(name = "school_Id")
	private School school;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MTLD getMtld() {
		return mtld;
	}
	public void setMtld(MTLD mtld) {
		this.mtld = mtld;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}

	
	
	

}
