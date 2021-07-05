package org.tfa.mtld.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.tfa.mtld.service.bean.UserBean;
import org.tfa.mtld.service.constants.TFAConstants;

/**
 * This class is created for form validations.
 * 
 * @author arun.rathore
 */
@Component
public class LoginValidator implements Validator {
	Logger logger = Logger.getLogger(LoginValidator.class);

	public boolean supports(Class<?> clazz) {
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("In side validate");
		UserBean user = (UserBean) target;
		boolean flag = false;
		if (StringUtils.hasText(user.getPassword())) {
			validatePassword(user.getPassword(), errors);
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
					"error.password.empty");
			flag = true;
		}

		ValidationUtils.rejectIfEmpty(errors, TFAConstants.LOGIN_ID, "error.loginId.empty");
	}

	public static void validatePassword(String user, Errors errors) {
		final String REGEX_PWD_US_1 = "(?=^.{6,18}$)(?=.*[a-zA-Z])(?=.*[0-9\\W])(?!.*\\s).*$";

		final String REGEX_PWD_US_2 = "(?=^.{6,18}$)(?=.*[0-9])(?=.*[\\W])(?!.*\\s).*$";

		Pattern pattern = Pattern.compile(REGEX_PWD_US_1, Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(user != null ? user : "");
		boolean isValid = matcher.matches();

		if (!isValid) {
			pattern = Pattern.compile(REGEX_PWD_US_2, Pattern.UNICODE_CASE);
			matcher = pattern.matcher(user != null ? user : "");
			isValid = matcher.matches();
		}

		// Remove below comment to enable password validation.
		/*
		 * if(!isValid){ errors.rejectValue("password","error.password.rule"); }
		 */

	}
}
