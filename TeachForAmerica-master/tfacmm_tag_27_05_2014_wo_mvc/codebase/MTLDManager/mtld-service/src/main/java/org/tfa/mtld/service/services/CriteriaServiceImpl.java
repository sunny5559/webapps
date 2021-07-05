package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.Criteria;
import org.tfa.mtld.data.model.CriteriaCategory;
import org.tfa.mtld.data.repository.CriteriaRepository;
import org.tfa.mtld.service.bean.CriteriaBean;

@Service
public class CriteriaServiceImpl implements CriteriaService {

	@Autowired
	CriteriaRepository criteriaRepository;

	@Override
	public HashMap<String, List<CriteriaBean>> getCriteriaList()
			throws Exception {

		ArrayList<CriteriaBean> beanList = null;
		List<CriteriaCategory> modelList = criteriaRepository.getCriteriaList();
		CriteriaBean beanObj;

		LinkedHashMap<String, List<CriteriaBean>> criteriaMap = new LinkedHashMap<String, List<CriteriaBean>>();
		if (modelList != null) {
			for (CriteriaCategory category : modelList) {
				beanList = new ArrayList<CriteriaBean>();

				for (Criteria model : category.getCriteriaList()) {

					beanObj = new CriteriaBean();

					BeanUtils.copyProperties(beanObj, model);

					beanList.add(beanObj);

				}
				criteriaMap.put(category.getName(), beanList);
			}
		}

		return criteriaMap;

	}

}
