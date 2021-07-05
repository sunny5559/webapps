package org.tfa.mtld.service.services;

import java.util.List;

import org.tfa.mtld.service.bean.MTLDBean;

/**
 * @author divesh.solanki
 * 
 */
public interface MTLDService {

	public void save(List<MTLDBean> mltds) throws Exception;
	
	public void updateMTLDLatLongOnStartUp() throws Exception;
	public void updateMTLDLatLong(Integer regionId) throws Exception;

}
