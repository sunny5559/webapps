package org.tfa.mtld.data.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.School;

@Repository
public class SchoolRepositoryImpl implements SchoolRepository {
	@Autowired
	SessionFactory sessionFactory;

	Logger logger = Logger.getLogger(SchoolRepositoryImpl.class);

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * This method is used to get the school details from db based on regionId
	 * 
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<School> getSchoolDetails(Integer regionId) throws Exception {
		Session session = null;
		List<School> schooldDetails = null;
		try {
			session = sessionFactory.openSession();
			Query query = (Query) session
					.createQuery("from School WHERE region.regionId = :regionId");
			query.setParameter("regionId", regionId);
			schooldDetails = (List<School>) query.list();

		} catch (Exception exception) {
			logger.error(
					"Exception has occured while getSchoolDetails for Region ID "
							+ regionId, exception);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return schooldDetails;
	}

	/**
	 * This method is used to get the school details from db.
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<School> getSchoolDetails() throws Exception {
		Session session = null;
		List<School> schooldDetails = null;
		try {
			session = sessionFactory.openSession();
			Query query = (Query) session.createQuery("from School ");
			schooldDetails = (List<School>) query.list();

		} catch (Exception exception) {
			logger.error("Exception has occured while getSchoolDetails ",
					exception);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return schooldDetails;
	}

	/**
	 * Update the lat and logn for the school address.
	 * 
	 * @param String
	 *            latitude
	 * @param String
	 *            longitude
	 * 
	 * @return none
	 */
	@Override
	public void updateSchoolLatLong(School school) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = (Query) session
					.createQuery("UPDATE School sc set sc.latitude = :latitude, sc.longitude = :longitude WHERE sc.schoolId = :schoolId");
			query.setParameter("latitude", school.getLatitude());
			query.setParameter("longitude", school.getLongitude());
			query.setParameter("schoolId", school.getSchoolId());
			int result = query.executeUpdate();
			logger.info("No of rows updated in school table--->" + result);
			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception has occured while updateSchoolLatLong ", e);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
	}

	@Override
	public School getSchoolById(String schoolTFAId) throws Exception {
		Session session = null;
		School school = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(School.class);
			criteria.add(Restrictions.eq("schoolTfaUid", schoolTFAId));

			List existingSchools = criteria.list();
			if (existingSchools != null && existingSchools.size() > 0) {
				school = (School) existingSchools.get(0);
			}
			
		} catch (Exception e) {
			logger.error("Exception has occured while getSchoolById ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return school;

	}
}
