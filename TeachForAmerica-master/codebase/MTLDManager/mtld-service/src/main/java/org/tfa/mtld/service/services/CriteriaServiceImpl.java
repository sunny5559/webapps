package org.tfa.mtld.service.services;

import java.util.ArrayList;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.Criteria;
import org.tfa.mtld.data.model.CriteriaCategory;
import org.tfa.mtld.data.repository.CriteriaRepository;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.CriteriaCategoryBean;
import org.tfa.mtld.service.bean.CriteriaFormBean;

@Service
public class CriteriaServiceImpl implements CriteriaService {

	@Autowired
	CriteriaRepository criteriaRepository;


	
	
	
	@Override
	public CriteriaFormBean getCriteriaFormBean() throws Exception
          {

		
		//ArrayList<CriteriaBean> beanList = null;
		List<CriteriaCategory> modelList = criteriaRepository.getCriteriaList();
	
		CriteriaCategoryBean criteriaCategoryBeanObj; 
		List<CriteriaCategoryBean> crieriaCategoryBeanList=new ArrayList<CriteriaCategoryBean>();
		CriteriaFormBean crieriaFormBean =new CriteriaFormBean(); 
		CriteriaBean beanObj;
		ArrayList<CriteriaBean> beanList = null;
	
		
		
		if (modelList != null) {
			for (CriteriaCategory category : modelList) {
				
				beanList = new ArrayList<CriteriaBean>();
				criteriaCategoryBeanObj=new CriteriaCategoryBean();
				
				criteriaCategoryBeanObj.setCategoryName(category.getName());
				criteriaCategoryBeanObj.setId(category.getId());
				
				for(Criteria criteria:category.getCriteriaList()){
					beanObj = new CriteriaBean();
					BeanUtils.copyProperties(beanObj, criteria);
					beanList.add(beanObj);
					
				}
				
			
				
						criteriaCategoryBeanObj.setCriteriaBeans(beanList);
						crieriaCategoryBeanList.add(criteriaCategoryBeanObj);
				
				
			}
			
			crieriaFormBean.setCriteriaCategoryBeanList(crieriaCategoryBeanList);
		}
		

		return crieriaFormBean;

	}
	
	

}
