package org.tfa.mtld.web.exception;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.exception.TFAInvalidCohortException;

/**
 * This class is written to handle all global exceptions in system. If any
 * exceptions comes in the system it will send them to a generic page with
 * Custom message.
 * 
 * @author arun.rathore
 * @version 1.0, 11 April, 2014.
 */

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	protected Logger logger;

	public GlobalDefaultExceptionHandler() {
		logger = Logger.getLogger(GlobalDefaultExceptionHandler.class);
	}

	@ExceptionHandler(TFAUserException.class)
	public ModelAndView handleCustomException(TFAUserException ex) {
		logger.error("TFAUserException Exception is :" + ex.getMessage());
		ModelAndView model = new ModelAndView("error/custom_error");
		model.addObject(TFAConstants.ERROR_MESSAGE, ex.getMessage());
		return model;

	}

	@ExceptionHandler(TFAException.class)
	public ModelAndView handleCustomException(TFAException ex) {
		logger.error("TFAUserException Exception is :" + ex.getMessage());
		ModelAndView model = new ModelAndView("error/custom_error");
		model.addObject(TFAConstants.ERROR_MESSAGE, ex.getMessage());
		return model;

	}


	@ExceptionHandler(value = TFAInvalidCohortException.class)
	public ModelAndView TFAInvalidCohortException(TFAInvalidCohortException ex) {
		logger.error("Exception is handled in GlobalDefaultExceptionHandler"
				+ ex.getMessage());
		ModelAndView model = new ModelAndView("error/custom_errorr");
		model.addObject(TFAConstants.ERROR_MESSAGE, ex.getMessage());
		return model;

	}

	@ExceptionHandler(value = Exception.class)
	public ModelAndView handleAllException(Exception ex) {
		logger.error("Exception is handled in GlobalDefaultExceptionHandler"
				+ ex.getMessage());
		ModelAndView model = new ModelAndView(TFAConstants.ERROR_PAGE);
		model.addObject(TFAConstants.ERROR_MESSAGE, ex.getMessage());
		return model;

	}
}
