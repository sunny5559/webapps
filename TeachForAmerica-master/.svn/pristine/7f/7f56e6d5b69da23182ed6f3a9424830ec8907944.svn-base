package org.tfa.mtld.web.controller.ws;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.service.bean.UserBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.services.UserService;
import org.tfa.mtld.web.exception.TFAUserException;
import org.tfa.mtld.web.validator.LoginValidator;

/**
 * This class is the used as controller for all user related operations
 * 
 * @author arun.rathore
 * @version 1.0, 25 March, 2014.
 * 
 */
@Controller
public class UserController {
	Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	@Qualifier("UserService")
	private UserService userService;

	@Autowired
	LoginValidator loginValidator;

	/**
	 * Method for show login page.
	 */
	@RequestMapping(value = "/login")
	public ModelAndView signIn(Map<String, Object> map, Model model,
			HttpServletRequest request) throws Exception {
		logger.debug("In Login Method");
		HttpSession session = request.getSession();
		User userResult = (User) session
				.getAttribute(TFAConstants.SESSION_LOGGEDIN_USER_REGION);
		if (userResult != null && userResult.getUserId() != null
				&& userResult.getIsLoggedIn() == true) {
			return new ModelAndView(TFAConstants.REDIRECT_TO_SELECT_CRITERIA);

		} else {
			map.put("UserBean", new UserBean());
			return new ModelAndView(TFAConstants.LOGIN_PAGE);

		}

	}

	/**
	 * This method perform the user login, if user not exist then it will
	 * redirect user to login screen with message. Also it Checks validity of
	 * User credential. If valid then create User session and redirect to
	 * criteria screen otherwise redirect to login screen with error message.
	 * 
	 * @param userBean
	 * @param result
	 * @param model
	 * @param request
	 * @return View
	 */
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public String login(@ModelAttribute("UserBean") UserBean userBean,
			BindingResult result, ModelMap model, HttpServletRequest request)
			throws TFAUserException, Exception {
		logger.debug("In side login method");
		loginValidator.validate(userBean, result);
		User loginResult = null;
		try {
			if (result.hasErrors()) {
				return TFAConstants.LOGIN_PAGE;
			}

			loginResult = userService.userLogin(userBean);
/*
			if (loginResult != null && loginResult.getIsLoggedIn() != null) {
				if (loginResult.getIsLoggedIn() == true) {
					result.rejectValue(TFAConstants.LOGIN_ID,
							"error.loginId.already.loggedIn");
					return TFAConstants.LOGIN_PAGE;
				}

			}
*/
			if (loginResult != null && !"".equals(loginResult)) {
				loginResult.setIsLoggedIn(true);
				request.getSession().setAttribute(
						TFAConstants.SESSION_LOGGEDIN_USER_REGION, loginResult);
			} else {
				result.rejectValue(TFAConstants.LOGIN_ID,
						"error.loginId.required");
				return TFAConstants.LOGIN_PAGE;
			}
		} catch (Exception e) {
			logger.error("Exception at login", e);
			throw new TFAUserException("User Not Found" + e.getMessage());
		}
		return TFAConstants.REDIRECT_TO_SELECT_CRITERIA;
	}

	/**
	 * This method is used to logout the user from his/her current session.
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelMap model, HttpServletRequest request)
			throws TFAUserException, Exception {
		long startTime = System.currentTimeMillis();
		logger.info("In Log out method " + System.currentTimeMillis());
		User user = (User) request.getSession().getAttribute(
				TFAConstants.SESSION_LOGGEDIN_USER_REGION);
		user.setIsLoggedIn(false);
		request.getSession().setAttribute(
				TFAConstants.SESSION_LOGGEDIN_USER_REGION, user);
		userService.updateUserLoggedInStatus(false, user.getUserId());
		request.getSession(false).invalidate();
		long endTime = System.currentTimeMillis();
		long timeDiff = endTime - startTime;
		logger.info("Out from Log out method " + timeDiff);
		return new ModelAndView(TFAConstants.REDIRECT_TO_LOGIN);

	}

	/**
	 * This method is used to check the access to any page, if session is not
	 * exist then it sends user to access denied page. redirect user to login
	 * screen
	 * 
	 * @param request
	 * @return void
	 */
	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public String loginerror(ModelMap model, HttpServletRequest request) {
		request.getSession(false).invalidate();
		model.addAttribute("error", "true");
		return TFAConstants.ACCESS_DENIED_PAGE;
	}

}
