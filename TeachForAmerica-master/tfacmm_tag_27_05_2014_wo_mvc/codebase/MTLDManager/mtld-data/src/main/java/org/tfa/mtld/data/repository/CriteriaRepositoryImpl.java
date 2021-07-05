package org.tfa.mtld.data.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tfa.mtld.data.model.CriteriaCategory;

@Repository
public class CriteriaRepositoryImpl implements CriteriaRepository {

	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	Logger logger = Logger.getLogger(CriteriaRepositoryImpl.class);

	@Override
	public List<CriteriaCategory> getCriteriaList() throws Exception {

		Session session = null;
		List<CriteriaCategory> criteriaList = null;
		try {
			logger.debug("In CriteriaRepositoryImpl.getCriteriaList() method");
			session = sessionFactory.openSession();

			Query q = session.createQuery("from CriteriaCategory");

			criteriaList = (List<CriteriaCategory>) q.list();
			logger.debug("Criteria List Size -" + criteriaList.size());
			

		} catch (Exception e) {
			logger.error(
					"Exception in CriteriaRepositoryImpl.getCriteriaList() method",
					e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}

		return criteriaList;

	}

}
