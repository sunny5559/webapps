package org.tfa.mtld.data.repository;

import java.util.List;

import org.tfa.mtld.data.model.Cohort;
import org.tfa.mtld.data.model.CohortDetail;
import org.tfa.mtld.data.model.CorpsMember;
import org.tfa.mtld.data.model.MTLD;

public interface MtldCmRepository {

	// Will return list of CM including hired and unhired, and only unassigned
	// this will not include any assigned cm which are there in finalize
	// cohort(s).
	public List<CorpsMember> getCorpMemberListByRegionId(Integer regionId)
			throws Exception;

	public List<MTLD> getMTLDListByRegionId(Integer regionId) throws Exception;

	// Will return list of CM including only hired, and only unassigned this
	// will not include any assigned cm which are there in finalize cohort(s).
	public List<CorpsMember> getHiredCorpMemberListByRegionId(Integer regionId) throws Exception;

	// Will return list of CM including unhired, and only unassigned this will
	// not include any assigned cm which are there in finalize cohort(s).
	List<CorpsMember> getUnhiredCorpMemberListByRegionId(Integer regionId) throws Exception;

	public Integer saveMTLD(MTLD mtld) throws Exception;

	/**
	 * Will return list of cohorts which are finalize for a region
	 * 
	 * @param regionId
	 * @return List of Cohort
	 * @throws Exception
	 * 
	 * @author lovely.ram
	 */
	public List<Cohort> getCohortListByRegionId(Integer regionId)
			throws Exception;

	/**
	 * This method calls the db to save/update the cohort.
	 * 
	 * @param cohort
	 *            Cohort which needs to be added/updated.
	 * 
	 * @return none
	 */
	public void saveCohort(Cohort cohort) throws Exception;
	public void saveCohortList(List<Cohort> cohortListToSave) throws Exception;

	/**
	 * Method to get CorpsMember object by id.
	 * 
	 * @param corpsMemberId
	 *            The corps member for which object needs to be fetched.
	 * 
	 * @return CorpsMember object.
	 */
	public CorpsMember getCorpsMemberById(int corpsId) throws Exception;

	/**
	 * This method returns the cohort object by id.
	 * 
	 * @param cohortId
	 *            cohort id for which object needs to be fetched.
	 * 
	 * @return Cohort object.
	 */
	public Cohort getCohort(Integer cohortId) throws Exception;

	/**
	 * ' This method is used to flush all the cohort records from db which are
	 * not finalize.
	 * 
	 * @param regionId
	 * @throws Exception
	 */
	public void flushUnfinalizeCohort(Integer regionId) throws Exception;


	/**
	 * This method calls the db to update cohortdetails table record.
	 * 
	 * @param cohort
	 *            Cohort which needs to be added/updated.
	 * 
	 * @return none
	 */
	public void deleteCohortDetail(CohortDetail cohortDetail) throws Exception;
	
	/**
	 * Method to get MTLD object by id.
	 * 
	 * @param mtldId
	 *            The mtld for which object needs to be fetched.
	 * 
	 * @return MTLD object.
	 */
	public MTLD getMTLDById(int mtldId) throws Exception;
	
	public List<MTLD> getMTLDList() throws Exception;
	public void updateMTLDLatLong(MTLD mtld) throws Exception;

	
	/**
	 * Method for storing the corpsmember
	 *  @param corps
	 *  @return Integer.
	 *  @throws Exception
	 *  
	 *  @author lovely.ram
	 */
	public Integer saveCorpsMember(CorpsMember corps)throws Exception;
	
	
	
	public MTLD getMTLDByTFAId(String PlacementAdvisorTfaId) throws Exception;

	List<MTLD> getUnassignedMTLDListByRegionId(Integer regionId)
			throws Exception;
	
}
