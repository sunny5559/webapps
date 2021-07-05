package org.tfa.mtld.web.utils;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tfa.mtld.service.services.MTLDService;
import org.tfa.mtld.service.services.SchoolService;
import org.tfa.mtld.service.services.UserService;

@Component
public class UpdateOnStartUpUtility {
	Logger logger = Logger.getLogger(UpdateOnStartUpUtility.class);

	@Autowired
	public SchoolService schoolService;
	
	@Autowired 
	public MTLDService mtldService;
	
	@Autowired
	@Qualifier("UserService")
	private UserService userService;
	
	@PostConstruct
	public void updateLatLong() throws Exception {
		logger.debug("updateLatLong started");
		schoolService.updateSchoolDetailsOnStartUp();
		mtldService.updateMTLDLatLongOnStartUp();
		userService.resetUserLoggedInFlag();
	}
}
