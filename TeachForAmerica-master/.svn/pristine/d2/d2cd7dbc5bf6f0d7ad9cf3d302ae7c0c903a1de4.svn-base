package org.tfa.mtld.service.utils;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SessionBeanServiceImpl implements SessionBeanService {

	public Map<String, List<?>> cohortMap;

	@Override
	public Map<String, List<?>> getCohortMap() {
		return cohortMap;
	}

	@Override
	public void setCohortMap(Map<String, List<?>> cohortMap) {
		this.cohortMap = cohortMap;
	}
}
