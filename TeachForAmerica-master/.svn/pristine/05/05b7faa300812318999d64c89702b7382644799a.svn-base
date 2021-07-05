package org.tfa.mtld.service.services;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tfa.mtld.data.model.Region;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.UserRepository;
import org.tfa.mtld.service.bean.RegionBean;
import org.tfa.mtld.service.bean.UserBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserServiceMockTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	UserBean userBean;
	User expectedUser;

	@Configuration
	static class UserServiceMockTestContextConfiguration {
		@Bean
		public UserServiceImpl userService() {
			return new UserServiceImpl();
		}

		@Bean
		public UserRepository userRepository() {
			return Mockito.mock(UserRepository.class);
		}
	}

	@Before
	public void setup() {
		expectedUser = new User();
		expectedUser.setUserId(5);
		expectedUser.setLoginId("tfatest");
		expectedUser.setPassword("tfatest");
		expectedUser.setFirstName("TFA");
		expectedUser.setLastName("User");
		expectedUser.setEmailId("tfa@tfa.com");
		expectedUser.setIsLoggedIn(true);
		expectedUser.setUserId(5);
		Region region = new Region();
		region.setRegionId(15);
		expectedUser.setRegion(region);

		try {
			Mockito.when(userRepository.userLogin("tfatest", "tfatest"))
					.thenReturn(expectedUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void testUserLogin() throws Exception {
		userBean = new UserBean();
		userBean.setLoginId("tfatest");
		userBean.setPassword("tfatest");
		RegionBean regionBean = new RegionBean();
		regionBean.setRegionId(15);
		userBean.setRegionBean(regionBean);

		User user = userService.userLogin(userBean);
		assertEquals("TFA", user.getFirstName());
		assertEquals("User", user.getLastName());
		assertTrue(user.equals(expectedUser));
	}

	@Test(expected = Exception.class)
	public void testUserLoginInCorrectPassword() throws Exception {
		userBean = new UserBean();
		userBean.setLoginId("tfatest");
		userBean.setPassword("wrong");
		RegionBean regionBean = new RegionBean();
		regionBean.setRegionId(15);
		userBean.setRegionBean(regionBean);

		User user = userService.userLogin(userBean);

		assertEquals("TFA", user.getFirstName());
		assertEquals("User", user.getLastName());
		assertTrue(user.equals(expectedUser));
	}

	@Test(expected = Exception.class)
	public void testUserLoginInCorrectUser() throws Exception {
		userBean = new UserBean();
		userBean.setLoginId("wrong");
		userBean.setPassword("tfatest");
		RegionBean regionBean = new RegionBean();
		regionBean.setRegionId(15);
		userBean.setRegionBean(regionBean);

		User user = userService.userLogin(userBean);

		assertEquals("TFA", user.getFirstName());
		assertEquals("User", user.getLastName());
		assertTrue(user.equals(expectedUser));
	}

	@Test(expected = Exception.class)
	public void testUserLoginInCorrectUserAndPassword() throws Exception {
		userBean = new UserBean();
		userBean.setLoginId("wrong");
		userBean.setPassword("wrong");
		RegionBean regionBean = new RegionBean();
		regionBean.setRegionId(15);
		userBean.setRegionBean(regionBean);

		User user = userService.userLogin(userBean);

		assertEquals("TFA", user.getFirstName());
		assertEquals("User", user.getLastName());
		assertTrue(user.equals(expectedUser));
	}

	@Test(expected = Exception.class)
	public void testUpdateUserStatusLoggedIn() throws Exception {

		userService.updateUserLoggedInStatus(true, 5);

		assertEquals(userBean.isLoggedIn(), expectedUser.getIsLoggedIn());
		assertTrue(userBean.isLoggedIn().equals(expectedUser.getIsLoggedIn()));
		assertEquals(new Integer("5"), expectedUser.getUserId());

	}

	@Test(expected = Exception.class)
	public void testUpdateUserStatusLoggedOut() throws Exception {

		userService.updateUserLoggedInStatus(false, 5);

		assertEquals(userBean.isLoggedIn(), expectedUser.getIsLoggedIn());
		assertTrue(userBean.isLoggedIn().equals(expectedUser.getIsLoggedIn()));
		assertEquals(new Integer("5"), expectedUser.getUserId());

	}

	@After
	public void verify() throws Exception {
		
			// Mockito.verify(userRepository, VerificationModeFactory.times(1))
			// .userLogin("tfatest", "tfatest");
	
		Mockito.reset(userRepository);
	}
}
