package org.tfa.mtld.web.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.services.UserService;

@ContextConfiguration(locations = { "classpath:applicationContext-service.xml" })
public class TFASessionListener implements HttpSessionListener {
	Logger logger = Logger.getLogger(TFASessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		logger.info("Session is created" + sessionEvent.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		ApplicationContext context = null;
		try {
			long startTime = System.currentTimeMillis();
			logger.info("In TFASessionListener Log out method "
					+ System.currentTimeMillis());
			if (null != sessionEvent.getSession()) {
				User user = (User) sessionEvent.getSession().getAttribute(
						TFAConstants.SESSION_LOGGEDIN_USER_REGION);

				if (null != user && null != user.getIsLoggedIn()
						&& user.getIsLoggedIn()) {
/*
					context = new ClassPathXmlApplicationContext(
							"applicationContext-service.xml");

					UserService userService = (UserService) context
							.getBean("UserService");
					userService.updateUserLoggedInStatus(false,
							user.getUserId());
							*/
					long endTime = System.currentTimeMillis();
					long timeDiff = endTime - startTime;
					logger.info("Out from TFASessionListener Log out method "
							+ timeDiff);
				}
			}
		} catch (Exception e) {
			logger.error("Exception at sessionDestroyed", e);
		}
	}

}
