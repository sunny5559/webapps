package org.tfa.mtld.data.repository;


import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tfa.mtld.data.model.MTLD;
import org.tfa.mtld.data.model.MTLDSchool;
import org.tfa.mtld.data.model.School;


@Repository
@Transactional(readOnly = false)
public class MTLDSchoolRepositoryImpl implements MTLDSchoolRepository {

	Logger logger = Logger.getLogger(MTLDSchoolRepositoryImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	SchoolRepository schoolRepository;
	
	@Autowired
	MtldCmRepository mtldCmRepository;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	@Override
	public Integer saveMTLDSchool(MTLDSchool mtldSchool) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer retVal = 0;
		

		try {

			School school = schoolRepository.getSchoolByTFASchoolId(mtldSchool
					.getSchool().getTfaSchoolId());
			MTLD mtld = mtldCmRepository.getMTLDByTFAId(mtldSchool.getMtld()
					.getPlacementAdvisorTfaId());

			if (school != null && mtld != null) {
				tx = session.beginTransaction();
				mtldSchool.setMtld(mtld);
				mtldSchool.setSchool(school);
				
				Query query = session
						.createQuery("from MTLDSchool  WHERE mtld.id = :mtldId and school.schoolId = :schoolId");
				query.setParameter("mtldId", mtld.getId());
				query.setParameter("schoolId", school.getSchoolId());
				List<MTLDSchool> mtldSchools = query.list();
				
				if(mtldSchools.size()==0){
					retVal = (Integer) session.save(mtldSchool);
					tx.commit();
				}
				
				
			
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception at saveMTLDSchool ", e);

			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		
		return retVal;

	}
	}
