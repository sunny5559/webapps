package org.tfa.mtld.data.repository;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tfa.mtld.data.model.Region;

/**
 * @author divesh.solanki
 *
 */
@Repository
public class RegionRepositoryImpl implements RegionRepository {

	@Autowired
	SessionFactory sessionFactory;
	
	Logger logger = Logger.getLogger(RegionRepositoryImpl.class);
	
	@Override
	public Region getRegionByRegionCode(String regionCode) throws Exception {
		// TODO Auto-generated method stub
		Session session= null ;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Region.class);
			criteria.add(Restrictions.eq("regionCode", regionCode));
			java.util.List regions = criteria.list();
			if(regions != null && !regions.isEmpty())
				return (Region) regions.get(0);
			
		} catch (Exception e) {	
			logger.error("Error occured while fetching region by region code" , e );
			throw e; 
		} finally{
			if(session != null && session.isOpen()){
				session.flush();
				session.close();
			}
		}
		return null;
		
	}

}
