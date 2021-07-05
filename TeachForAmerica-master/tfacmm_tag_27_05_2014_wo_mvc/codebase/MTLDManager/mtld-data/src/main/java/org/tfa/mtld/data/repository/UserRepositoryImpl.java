package org.tfa.mtld.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tfa.mtld.data.model.User;


/**
 * This class is created to implement all the methods defined in the UserService Interface
 * 
 * 
 */

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

	Logger logger = Logger.getLogger(UserRepositoryImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * This method perform the user login for given loginId and password
	 * It is returning the user object for correct loginId and password
	 * 
	 * @param loginId
	 * @param password
	 * @return User
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public User userLogin(String loginId, String password) throws Exception {
		logger.debug("In side UserRepositoryImpl");
		List<User> userList = new ArrayList<User>();
		Session session = null;
		User userDetails = null;
		try {
			session = sessionFactory.openSession();
			String hql = "FROM User user WHERE user.loginId = :loginId and user.password = :password";
			Query query = session.createQuery(hql);
			query.setParameter("loginId", loginId.trim());
			query.setParameter("password", password.trim());
			userList = query.list();
			if (userList != null && userList.size() > 0) {
				userDetails = (User) userList.get(0);
				org.hibernate.Hibernate.initialize(userDetails.getRegion());
				
				if(userDetails.getIsLoggedIn()==null ||userDetails.getIsLoggedIn()==false ){
					updateUserLoggedInStatus(true, userDetails.getUserId());
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception has occured while validating user ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return userDetails;
	}
	
	/**
	 * Update the user status as logged in or not logged in to user table.
	 * 
	 * @param String
	 *            latitude
	 * @param String
	 *            longitude
	 * 
	 * @return none
	 */
	public void updateUserLoggedInStatus(Boolean isLoggedIn, Integer userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = (Query) session
					.createQuery("UPDATE User user set user.isLoggedIn = :isLoggedIn WHERE user.userId = :userId");
			query.setParameter("isLoggedIn", isLoggedIn);
			query.setParameter("userId", userId);
			int result = query.executeUpdate();
			logger.debug("No of rows updated in User table :" + result);
			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			logger.error("User Logged in status Not updated" ,e);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
	}

	@Override
	public void resetUserLoggedInFlag() throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = (Query) session
					.createQuery("UPDATE User user set user.isLoggedIn = :isLoggedIn ");
			query.setParameter("isLoggedIn", Boolean.FALSE);
			int result = query.executeUpdate();
			logger.debug("No of rows updated in User table :" + result);
			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			logger.error("User Logged in status Not updated" ,e);
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		
	}

}
