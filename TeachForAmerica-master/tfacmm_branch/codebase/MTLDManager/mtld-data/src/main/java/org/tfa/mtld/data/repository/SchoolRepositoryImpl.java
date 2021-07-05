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
			query.setCacheable(true);
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
			Criteria criteria = session.createCriteria(School.class)
					.add(Restrictions.isNotNull("address"))
					.add(Restrictions.isNotNull("city"))
					.add(Restrictions.isNotNull("state"))
					.add(Restrictions.isNotNull("zipCode"))
					.add(Restrictions.isNull("latitude"))
					.add(Restrictions.isNull("longitude"));
			criteria.setCacheable(true);
			schooldDetails = (List<School>) criteria.list();

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
			logger.info("No of rows updated for Lat Long in school table--->"
					+ result);
			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null)
				logger.error("Exception has occured while updateSchoolLatLong ", e);
				tx.rollback();
			
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
			criteria.add(Restrictions.eq("tfaSchoolId", schoolTFAId));
			//criteria.setCacheable(true);
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

	@Override
	public School getSchoolByTFASchoolId(String tfaSchoolId) throws Exception {

		Session session = null;
		School school = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(School.class);
			criteria.add(Restrictions.eq("tfaSchoolId", tfaSchoolId));
			criteria.setCacheable(true);
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

	@Override
	public List<School> getSchoolLatLongWithNull(Integer regionId)
			throws Exception {

		Session session = null;
		List<School> schools = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(School.class)
					.add(Restrictions.isNotNull("address"))
					.add(Restrictions.isNull("latitude"))
					.add(Restrictions.isNull("longitude"))
					.add(Restrictions.eq("region.regionId", regionId));
			criteria.setCacheable(true);
			schools = criteria.list();
		} catch (Exception e) {
			logger.error("Exception has occured while getSchoolLatLongWithNull ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return schools;
	}
}
