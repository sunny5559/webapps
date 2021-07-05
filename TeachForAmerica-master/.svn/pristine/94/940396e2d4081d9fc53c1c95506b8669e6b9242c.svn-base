package org.tfa.mtld.data.repository;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

@Repository
@Transactional(readOnly = false)
public class MtldCmRepositoryImpl implements MtldCmRepository {

	Logger logger = Logger.getLogger(MtldCmRepositoryImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Will return list of CM including hired and unhired, and only unassigned,
	 * list will not include any assigned cm which are there in finalize
	 * cohort(s).
	 * 
	 * @param regionId
	 * @return
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CorpsMember> getCorpMemberListByRegionId(Integer regionId)
			throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getCorpMemberListByRegionIdr in time "
				+ System.currentTimeMillis());
		List<CorpsMember> corps = null;
		Session session = null;
		
		try {
			session = sessionFactory.openSession();
			Query query = session
					.createQuery("from CohortDetail WHERE cohort.isFinalCohort = :isFinalCohort and cohort.region.regionId = :regionId");
			query.setParameter("isFinalCohort", Boolean.TRUE);
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			List<CohortDetail> cohortDetails = query.list();
			Integer[] cmIDs = new Integer[cohortDetails.size()];
			int i = 0;
			if (cohortDetails != null) {
				for (CohortDetail cohortDetail : cohortDetails) {
					if (cohortDetail.getCorpMember() != null) {
						cmIDs[i++] = cohortDetail.getCorpMember().getId();
					}
				}
			}
			Criteria criteria = session.createCriteria(CorpsMember.class);
			criteria.add(Restrictions.eq("region.regionId", regionId));
			query.setCacheable(true);
			if (cmIDs != null && cmIDs.length > 0) {
				criteria.add(Restrictions.and(Restrictions.not(Restrictions.in(
						"id", cmIDs))));
			}

			//
			// criteria.add(Restrictions.eq("region.regionId", regionId));
			// if (cmIDs != null && cmIDs.length > 0) {
			// criteria.add(Restrictions.and(Restrictions.in("id", cmIDs)));
			// }
			criteria.setCacheable(true);
			corps = (List<CorpsMember>) criteria.list();

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Exception has occured while fetch Corp member ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getCorpMemberListByRegionId executin time taken "
				+ timeDiff);
		return corps;
	}

	/**
	 * return all the MTLD for login user's Region
	 * 
	 * @param regionId
	 * @return
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MTLD> getMTLDListByRegionId(Integer regionId) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info(" In getMTLDListByRegionId(Integer regionId) in time "
				+ System.currentTimeMillis());
		List<MTLD> mtlds = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			
			Query query = (Query) session
					.createQuery("from MTLD WHERE region.regionId = :regionId");
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			
			mtlds = query.list();
			
		} catch (Exception e) {
			logger.error("Exception has occured while fetch MTLD  ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getMTLDListByRegionId(Integer regionId) executin time taken "
				+ timeDiff);
		return mtlds;

	}

	/**
	 * return all the MTLD for login user's Region
	 * 
	 * @param regionId
	 * @return
	 * @throws Exception
	 * 
	 *             select * from mtld where region_id=22 and mtld_id not in
	 *             (select mtld_id from cohort where IS_FINAL_COHORT=1 and
	 *             region_id=22)
	 * 
	 * @author lovely.ram
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MTLD> getUnassignedMTLDListByRegionId(Integer regionId)
			throws Exception {
		List<MTLD> mtlds = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			Query query = (Query) session
					.createQuery("from MTLD WHERE region.regionId = :regionId and id not in (select id from Cohort where isFinalCohort = true and region.regionId = :regionId)");
			query.setParameter("regionId", regionId);
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			mtlds = query.list();

		} catch (Exception e) {
			logger.error("Exception has occured while fetch MTLD  ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return mtlds;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MTLD> getMTLDList() throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getMTLDList in time " + System.currentTimeMillis());
		List<MTLD> mtlds = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(MTLD.class)
					.add(Restrictions.isNotNull("address"))
					.add(Restrictions.isNotNull("city"))
					.add(Restrictions.isNotNull("state"))
					.add(Restrictions.isNotNull("zipCode"))
					.add(Restrictions.isNull("latitude"))
					.add(Restrictions.isNull("longitude"));
			criteria.setCacheable(true);
			mtlds = criteria.list();
			
		} catch (Exception e) {
			logger.error("Exception has occured while fetch MTLD  ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getMTLDList executin time taken " + timeDiff);
		return mtlds;

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
	public void updateMTLDLatLong(MTLD mtld) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In updateMTLDLatLong in time "
				+ System.currentTimeMillis());
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = (Query) session
					.createQuery("UPDATE MTLD sc set sc.latitude = :latitude, sc.longitude = :longitude WHERE sc.id = :id");
			query.setParameter("latitude", mtld.getLatitude());
			query.setParameter("longitude", mtld.getLongitude());
			query.setParameter("id", mtld.getId());
			query.setCacheable(true);
			int result = query.executeUpdate();
			logger.info("No of rows updated in MTLD table--->" + result);
			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception at updateMTLDLatLong ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out updateMTLDLatLong executin time taken " + timeDiff);
	}

	/**
	 * * Will return list of CM including unhired, and only unassigned this will
	 * not include any assigned cm which are there in finalize cohort(s).
	 * 
	 * @param regionId
	 * @return
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CorpsMember> getUnhiredCorpMemberListByRegionId(Integer regionId)
			throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getUnhiredCorpMemberListByRegionId in time "
				+ System.currentTimeMillis());
		List<CorpsMember> corps = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session
					.createQuery("from CohortDetail WHERE cohort.isFinalCohort = :isFinalCohort and cohort.region.regionId = :regionId");
			query.setParameter("isFinalCohort", Boolean.TRUE);
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			List<CohortDetail> cohortDetails = query.list();
			Integer[] cmIDs = new Integer[cohortDetails.size()];
			int i = 0;
			if (cohortDetails != null) {
				for (CohortDetail cohortDetail : cohortDetails) {
					if (cohortDetail.getCorpMember() != null) {
						cmIDs[i++] = cohortDetail.getCorpMember().getId();
					}
				}
			}
			Criteria criteria = session.createCriteria(CorpsMember.class);
			criteria.add(Restrictions.eq("region.regionId", regionId));
			if (cmIDs != null && cmIDs.length > 0) {
				criteria.add(Restrictions.and(Restrictions.not(Restrictions.in(
						"id", cmIDs))));
			}
			criteria.add(Restrictions.isNull("school.schoolId"));
			criteria.setCacheable(true);
			corps = (List<CorpsMember>) criteria.list();

		} catch (Exception e) {
			logger.error(
					"Exception has occured while fetch Unhired Corps member ",
					e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getUnhiredCorpMemberListByRegionId executin time taken "
				+ timeDiff);
		return corps;
	}

	/**
	 * Will return list of CM including only hired, and only unassigned. List
	 * will not include any assigned cm which are there in finalize cohort(s).
	 * 
	 * @param regionId
	 * @param isUnhierdCMIncluded
	 * @return
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CorpsMember> getHiredCorpMemberListByRegionId(Integer regionId)
			throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getHiredCorpMemberListByRegionId(Integer regionId) in time "
				+ System.currentTimeMillis());
		List<CorpsMember> corps = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session
					.createQuery("from CohortDetail WHERE cohort.isFinalCohort = :isFinalCohort and cohort.region.regionId = :regionId");
			query.setParameter("isFinalCohort", Boolean.TRUE);
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			List<CohortDetail> cohortDetails = query.list();
			Integer[] cmIDs = new Integer[cohortDetails.size()];
			int i = 0;
			if (cohortDetails != null) {
				for (CohortDetail cohortDetail : cohortDetails) {
					if (cohortDetail.getCorpMember() != null) {
						cmIDs[i++] = cohortDetail.getCorpMember().getId();
					}
				}
			}
			Criteria criteria = session.createCriteria(CorpsMember.class);
			criteria.add(Restrictions.eq("region.regionId", regionId));
			if (cmIDs != null && cmIDs.length > 0) {
				criteria.add(Restrictions.and(Restrictions.not(Restrictions.in(
						"id", cmIDs))));
			}
			criteria.add(Restrictions.isNotNull("school.schoolId"));
			criteria.setCacheable(true);
			corps = (List<CorpsMember>) criteria.list();

		} catch (Exception e) {
			logger.error(
					"Exception has occured while fetch hired Corps member ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getHiredCorpMemberListByRegionId(Integer regionId) executin time taken "
				+ timeDiff);
		return corps;
	}

	/**
	 * Will return list of cohorts which are finalize for a region
	 * 
	 * @param regionId
	 * @return List of Cohort
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Cohort> getCohortListByRegionId(Integer regionId)
			throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getCohortListByRegionId in time "
				+ System.currentTimeMillis());
		Session session = null;
		List<Cohort> cohorts = null;
		try {
			session = sessionFactory.openSession();
			/*
			 * String sql =
			 * "select * from cohort where is_final_cohort=1 and region_id = :regionId"
			 * ; SQLQuery query =
			 * session.createSQLQuery(sql).addEntity(Cohort.class);
			 */
			Query query = (Query) session
					.createQuery("from Cohort WHERE isFinalCohort = true and region.regionId = :regionId");
			query.setParameter("regionId", regionId);

			//query.addEntity(Cohort.class);
			query.setCacheable(true);

			// query.addEntity(Cohort.class);

			cohorts = (List<Cohort>) query.list();
			
		} catch (Exception e) {

			logger.error(
					"Exception has occured while fetch finalize cohort list ",
					e);
			throw e;

		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getCohortListByRegionId executin time taken "
				+ timeDiff);
		return cohorts;
	}

	@Override
	public Integer saveMTLD(MTLD mtld) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info(" In saveMTLD in time " + System.currentTimeMillis());
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer retVal = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(MTLD.class);
			criteria.add(Restrictions.eq("PlacementAdvisorTfaId",
					mtld.getPlacementAdvisorTfaId()));
			List mtlds = criteria.list();
			if (mtlds != null && mtlds.size() > 0) {
				String[] ignoreProperties = { "id", "priorSchoolsWorked",
						"createdDate", "updatedDate" };
				BeanUtils.copyProperties(mtld, mtlds.get(0), ignoreProperties);
				session.saveOrUpdate(mtlds.get(0));
				retVal = mtld.getId();
			} else {
				retVal = (Integer) session.save(mtld);
			}

			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception at saveMTLD ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out saveMTLD executin time taken " + timeDiff);
		return retVal;

	}

	/**
	 * This method returns the cohort object by id.
	 * 
	 * @param cohortId
	 *            cohort id for which object needs to be fetched.
	 * 
	 * @return Cohort object.
	 */
	@Override
	@Transactional(readOnly = true)
	public Cohort getCohort(Integer cohortId) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info(" In getCohort in time " + System.currentTimeMillis());
		Session session = sessionFactory.openSession();
		Cohort cohort = null;
		try {
			cohort = (Cohort) session.get(Cohort.class, cohortId);
			
			if (cohort != null && cohort.getCohortDetails() != null) {
				Collections.sort(cohort.getCohortDetails(), new CohortDetail());
			}
		} catch (Exception e) {
			logger.error("Exception at getCohort ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getCohort executin time taken " + timeDiff);
		return cohort;
	}

	/**
	 * This method returns the cohort object by id.
	 * 
	 * @param cohortId
	 *            cohort id for which object needs to be fetched.
	 * 
	 * @return Cohort object.
	 */
	/*
	 * @Override public Cohort getCorpsCountByCohort(int cohortId) { Session
	 * session = sessionFactory.openSession(); Cohort cohort = (Cohort)
	 * session.get(Cohort.class, cohortId); session.close(); return cohort;
	 * 
	 * }
	 */

	/**
	 * This method calls the db to add/update cohort and cohort details.
	 * 
	 * @param cohort
	 *            Cohort which needs to be added/updated.
	 * 
	 * @return none
	 */
	@Override
	public void saveCohortList(List<Cohort> cohortListToSave) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info(" In saveCohort in time " + System.currentTimeMillis());
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < cohortListToSave.size(); i++) {
				Cohort cohort1 = (Cohort) cohortListToSave.get(i);
				session.saveOrUpdate(cohort1);
				if (i % cohortListToSave.size() == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
				logger.error("Exception has occured while saving Cohort ", e);
				throw e;
			}
		} finally {
			// Added by Divesh Solanki session should be closed here.
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info(" Out saveCohort executin time taken " + timeDiff);
	}

	/**
	 * This method calls the db to add/update cohort and cohort details.
	 * 
	 * @param cohort
	 *            Cohort which needs to be added/updated.
	 * 
	 * @return none
	 */
	@Override
	public void saveCohort(Cohort cohort) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info(" In saveCohort in time " + System.currentTimeMillis());
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(cohort);

			tx.commit();

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
				logger.error("Exception has occured while saving Cohort ", e);
				throw e;
			}
		} finally {
			// Added by Divesh Solanki session should be closed here.
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info(" Out saveCohort executin time taken " + timeDiff);
	}

	/**
	 * Method to get CorpsMember object by id.
	 * 
	 * @param corpsMemberId
	 *            The corps member for which object needs to be fetched.
	 * 
	 * @return CorpsMember object.
	 */
	@Override
	@Transactional(readOnly = true)
	public CorpsMember getCorpsMemberById(int corpsId) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getCorpsMemberById in time "
				+ System.currentTimeMillis());
		Session session = sessionFactory.openSession();
		CorpsMember corpsMember = null;
		try {
			corpsMember = (CorpsMember) session.get(CorpsMember.class, corpsId);

		} catch (Exception e) {
			logger.error("Exception at getCorpsMemberById ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getCorpsMemberById executin time taken " + timeDiff);
		return corpsMember;
	}

	/*
	 * @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	 * public Integer saveMTLDTrans(MTLD mtld) throws Exception {
	 * 
	 * Session session = sessionFactory.openSession(); Transaction tx = null;
	 * Integer retVal = null; try { tx = session.beginTransaction(); retVal =
	 * (Integer) session.save(mtld); tx.commit(); } catch (Exception e) { if
	 * (tx!=null) tx.rollback(); throw e; } finally { session.close(); } return
	 * retVal;
	 * 
	 * }
	 */
	// delete from cohort_detail where cohort_Id not in (1058);
	// delete from cohort where cohort_id not in (1058);
	/**
	 * This method is used to flush all the cohort records from db which are not
	 * finalize.
	 * 
	 * @param regionId
	 * @throws Exception
	 */

	@Override
	public void flushUnfinalizeCohort(Integer regionId) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In flushUnfinalizeCohort in time "
				+ System.currentTimeMillis());
		Session session = null;
		try {
			session = sessionFactory.openSession();

			session.beginTransaction();
			Query query = (Query) session
					.createQuery("delete CohortDetail cd where cd.cohort.id  IN (select id from Cohort where region.regionId = :regionId and isFinalCohort=false)");
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			int result = query.executeUpdate();
			query = (Query) session
					.createQuery("delete Cohort c where  region.regionId = :regionId and isFinalCohort=false");
			query.setParameter("regionId", regionId);
			query.setCacheable(true);
			result = query.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
			logger.error(
					"Exception has occured while flush unfinalize cohort ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out flushUnfinalizeCohort executin time taken " + timeDiff);
	}

	/**
	 * This method calls the db to update cohortdetails table record.
	 * 
	 * @param cohort
	 *            Cohort which needs to be added/updated.
	 * 
	 * @return none
	 */
	@Override
	public void deleteCohortDetail(CohortDetail cohortDetail) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In deleteCohortDetail in time "
				+ System.currentTimeMillis());
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(cohortDetail);
			tx.commit();

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
				logger.error(
						"Exception has occured while deleting Cohort Detail  ",
						e);
				throw e;
			}
		} finally {
			// Added by Divesh Solanki session should be closed here.
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out deleteCohortDetail executin time taken " + timeDiff);
	}

	/**
	 * Method to get MTLD object by id.
	 * 
	 * @param mtldId
	 *            The mtld for which object needs to be fetched.
	 * 
	 * @return MTLD object.
	 */
	@Override
	@Transactional(readOnly = true)
	public MTLD getMTLDById(int mtldId) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In getMTLDById(int mtldId) in time "
				+ System.currentTimeMillis());
		Session session = sessionFactory.openSession();
		MTLD mtld = null;
		try {
			mtld = (MTLD) session.get(MTLD.class, mtldId);

		} catch (Exception e) {
			logger.error("Exception at getMTLDById ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out getMTLDById(int mtldId) executin time taken "
				+ timeDiff);
		return mtld;
	}

	@Override
	public Integer saveCorpsMember(CorpsMember corps) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In saveCorpsMember in time " + System.currentTimeMillis());
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer retVal = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CorpsMember.class);
			criteria.add(Restrictions.eq("tfaMasterUID",
					corps.getTfaMasterUID()));
			criteria.setCacheable(true);
			List existingCorps = criteria.list();
			if (existingCorps != null && existingCorps.size() > 0) {
				CorpsMember existingCorpsMember = (CorpsMember) existingCorps
						.get(0);
				if (existingCorpsMember.getRegion().getRegionId() == corps
						.getRegion().getRegionId()) {

					existingCorpsMember.setFirstName(corps.getFirstName());
					existingCorpsMember.setLastName(corps.getLastName());
					existingCorpsMember.setHiredStatus(corps.getHiredStatus());
					existingCorpsMember.setGradeLevel(corps.getGradeLevel());
					existingCorpsMember.setSchool(corps.getSchool());
					existingCorpsMember
							.setSubjectGroup(corps.getSubjectGroup());
					existingCorpsMember.setSubjectModifier(corps
							.getSubjectModifier());
					existingCorpsMember.setUpdatedBy(corps.getCreatedBy());
					existingCorpsMember.setUpdatedDate(corps.getCreatedDate());

					session.saveOrUpdate(existingCorpsMember);
					retVal = corps.getId();
				}
			} else {
				retVal = (Integer) session.save(corps);
			}

			if (tx != null) {
				tx.commit();
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception at saveCorpsMember ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out saveCorpsMember executin time taken " + timeDiff);
		return retVal;

	}

	@Override
	public MTLD getMTLDByTFAId(String placementAdvisorTfaId) throws Exception {
		Session session = sessionFactory.openSession();
		MTLD mtld = null;
		try {

			Criteria criteria = session.createCriteria(MTLD.class).add(
					Restrictions.eq("PlacementAdvisorTfaId",
							placementAdvisorTfaId));
			criteria.setCacheable(true);
			List<MTLD> mtlds = criteria.list();

			if (mtlds != null) { //TO DO : Do we need to check for list size should be one
				mtld = mtlds.get(0);
			}

		} catch (Exception e) {
			logger.error("Exception at getMTLDById ", e);
			throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		}
		return mtld;
	}

}
