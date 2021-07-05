package org.tfa.mtld.service.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.tfa.mtld.data.model.Region;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.UserBean;

@ContextConfiguration(locations = { "classpath:applicationContext-service.xml" })
public class UserServiceTest {

	@Autowired
	public UserService userService;

	@Before
	public void setUp() {

			if (userService == null) {
				ApplicationContext context = new ClassPathXmlApplicationContext(
						"applicationContext-service.xml");
				userService = (UserService) context.getBean("userServiceImpl");
			}
			Assert.assertNotNull(userService);
		
	}

	/*
	 * Below test case is for User Login, need to pass loginId and password to
	 * run this test case. This test case runs with correct loginId and
	 * password. specific user.
	 */
	@Test
	public void testUserLoginWithCorrectUser() throws Exception {
		Assert.assertNotNull(userService);
			UserBean userBean = new UserBean();
			userBean.setLoginId("tfa");
			userBean.setPassword("tfa");
			User userResult = userService.userLogin(userBean);

			User user = new User();
			Region region = new Region();
			region.setRegionId(2);
			user.setLoginId("tfa");
			user.setPassword("tfa");
			user.setRegion(region);

			Assert.assertEquals(user.getLoginId(), userResult.getLoginId());
			Assert.assertEquals(user.getPassword(), userResult.getPassword());

		
	}

	/*
	 * Below test case is for User Login, need to pass loginId and password to
	 * run this test case. This test case runs with wrong loginId and correct
	 * password. specific user.
	 */
	@Test
	public void testUserLoginWithInCorrectUser() throws Exception {
		Assert.assertNotNull(userService);
			UserBean userBean = new UserBean();
			userBean.setLoginId("abc#$%%%%");
			userBean.setPassword("tfa");
			User userResult = userService.userLogin(userBean);

			User user = new User();
			Region region = new Region();
			region.setRegionId(2);
			user.setLoginId("tfa");
			user.setPassword("tfa");
			user.setRegion(region);

			Assert.assertEquals(user.getLoginId(), userResult.getLoginId());
			Assert.assertEquals(user.getPassword(), userResult.getPassword());

	}

	/*
	 * Below test case is for User Login, need to pass loginId and password to
	 * run this test case. This test case runs with correct loginId and wrong
	 * password. specific user.
	 */
	@Test
	public void testUserLoginWithInCorrectPassword()throws Exception {
		Assert.assertNotNull(userService);

			UserBean userBean = new UserBean();
			userBean.setLoginId("tfa");
			userBean.setPassword("xyz");
			User userResult = userService.userLogin(userBean);

			User user = new User();
			Region region = new Region();
			region.setRegionId(2);
			user.setLoginId("tfa");
			user.setPassword("tfa");
			user.setRegion(region);

			Assert.assertEquals(user.getLoginId(), userResult.getLoginId());
			Assert.assertEquals(user.getPassword(), userResult.getPassword());

	
	}

	/*
	 * Below test case is for User Login, need to pass loginId and password to
	 * run this test case. This test case runs with wrong loginId and password.
	 * specific user.
	 */
	@Test
	public void testUserLoginWithInCorrectUserAndPassword()throws Exception {
		Assert.assertNotNull(userService);
			UserBean userBean = new UserBean();
			userBean.setLoginId("abc");
			userBean.setPassword("xyz");
			User userResult = userService.userLogin(userBean);

			User user = new User();
			Region region = new Region();
			region.setRegionId(2);
			user.setLoginId("tfa");
			user.setPassword("tfa");
			user.setRegion(region);

			Assert.assertEquals(user.getLoginId(), userResult.getLoginId());
			Assert.assertEquals(user.getPassword(), userResult.getPassword());

	}

}
