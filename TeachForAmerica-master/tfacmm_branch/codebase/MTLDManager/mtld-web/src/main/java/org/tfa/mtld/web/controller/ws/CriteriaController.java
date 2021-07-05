package org.tfa.mtld.web.controller.ws;

import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tfa.mtld.service.bean.CriteriaFormBean;
import org.tfa.mtld.service.constants.TFAConstants;
import org.tfa.mtld.service.services.CriteriaService;
import org.tfa.mtld.service.utils.CriteriaScoringUtils;
import org.tfa.mtld.web.exception.TFAException;

import com.google.gson.Gson;

@Controller
public class CriteriaController {

	@Autowired
	CriteriaService criteriaService;

	Logger logger = Logger.getLogger(CriteriaController.class);

	/**
	 * @param model
	 * @param session
	 * @return String. Below method is used to set criteria list in model to
	 *         iterate it on Criteria Page.
	 */


	@SuppressWarnings("static-access")
	@RequestMapping(value = "/selectCriteria", method = RequestMethod.GET)
	public String selectCriteria(Model model, HttpSession session)
			throws TFAException {

		try {

			if (session
					.getAttribute(TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING) != null) {
				Gson criteriaGson = new Gson();
				model.addAttribute(
						TFAConstants.COMMAND_CRITERIA_FORM_BEAN,
						criteriaGson
								.fromJson(
										session.getAttribute(
												TFAConstants.SESSION_CRITERIA_FORM_BEAN_STRING)
												.toString(),
										CriteriaFormBean.class));

			} else {
				CriteriaFormBean criteriaFormBean = criteriaService
						.getCriteriaFormBean();

				model.addAttribute(TFAConstants.COMMAND_CRITERIA_FORM_BEAN,
						criteriaFormBean);
				Calendar cal = Calendar.getInstance();
				CriteriaScoringUtils.CURRENTCORPYEAR = cal.get(cal.YEAR);
			}

			
		} catch (Exception e) {
			logger.error("Exception in CriteriaController.selectCriteria()", e);
			throw new TFAException();
		}
		return "criteriaList";
	}

}
