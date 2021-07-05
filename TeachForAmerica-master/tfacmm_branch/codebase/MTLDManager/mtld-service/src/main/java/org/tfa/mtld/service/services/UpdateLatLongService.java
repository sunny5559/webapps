package org.tfa.mtld.service.services;

import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * @author vaibhav.poorey
 *
 */

@Service
public class UpdateLatLongService {
	
	@Autowired
	SchoolService schoolService;
	
	@Autowired
	MTLDService mtldService;
	
	Logger logger = Logger.getLogger(UpdateLatLongService.class);
	
	
	/*Below method will execute on given cron expression value to update latitude and longitude for School and MTLD.
	 * Currently cron expression is set for midnight (i.e 12 am) everyday.
	 * */
	
	@Scheduled(cron="0 0 * * * *") 
    public void updateLatLong()
    {
		logger.info("UpdateLatLongService.updateLatLong() method executed at : "+new Date());
		
        try {
			schoolService.updateSchoolDetailsOnStartUp();
			mtldService.updateMTLDLatLongOnStartUp();
		} catch (Exception e) {
			
			logger.error("Exceptin in UpdateLatLongService.updateLatLong()" , e);
		}
        
        logger.info("UpdateLatLongService.updateLatLong() method ended at : "+new Date());
    }

}
