package org.tfa.mtld.service.services;

import java.util.List;

import org.tfa.mtld.data.model.School;
import org.tfa.mtld.service.bean.SchoolBean;

public interface SchoolService {
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
	
	public List<SchoolBean> updateSchoolDetails(Integer regionId) throws Exception;
	
	public void updateSchoolDetailsOnStartUp() throws Exception;
	public void updateSchoolNullLatLong(Integer regionId) throws Exception;
}
