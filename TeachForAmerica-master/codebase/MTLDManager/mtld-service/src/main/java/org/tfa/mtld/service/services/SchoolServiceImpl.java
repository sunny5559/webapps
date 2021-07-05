package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.data.repository.SchoolRepository;
import org.tfa.mtld.service.bean.Address;
import org.tfa.mtld.service.bean.SchoolBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.utils.LongLatUtility;

@Service
public class SchoolServiceImpl implements SchoolService {
	
	Logger logger = Logger.getLogger(SchoolServiceImpl.class);
	
	@Autowired
	SchoolRepository schoolRepository;
	
	public List<School> getSchoolDetails(Integer regionId) throws Exception{
		List<School> schoolDetails;
		schoolDetails = schoolRepository.getSchoolDetails(regionId);
		return schoolDetails;
	}
	
	public void updateSchoolNullLatLong (Integer regionId) throws Exception{
		List<School> schools = getSchoolLatLongWithNull(regionId);
		Address address = null;
		for (School school : schools) {
			address = new Address();
			if (school.getAddress() != null && !"".equals(school.getAddress())){
			address.setAddress(school.getAddress());
			address.setCity(school.getCity());
			address.setState(school.getState());
			address.setZipCode(school.getZipCode());
			LongLatUtility.getLongitudeLatitude(address);
			school.setLatitude(address.getLatitude());
			school.setLongitude(address.getLongitude());
			schoolRepository.updateSchoolLatLong(school);
			}else {
				logger.error(TFAConstants.SCHOOL_ADDRESS_NOT_FOUND
						+ school.getSchoolId());
			}
		}
	}
	
	private List<School> getSchoolLatLongWithNull(Integer regionId) throws Exception{
		List<School> schools;
		schools = schoolRepository.getSchoolLatLongWithNull(regionId);
		return schools;
	}
	
	public List<School> getSchoolDetails() throws Exception{
		List<School> schoolDetails;
		schoolDetails = schoolRepository.getSchoolDetails();
		return schoolDetails;
	}
	
	public void updateSchoolLatLong(School school) throws Exception{
		schoolRepository.updateSchoolLatLong(school);
	}
	
	// Method added by Arun
		public List<SchoolBean> updateSchoolDetails(Integer regionId)
				throws Exception {
			List<School> school = null;
			List<SchoolBean> schoolDetails = new ArrayList<SchoolBean>();
			Address address = null;
			SchoolBean schoolBean = null;
			school = schoolRepository.getSchoolDetails(regionId);
			if (null != school && !"".equals(school)) {
				for (int i = 0; i < school.size(); i++) {
					schoolBean = new SchoolBean();
					BeanUtils.copyProperties(schoolBean, school.get(i));
					if (null != schoolBean && !"".equals(schoolBean)) {
						schoolDetails.add(schoolBean);
					}
				}
			}
			for (School schoolDetails1 : school) {
				address = new Address();
				if (schoolDetails1.getAddress() != null && !"".equals(schoolDetails1.getAddress())) {
					address.setAddress(schoolDetails1.getAddress());
					address.setCity(schoolDetails1.getCity());
					address.setState(schoolDetails1.getState());
					address.setZipCode(schoolDetails1.getZipCode());

					if (schoolDetails1.getLatitude() == null
							|| ("").equals(schoolDetails1.getLatitude())
							|| schoolDetails1.getLongitude() == null
							|| ("").equals(schoolDetails1.getLongitude())) {
						LongLatUtility.getLongitudeLatitude(address);
						schoolDetails1.setLatitude(address.getLatitude());
						schoolDetails1.setLongitude(address.getLongitude());

						schoolRepository.updateSchoolLatLong(schoolDetails1);
					}
				} else {
					logger.error(TFAConstants.SCHOOL_ADDRESS_NOT_FOUND
							+ schoolDetails1.getSchoolId());
				}
			}
			return schoolDetails;
		}

		public void updateSchoolDetailsOnStartUp() throws Exception {
			List<School> school = null;
			Address address = null;
			school = schoolRepository.getSchoolDetails();
			if (null != school && !"".equals(school)) {

				for (School schoolDetails : school) {
					address = new Address();
					if (schoolDetails.getAddress() != null && !"".equals(schoolDetails.getAddress())) {
						address.setAddress(schoolDetails.getAddress());
						address.setCity(schoolDetails.getCity());
						address.setState(schoolDetails.getState());
						address.setZipCode(schoolDetails.getZipCode());

						if (null ==schoolDetails.getLatitude()
								|| null==schoolDetails.getLongitude()) {
							LongLatUtility.getLongitudeLatitude(address);
							schoolDetails.setLatitude(address.getLatitude());
							schoolDetails.setLongitude(address.getLongitude());

							schoolRepository.updateSchoolLatLong(schoolDetails);
						}
					} else {
						logger.error(TFAConstants.SCHOOL_ADDRESS_NOT_FOUND
								+ schoolDetails.getSchoolId());
					}
				}
			}
		}

		// Arun Ended

}
