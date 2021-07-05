package org.tfa.mtld.web.controller.ws;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.tfa.mtld.service.bean.CriteriaBean;
import org.tfa.mtld.service.services.CriteriaService;
import org.tfa.mtld.web.exception.TFAException;
import org.tfa.mtld.web.utils.CommonUtility;

@Controller
@SessionAttributes({ CommonUtility.SESSION_CRITERIA_MAP })
public class CriteriaController {

	@Autowired
	CriteriaService criteriaService;

	Logger logger = Logger.getLogger(CriteriaController.class);

	/**
	 * @param model
	 * @param session
	 * @return String.
	 *  Below method is used to set criteria list in model to
	 *         iterate it on Criteria Page.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectCriteria", method = RequestMethod.GET)
	public String selectCriteria(Model model,  HttpSession session
			) throws TFAException {

		try {

			
             model.addAttribute("criteriaBean", null!=(CriteriaBean)session.getAttribute("criteriaBean")?(CriteriaBean)session.getAttribute("criteriaBean"):new CriteriaBean());
			if (session.getAttribute(CommonUtility.SESSION_CRITERIA_MAP) != null) {
				model.addAttribute(CommonUtility.SESSION_CRITERIA_MAP,
						(Map<String, List<CriteriaBean>>) session
								.getAttribute(CommonUtility.SESSION_CRITERIA_MAP));
			} else {
				model.addAttribute(CommonUtility.SESSION_CRITERIA_MAP,
						criteriaService.getCriteriaList());
			}

		} catch (Exception e) {
			logger.error("Exception in CriteriaController.selectCriteria()"
					, e);
			throw new TFAException();
		}
		return "criteriaList";
	}

}
