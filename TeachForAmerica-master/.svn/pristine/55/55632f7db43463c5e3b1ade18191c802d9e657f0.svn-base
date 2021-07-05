package org.tfa.mtld.data.repository;

import java.util.List;

import org.tfa.mtld.data.model.School;

public interface SchoolRepository {
	/**
	 * This method is used to get the school details from db based on regionId
	 * 
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	public List<School> getSchoolDetails(Integer regionId) throws Exception;
	/**
	 * This method is used to get the school details from db.
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	public List<School> getSchoolDetails() throws Exception;
	/**
	 * ' This method is used to update the school details. 
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	public void updateSchoolLatLong(School school) throws Exception;
	
	/**
	 * ' This method is used to getting the school object. 
	 * 
	 * @param schoolId
	 * @throws Exception
	 */
	public School getSchoolById(String schoolTFAId) throws Exception;
}
