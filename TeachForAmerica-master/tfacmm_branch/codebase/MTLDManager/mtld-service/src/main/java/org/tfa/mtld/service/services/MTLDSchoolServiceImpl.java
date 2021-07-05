package org.tfa.mtld.service.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.MTLDSchool;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.data.repository.MTLDSchoolRepository;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.bean.MTLDSchoolBean;

@Service
public class MTLDSchoolServiceImpl implements MTLDSchoolService {

	Logger logger = Logger.getLogger(MTLDSchoolServiceImpl.class);
	
	@Autowired
	MTLDSchoolRepository mtldSchoolRepository;
	
	@Override
	public Integer saveMTLDSchool(Set<MTLDSchoolBean> mtldSchoolBeans)
			throws Exception {
	
		Integer rowsUpdated=0;
		
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = mtldSchoolBeans.iterator(); iterator.hasNext();) {
			
			MTLDSchoolBean mtldSchoolBean = (MTLDSchoolBean) iterator.next();
			MTLD mtld = new MTLD();
			MTLDSchool mtldSchool=new MTLDSchool();
			
			mtld.setPlacementAdvisorTfaId(mtldSchoolBean.getMtld().getPlacementAdvisorTfaId());
			School school=new School();
			school.setTfaSchoolId(mtldSchoolBean.getSchool().getTfaSchoolId());
			
			mtldSchool.setMtld(mtld);
			mtldSchool.setSchool(school);
			
			
			
			rowsUpdated=mtldSchoolRepository.saveMTLDSchool(mtldSchool);
			
		}
		return rowsUpdated;
		
		
	}
}
