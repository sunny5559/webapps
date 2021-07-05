package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.Region;
import org.tfa.mtld.data.repository.MtldCmRepository;
import org.tfa.mtld.data.repository.RegionRepository;
import org.tfa.mtld.service.bean.Address;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.utils.LongLatUtility;

/**
 * @author divesh.solanki
 * 
 */
@Service
public class MTLDServiceImpl implements MTLDService {
	Logger logger = Logger.getLogger(MTLDServiceImpl.class);
	@Autowired
	MtldCmRepository mtldCmRepository;

	@Autowired
	RegionRepository regionRepository;
	
	@Override
	public void save(List<MTLDBean> mltds) throws Exception {
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = mltds.iterator(); iterator.hasNext();) {
			MTLDBean mtldBean = (MTLDBean) iterator.next();
			MTLD mtld = new MTLD();
			String[] ignoreProperties = { "id" };
			BeanUtils.copyProperties(mtldBean, mtld, ignoreProperties);
//			mtld.setAlum("Y".equalsIgnoreCase(mtldBean.getAlum()));			
			if(mtldBean.getCorpsRegionName() != null && "".equalsIgnoreCase(mtldBean.getCorpsRegionName().trim())){
				Region corpRegion = regionRepository.getRegionByRegionCode(mtldBean.getCorpsRegionName());
				mtld.setCorpsRegion(corpRegion); 
			}
			if(mtldBean.getRegionCode() != null && !"".equalsIgnoreCase(mtldBean.getRegionCode().trim())){
				Region region = regionRepository.getRegionByRegionCode(mtldBean.getRegionCode());
				mtld.setRegion(region); 
			}
			getLatAndLong(mtld);
			mtldCmRepository.saveMTLD(mtld);
		}

	}
	
	private void getLatAndLong(MTLD mtld) {
		try {
			Address address = new Address();
			if (mtld.getAddress() != null) {
				address.setAddress(mtld.getAddress());
				address.setCity(mtld.getCity());
				address.setState(mtld.getState());
				address.setZipCode(mtld.getZipCode());
				if (mtld.getLatitude() == null
						|| ("").equals(mtld.getLatitude())
						|| mtld.getLongitude() == null
						|| ("").equals(mtld.getLongitude())) {
					LongLatUtility.getLongitudeLatitude(address);
					mtld.setLatitude(address.getLatitude());
					mtld.setLongitude(address.getLongitude());
					
				}
			} else {
				logger.error("Address Not found for MTLD ID"
						+ mtld.getId());
			}
		} catch (Exception e) {
			logger.error("An error occured while geocoding and get lat and long for MTLD with address" 
					+ mtld.getAddress() + ", " +mtld.getCity()+ " , "+ mtld.getState()+ mtld.getZipCode() + "\t " , e);
		}
	}

	@Override
	public void updateMTLDLatLongOnStartUp() throws Exception{
			List<MTLD> mtld = null;
			Address address = null;
			mtld = mtldCmRepository.getMTLDList();
			if (null != mtld && !"".equals(mtld)) {

				for (MTLD mtldDetails : mtld) {
					address = new Address();
					if (mtldDetails.getAddress() != null) {
						address.setAddress(mtldDetails.getAddress());
						address.setCity(mtldDetails.getCity());
						address.setState(mtldDetails.getState());
						address.setZipCode(mtldDetails.getZipCode());

						if (mtldDetails.getLatitude() == null
								|| ("").equals(mtldDetails.getLatitude())
								|| mtldDetails.getLongitude() == null
								|| ("").equals(mtldDetails.getLongitude())) {
							LongLatUtility.getLongitudeLatitude(address);
							mtldDetails.setLatitude(address.getLatitude());
							mtldDetails.setLongitude(address.getLongitude());

							mtldCmRepository.updateMTLDLatLong(mtldDetails);
						}
					} else {
						logger.error("Address Not found for MTLD ID"
								+ mtldDetails.getId());
					}
				}
			}
		}
	
	public void updateMTLDLatLong(Integer regionId)
			throws Exception {
		List<MTLD> mtld = null;
		List<MTLDBean> mtldDetails = new ArrayList<MTLDBean>();
		Address address = null;
		MTLDBean mtldBean = null;
		mtld = mtldCmRepository.getMTLDListByRegionId(regionId);
		if (null != mtld && !"".equals(mtld)) {
			for (int i = 0; i < mtld.size(); i++) {
				mtldBean = new MTLDBean();
				BeanUtils.copyProperties(mtldBean, mtld.get(i));
				if (null != mtldBean && !"".equals(mtldBean)) {
					mtldDetails.add(mtldBean);
				}
			}
		}
		for (MTLD mtldDetails1 : mtld) {
			address = new Address();
			if (mtldDetails1.getAddress() != null) {
				address.setAddress(mtldDetails1.getAddress());
				address.setCity(mtldDetails1.getCity());
				address.setState(mtldDetails1.getState());
				address.setZipCode(mtldDetails1.getZipCode());
				if (mtldDetails1.getLatitude() == null
						|| ("").equals(mtldDetails1.getLatitude())
						|| mtldDetails1.getLongitude() == null
						|| ("").equals(mtldDetails1.getLongitude())) {
					LongLatUtility.getLongitudeLatitude(address);
					mtldDetails1.setLatitude(address.getLatitude());
					mtldDetails1.setLongitude(address.getLongitude());

					mtldCmRepository.updateMTLDLatLong(mtldDetails1);
				}
			} else {
				logger.error("Address Not found for MTLD ID"
						+ mtldDetails1.getId());
			}
		}
	}
	
	
}
