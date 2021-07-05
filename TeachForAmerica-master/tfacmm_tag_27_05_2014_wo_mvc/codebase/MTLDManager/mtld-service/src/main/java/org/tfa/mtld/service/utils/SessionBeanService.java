package org.tfa.mtld.service.utils;

import java.util.List;
import java.util.Map;

public interface SessionBeanService {
	public Map<String, List<?>> getCohortMap();

	public void setCohortMap(Map<String, List<?>> cohortMap);
}
