package org.tfa.mtld.service.services;

import java.util.Set;

import org.tfa.mtld.service.bean.MTLDSchoolBean;

public interface MTLDSchoolService {
	
	public Integer saveMTLDSchool(Set<MTLDSchoolBean> mtldSchoolBeans) throws Exception;

}
