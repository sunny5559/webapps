/**
 * 
 */
package org.tfa.mtld.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tfa.mtld.data.model.User;
import org.tfa.mtld.data.repository.UserRepository;
import org.tfa.mtld.service.constants.TFAConstants;

/**
 * @author arun.rathore
 * 
 */
public class WebUrlInterceptor extends HandlerInterceptorAdapter {
	Logger logger = Logger.getLogger(WebUrlInterceptor.class);


	public static final String AUTHENTICATION_LOGIN_URI = TFAConstants.AUTHENTICATION_LOGIN_URI;


	@Autowired
	UserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();
		
		logger.info("Interception Web Url [" + uri + "]");
		String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
		User userData = (User) request.getSession().getAttribute(
				TFAConstants.SESSION_LOGGEDIN_USER_REGION);
		if(uri.contains("/login") && userData != null){
			response.sendRedirect("/selectCriteria");
			return false;
		}else if(userData == null){
			if ("XMLHttpRequest".equals(ajaxHeader)) 
			{
				response.sendError(601);
			}
			else
			{
				request.getRequestDispatcher(AUTHENTICATION_LOGIN_URI)
				.forward(request, response);
			}
			logger.warn("WebUrlInterceptor: Invalid session.");
			return false;
		}else {
            return true;
        }
	}
}
	