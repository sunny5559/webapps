package org.tfa.mtld.service.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.School;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.data.repository.SchoolRepository;
import org.tfa.mtld.service.bean.CMBean;

@Service
public class CorpsMemberServiceImpl implements CorpsMemberService {
	
	@Autowired
	MtldCmRepository mtldCmRepository;
	
	@Autowired
	SchoolRepository schoolRepository;

	@Override
	public void saveCoprsMember(List<CMBean> cmBeans,User sessionUser) throws Exception {
		
		Date currentDate = new Date(); 
		School school = null;
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = cmBeans.iterator(); iterator.hasNext();) {
			CMBean cmBean = (CMBean) iterator.next();
			CorpsMember corps = new CorpsMember();
			String[] ignoreProperties = { "id" };
			BeanUtils.copyProperties(cmBean, corps, ignoreProperties);
			corps.setRegion(sessionUser.getRegion());
			corps.setCreatedBy(sessionUser.getLoginId());
			corps.setCreatedDate(currentDate);
			if(null != cmBean.getSchoolBean()){
			 school = schoolRepository.getSchoolById(cmBean.getSchoolBean().getTfaSchoolId());
			}
			corps.setSchool(school);

			mtldCmRepository.saveCorpsMember(corps);
		}

	}

}
