package org.tfa.mtld.service.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.scoring.MTLDToCMRatioScoringCriteria;
import org.tfa.mtld.scoring.MinimizeTravelDistanceScoringCriteria;
import org.tfa.mtld.scoring.ScoringCriteriaStrategy;
import org.tfa.mtld.service.bean.CMBean;
import org.tfa.mtld.service.bean.CohortBean;
import org.tfa.mtld.service.bean.CohortDetailBean;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.bean.MTLDBean;
import org.tfa.mtld.service.bean.UserBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;
import org.tfa.mtld.service.exception.TFAMTLDPerCohortException;

@ContextConfiguration(locations = { "classpath:applicationContext-service.xml" })
public class CohortUpdateServiceTest {
	@Autowired
	public CohortService cohortService;

	@Autowired
	public CohortUpdateService cohortUpdateService;

	@Autowired
	public CriteriaService criteriaService;

	@Autowired
	public UserService userService;

	List<CriteriaBean> criteriaBeanList;

	Map<String, List<CriteriaBean>> criteriaList;

	User user = null;

	@Before
	public void init() throws Exception {
		
			if (cohortService == null || criteriaService == null
					|| userService == null) {
				ApplicationContext context = new ClassPathXmlApplicationContext(
						"applicationContext-service.xml");
				cohortService = (CohortService) context
						.getBean("cohortServiceImpl");

				criteriaService = (CriteriaService) context
						.getBean("criteriaServiceImpl");

				userService = (UserService) context.getBean("userServiceImpl");

				cohortUpdateService = (CohortUpdateService) context
						.getBean("cohortUpdateServiceImpl");
			}
			Assert.assertNotNull(cohortService);
			Assert.assertNotNull(criteriaService);
			Assert.assertNotNull(userService);
			Assert.assertNotNull(cohortUpdateService);
			

			criteriaList = criteriaService.getCriteriaList();

			criteriaBeanList = getCriteriaList(criteriaList);

			user = getUser("cohort", "cohort");

	}
	
	@After
	public void destroy() throws Exception {
		
			cohortService.flushUnfinalizeCohort(4); 
											
	}
	
	/*
	 * Below test case checks the method addCMToCohort which adds the passed CM
	 * to the specified cohort.
	 */
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testAddCMToCohort() throws TFAInvalidCohortException, Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,5, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);
			// For Unhired CM
			List<CMBean> cmUnhiredBean = (List<CMBean>) cohortMap
					.get(TFAConstants.UNHIRED_CORPS_MEMBER);
			if (cmUnhiredBean != null && cmUnhiredBean.size() > 0
					&& cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				int cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();
				CohortBean cohortBean = cohortUpdateService.addCMToCohort(
						criteriaList, true, cmUnhiredBean.get(0).getId(),
						cohortId, criteriaBeanList);
				CohortBean cohort = cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList);
				Assert.assertEquals("Executed Successful",
						cohort.getCohortDetailBean().size(), cohortBean.getCohortDetailBean().size());
			}

			// For hired CM
			List<CMBean> cmHiredBean = (List<CMBean>) cohortMap
					.get(TFAConstants.HIRED_CORPS_MEMBER);
			if (cmHiredBean != null && cmHiredBean.size() > 0
					&& cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				int cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();
				CohortBean cohortBean = cohortUpdateService.addCMToCohort(
						criteriaList, true, cmHiredBean.get(0).getId(),
						cohortId, criteriaBeanList);
				CohortBean cohort = cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList);
				Assert.assertEquals("Executed Successful",
						cohort.getCohortDetailBean().size(), cohortBean.getCohortDetailBean().size());
			}
	}

	/*
	 * Below test case checks the method removeCMFromCohort which removes the
	 * passed CM to the specified cohort.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveCMFromCohort() throws TFAInvalidCohortException, Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,6, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);
			List<CohortBean> cohortBeanList = (List<CohortBean>) cohortMap
					.get(TFAConstants.COHORT_LIST);

			if (cohortBeanList != null && cohortBeanList.size() > 0		
					&& cohortBeanList.get(0).getCohortDetailBean() != null
					&& cohortBeanList.get(0).getCohortDetailBean().size() > 0) {
				int corpMemberId =0;
				Iterator<CohortDetailBean> iterable = cohortBeanList.get(0).getCohortDetailBean().iterator();
				while(iterable.hasNext()){
					CohortDetailBean cohortDetailBean = iterable.next();
					corpMemberId = cohortDetailBean.getCorpsMember().getId();
					break;
				}
				
				
				int cohortId = cohortBeanList.get(0).getId();
				CohortBean cohortBean = cohortUpdateService.removeCMFromCohort(
						corpMemberId, cohortId, criteriaBeanList);
				CohortBean cohort = cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList);
				Assert.assertEquals("Executed Successful",
						cohort.getCohortDetailBean().size(), cohortBean.getCohortDetailBean().size());
			}
	}

	/*
	 * Below test case checks the method saveUpdatedCohort which updates the
	 * finalCohort flag as true in database for the specified cohort.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveUpdatedCohort() throws TFAInvalidCohortException, Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,7, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);

			if (cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				int cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();
				cohortUpdateService.saveUpdatedCohort(cohortId,null,
						user.getLoginId());
				CohortBean cohort = cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList);
				cohortUpdateService.unlockCohort(cohortId, user.getLoginId());
				Assert.assertEquals("Executed Successful",
						cohort.getIsFinalCohort(), true);
				// To-Do isFinalCohort flag will be marked as true in database.
			}
	}

	/*
	 * Below test case checks the method unlockCohort which updates the
	 * finalCohort flag as false in database for the specified cohort.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testUnlockCohort() throws Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,8, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);

			if (cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				int cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();
				cohortUpdateService.unlockCohort(cohortId, user.getLoginId());
				CohortBean cohort = cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList);
				Assert.assertEquals("Executed Successful",
						cohort.getIsFinalCohort(), false);
			}
	}

	/*
	 * Below test case checks the method getCohortDetails which fetches the
	 * cohort object from database for the specified cohortId.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetCohortDetails() throws Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,2, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);

			if (cohortMap.get(TFAConstants.COHORT_LIST) != null
					&& cohortMap.get(TFAConstants.COHORT_LIST).size() > 0) {
				Integer cohortId = ((List<CohortBean>) cohortMap
						.get(TFAConstants.COHORT_LIST)).get(0).getId();
				Assert.assertEquals("Executed Successful", cohortUpdateService
						.getCohortDetails(cohortId,criteriaBeanList).getId(), cohortId);
			}
	}
	
	/*
	 * Below test case checks the method addSeededMember which adds the
	 * seeded MTLD in the specified cohort.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddSeededMember() throws TFAInvalidCohortException, TFAMTLDPerCohortException, Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,5, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);
			List<CohortBean> cohortBeanList = (List<CohortBean>) cohortMap
					.get(TFAConstants.COHORT_LIST);

			if (cohortBeanList != null && cohortBeanList.size() > 0		
					&& cohortBeanList.get(0).getCohortDetailBean() != null
					&& cohortBeanList.get(0).getCohortDetailBean().size() > 0) {
				Integer mtldId =0;
				List<MTLDBean> mtlds= (List<MTLDBean>) cohortMap.get(TFAConstants.MTLD_LIST);
				if(mtlds.size()>0)
				{
					mtldId=  mtlds.get(0).getId();
				}
				Integer cohortId = cohortBeanList.get(0).getId();
				
				if(cohortBeanList.get(0).getSeededMtldBean()!=null && mtldId!=0)
				{
					CohortBean cohortBean = cohortUpdateService.addSeededMember(
							mtldId, cohortId, criteriaBeanList);
					CohortBean cohort = cohortUpdateService
							.getCohortDetails(cohortId,criteriaBeanList);
					Assert.assertEquals("Executed Successful", cohort.getSeededMtldBean().getId(), mtldId);
				}
			}
	}
	
	/*
	 * Below test case checks the method removeSeededMember which removes the specified MTLD
	 * from the cohort.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveSeededMember() throws Exception {
			Map<String, List<?>> cohortMap = cohortService.createGroup(
					criteriaBeanList,10, Boolean.TRUE, Boolean.FALSE,
					Boolean.TRUE, user);
			List<CohortBean> cohortBeanList = (List<CohortBean>) cohortMap
					.get(TFAConstants.COHORT_LIST);

			if (cohortBeanList != null && cohortBeanList.size() > 0		
					&& cohortBeanList.get(0).getCohortDetailBean() != null
					&& cohortBeanList.get(0).getCohortDetailBean().size() > 0) {
				if(cohortBeanList.get(0).getSeededMtldBean()!=null)
				{
					int mtldId = cohortBeanList.get(0).getSeededMtldBean().getId();
					int cohortId = cohortBeanList.get(0).getId();
					CohortBean cohortBean = cohortUpdateService.removeSeededMember(
							TFAConstants.SEEDED_MEMBER_MTLD,mtldId, cohortId, criteriaBeanList);
					CohortBean cohort = cohortUpdateService
							.getCohortDetails(cohortId,criteriaBeanList);
					Assert.assertNull(cohort.getSeededMtldBean());
				}
			}
	}

	@SuppressWarnings("rawtypes")
	private List getCriteriaList(Map<String, List<CriteriaBean>> criteriaList) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<CriteriaBean> criteriaBeanList = new ArrayList<CriteriaBean>();
		ScoringCriteriaStrategy scoringCriteriaStrategy = null;

		for (Entry entry : criteriaList.entrySet()) {
			List<CriteriaBean> criteriaList1 = (List<CriteriaBean>) entry
					.getValue();

			for (CriteriaBean criteriaBean : criteriaList1) {

				criteriaBean.setPriorityValue(2);

				if (TFAConstants.MTLDTOCMRATIOSCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {

					criteriaBean.setFieldValue("10");
					scoringCriteriaStrategy = new MTLDToCMRatioScoringCriteria(
							Integer.parseInt(criteriaBean.getFieldValue()));

				} else if (TFAConstants.MINIMIZETRAVELDISTANCESCORINGCRITERIA
						.equalsIgnoreCase(criteriaBean.getClassAPI().trim())
						&& criteriaBean.getPriorityValue() != null
						&& criteriaBean.getPriorityValue() != 0) {
					criteriaBean.setFieldValue("2");
					scoringCriteriaStrategy = new MinimizeTravelDistanceScoringCriteria(
							Double.parseDouble(criteriaBean.getFieldValue()));

				} else if (criteriaBean.getPriorityValue() != null) {
					
						scoringCriteriaStrategy = (ScoringCriteriaStrategy) Class
								.forName(
										"org.tfa.mtld.scoring."
												+ criteriaBean.getClassAPI()
														.trim()).newInstance();
					
				}
				criteriaBean
						.setScoringCriteriaStrategy(scoringCriteriaStrategy);

				criteriaBeanList.add(criteriaBean);

			}

		}
		// for priority 0-----0
		// for priority 1-----1
		// for priority 2-----2
		// for priority 3-----4
		// for priority 4-----8

		criteriaBeanList.get(0).setPriorityValue(0);

		criteriaBeanList.get(1).setPriorityValue(1);

		criteriaBeanList.get(2).setPriorityValue(1);

		criteriaBeanList.get(3).setPriorityValue(1);

		criteriaBeanList.get(4).setPriorityValue(1);

		criteriaBeanList.get(5).setPriorityValue(1);

		criteriaBeanList.get(6).setPriorityValue(1);

		criteriaBeanList.get(7).setPriorityValue(1);

		criteriaBeanList.get(8).setPriorityValue(1);

		criteriaBeanList.get(9).setPriorityValue(1);

		criteriaBeanList.get(10).setPriorityValue(1);

		criteriaBeanList.get(11).setPriorityValue(1);

		criteriaBeanList.get(12).setPriorityValue(1);

		criteriaBeanList.get(13).setPriorityValue(1);

		criteriaBeanList.get(14).setPriorityValue(1);

		return criteriaBeanList;
	}

	private User getUser(String userName, String password) throws Exception {

		UserBean userBean = new UserBean();
		userBean.setLoginId(userName);
		userBean.setPassword(password);
		User user = null;
		
			user = userService.userLogin(userBean);
		

		return user;
	}
}
